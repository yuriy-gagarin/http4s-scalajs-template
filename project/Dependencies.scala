import sbt._
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.8"

  val http4sVersion = "0.21.22"
  lazy val http4s = Seq(
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "org.http4s" %% "http4s-blaze-server" % http4sVersion,
    "org.http4s" %% "http4s-blaze-client" % http4sVersion
  )

  lazy val cats =
    "org.typelevel" %% "cats-core" % "2.3.0"

  lazy val catsEffect =
    "org.typelevel" %% "cats-effect" % "2.4.1"

  lazy val pureconfig =
    "com.github.pureconfig" %% "pureconfig" % "0.16.0"
}
