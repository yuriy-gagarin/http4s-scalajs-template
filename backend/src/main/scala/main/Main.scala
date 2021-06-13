package main

import cats.effect._
import cats.syntax.semigroupk._
import org.http4s._
import org.http4s.implicits._
import org.http4s.server.staticcontent._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.Router
import pureconfig._
import scala.concurrent.ExecutionContext.global

object Main extends IOApp.Simple {
  def run: IO[Unit] = {
    val config = ConfigSource.default.loadOrThrow[AppConfig]

    val server = for {
      blocker <- Blocker[IO]

      val static = Static[IO](config.jsResource, blocker).routes
      val hello = Hello[IO].routes

      val service = Router("/hello" -> hello, "/" -> static).orNotFound

      server  <- BlazeServerBuilder[IO](global)
        .bindHttp(config.port, config.host)
        .withHttpApp(service)
        .resource
        
    } yield server

    server.use(_ => IO.never)
  }
}