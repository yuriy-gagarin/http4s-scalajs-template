package main

import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.Sync
import cats.MonadError

trait Hello[F[_]] {
  def routes: HttpRoutes[F]
}

object Hello {

  def apply[F[_] : Sync] = new Hello[F] with Http4sDsl[F] {
    def routes: HttpRoutes[F] = HttpRoutes.of {
      case GET -> Root / "hello" => Ok(Common.hello)
    }
  }
}