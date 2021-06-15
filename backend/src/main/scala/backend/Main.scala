package backend

import cats.effect._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import scala.concurrent.ExecutionContext.global

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    val config = AppConfig.fromEnvironment

    val server = for {
      blocker <- Blocker[IO]
      static = Static[IO](blocker, config).routes
      api    = Hello[IO].routes

      server <- BlazeServerBuilder[IO](global)
        .bindHttp(3333, "0.0.0.0")
        .withHttpApp(Router("/" -> static, "hello" -> api).orNotFound)
        .resource

    } yield server


    server.use(_ => IO.never).as(ExitCode.Success)
  }
}
