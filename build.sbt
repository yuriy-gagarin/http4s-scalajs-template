import Dependencies._

ThisBuild / scalaVersion     := "2.13.5"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val common = (crossProject(JSPlatform, JVMPlatform).crossType(CrossType.Pure) in file ("common"))
  .settings(
    name := "common",
    libraryDependencies ++= Dependencies.shared.value
  )

lazy val backend = (project in file("backend"))
  .settings(
    name := "backend",
    libraryDependencies ++= Dependencies.jvm.value ++ Dependencies.shared.value,
    reStart := (reStart dependsOn (frontend / Compile / fastOptJS / webpack)).evaluated,
    Compile / compile := ((Compile / compile) dependsOn (frontend / Compile / fastOptJS / webpack)).value,
    Compile / resources ++= (frontend / Compile / fastOptJS / webpack).value.map(_.data)
  )
  .dependsOn(common.jvm)

lazy val frontend = (project in file("frontend"))
  .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
  .settings(
    name := "frontend",
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Dependencies.js.value ++ Dependencies.shared.value,
    Compile / npmDependencies ++= Seq(
      "react" -> "17.0.2",
      "react-dom" -> "17.0.2"
    )
  )
  .dependsOn(common.js)

Global / onLoad := (Command.process("project backend", _)) compose (Global / onLoad).value
