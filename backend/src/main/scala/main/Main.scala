package example

import cats.effect._
import org.http4s.client.blaze.BlazeClientBuilder
import scala.concurrent.ExecutionContext.global
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s._
import org.http4s.server.staticcontent._

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    val server = for {
      blocker <- Blocker[IO]

      val static = fileService[IO](FileService.Config("../static", blocker))

      server  <- BlazeServerBuilder[IO](global)
        .bindHttp(8888, "0.0.0.0")
        .withHttpApp(static.orNotFound)
        .resource
        
    } yield server

    server.use(_ => IO.never).as(ExitCode.Success)
  }
}