import Dependencies._

ThisBuild / scalaVersion     := "2.13.5"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val backend = (project in file("backend"))
  .settings(
    name := "backend",
    libraryDependencies ++= Seq(scalaTest % Test, cats, catsEffect) ++ http4s,
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.0" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
  )
    
lazy val frontend = (project in file("frontend"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "frontend",
    scalaJSUseMainModuleInitializer := true,
    Compile / fullOptJS / artifactPath := baseDirectory.value / ".." / "static" / "main.js",
    Compile / fastOptJS / artifactPath := baseDirectory.value / ".." / "static" / "main.js"
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
