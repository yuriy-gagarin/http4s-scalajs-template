package main

import cats.effect._
import org.http4s._
import org.http4s.dsl.Http4sDsl

trait Static[F[_]] {
  def routes: HttpRoutes[F]
}

object Static {
  def apply[F[_] : Sync : ContextShift](blocker: Blocker)(implicit config: AppConfig) = new Static[F] with Http4sDsl[F] {

    def routes: HttpRoutes[F] = HttpRoutes.of {
      case req @ GET -> Root =>
        StaticFile.fromResource("/index.html", blocker, Some(req)).getOrElseF(NotFound())

      case req @ GET -> Root / "main.js" =>
        StaticFile.fromResource(s"/public/${config.jsResource}", blocker, Some(req)).getOrElseF(NotFound())

      case req @ GET -> Root / path if List(".js", ".js.map").exists(path.endsWith) => path match {
        case "main.js" =>
          StaticFile.fromResource(s"/public/${config.jsResource}", blocker, Some(req)).getOrElseF(NotFound())
        case p@s"${config.jsResource}.map" =>
          StaticFile.fromResource(s"/public/$p", blocker, Some(req)).getOrElseF(NotFound())
        case _ => NotFound()
      }

      case req @ GET -> Root / "assets" / path if List(".js", ".css", ".js.map", ".html").exists(path.endsWith) =>
        StaticFile.fromResource(path, blocker, Some(req)).getOrElseF(NotFound())
    }
  }
}
