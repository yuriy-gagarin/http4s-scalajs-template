package main

import cats.effect._
import org.http4s.client.blaze.BlazeClientBuilder
import scala.concurrent.ExecutionContext.global
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s._
import org.http4s.server.staticcontent._
import pureconfig._

object Main extends IOApp.Simple {
  def run: IO[Unit] = {
    val config = ConfigSource.default.loadOrThrow[AppConfig]

    val server = for {
      blocker <- Blocker[IO]

      val static = fileService[IO](FileService.Config(config.staticPath, blocker))

      val service = (static).orNotFound

      server  <- BlazeServerBuilder[IO](global)
        .bindHttp(config.port, config.host)
        .withHttpApp(service)
        .resource
        
    } yield server

    server.use(_ => IO.never)
  }
}