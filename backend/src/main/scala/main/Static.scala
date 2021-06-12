package main

import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.Sync
import cats.MonadError
import org.http4s.StaticFile
import cats.effect.Blocker
import cats.effect.ContextShift

trait Static[F[_]] {
  def routes: HttpRoutes[F]
}

object Static {
  def apply[F[_] : Sync : ContextShift](blocker: Blocker) = new Static[F] with Http4sDsl[F] {

    def routes: HttpRoutes[F] = HttpRoutes.of {
      case req @ GET -> Root =>
        StaticFile.fromResource("/index.html", blocker, Some(req)).getOrElseF(NotFound())

      case req @ GET -> Root / path if List(".js", ".css", ".map", ".html").exists(path.endsWith) =>
        StaticFile.fromResource("/"+path, blocker, Some(req)).getOrElseF(NotFound())
    }
  }
}