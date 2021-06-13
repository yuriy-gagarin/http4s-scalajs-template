import sbt._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._

object Dependencies {

  val http4sVersion = "0.21.22"

  val shared = Def.setting(Seq(
    "org.typelevel" %% "cats-core" % "2.3.0",
    compilerPlugin("org.typelevel" %% "kind-projector" % "0.13.0" cross CrossVersion.full),
    compilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
  ))

  val jvm = Def.setting(Seq(
    "org.scalatest" %% "scalatest" % "3.2.8",
    "org.http4s"    %% "http4s-dsl" % http4sVersion,
    "org.http4s"    %% "http4s-blaze-server" % http4sVersion,
    "org.http4s"    %% "http4s-blaze-client" % http4sVersion,
    "org.typelevel" %% "cats-effect" % "2.4.1",
    "com.github.pureconfig" %% "pureconfig" % "0.16.0",
    "ch.qos.logback" % "logback-classic" % "1.2.3"
  ))

  val js = Def.setting(Seq(
    "org.scala-js" %%% "scalajs-dom" % "1.1.0",
    "com.github.japgolly.scalajs-react" %%% "core" % "1.7.7",
    "com.github.japgolly.scalajs-react" %%% "extra" % "1.7.7"
  ))

}
