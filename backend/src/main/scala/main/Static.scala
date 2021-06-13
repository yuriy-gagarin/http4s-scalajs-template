package main

import org.http4s._
import org.http4s.dsl.Http4sDsl
import cats.effect._

trait Static[F[_]] {
  def routes: HttpRoutes[F]
}

object Static {
  def apply[F[_] : Sync : ContextShift](jsResource: String, blocker: Blocker) = new Static[F] with Http4sDsl[F] {

    def routes: HttpRoutes[F] = HttpRoutes.of {
      case req @ GET -> Root =>
        StaticFile.fromResource("/index.html", blocker, Some(req)).getOrElseF(NotFound())

      case req @ GET -> Root / "main.js" =>
        StaticFile.fromResource(s"/public/$jsResource", blocker, Some(req)).getOrElseF(NotFound())

      case req @ GET -> Root / "assets" / path if List(".js", ".css", ".map", ".html").exists(path.endsWith) =>
        StaticFile.fromResource(path, blocker, Some(req)).getOrElseF(NotFound())
    }
  }
}