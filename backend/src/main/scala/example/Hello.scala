package example

import cats.effect._
import org.http4s.client.blaze.BlazeClientBuilder
import scala.concurrent.ExecutionContext.global
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s._

trait IndexService[F[_]] {
  def routes: HttpRoutes[F]
}

object IndexService {
  def create[F[_] : Effect : ContextShift](blocker: Blocker): IndexService[F] = new IndexService[F] with Http4sDsl[F] {
    def routes: HttpRoutes[F] =
      HttpRoutes.of {
        case req @ GET -> Root => StaticFile.fromResource("index.html", blocker, Some(req))
          .getOrElseF(NotFound())
      }  
  }
}

object Backend extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    val server = for {
      blocker <- Blocker[IO]

      val indexService = IndexService.create[IO](blocker)

      server  <- BlazeServerBuilder[IO](global)
        .bindHttp(8888, "0.0.0.0")
        .withHttpApp(indexService.routes.orNotFound)
        .resource
        
    } yield server

    server.use(_ => IO.never).as(ExitCode.Success)
  }
}