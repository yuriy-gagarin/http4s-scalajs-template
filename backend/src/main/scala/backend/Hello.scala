package backend

import cats.effect._
import org.http4s._
import org.http4s.implicits._
import org.http4s.dsl.Http4sDsl

import common._

trait Hello[F[_]] {
  def routes: HttpRoutes[F]
}

object Hello {
  def apply[F[_] : Sync] = new Hello[F] with Http4sDsl[F] {

    def routes: HttpRoutes[F] = HttpRoutes.of {
      case GET -> Root => Ok(Common.hello)
    }
  }
}
