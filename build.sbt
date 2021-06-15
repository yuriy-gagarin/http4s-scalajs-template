import Dependencies._

ThisBuild / scalaVersion     := "2.13.5"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.github.yuriygagarin"
ThisBuild / organizationName := "yuriygagarin"

lazy val common = (crossProject(JSPlatform, JVMPlatform).crossType(CrossType.Pure) in file ("common"))
  .settings(
    name := "common",
    libraryDependencies ++= Dependencies.shared.value
  )

lazy val backend = (project in file("backend"))
  .enablePlugins(WebScalaJSBundlerPlugin, Http4sWebPlugin, JavaAppPackaging)
  .settings(
    name := "backend",
    libraryDependencies ++= Dependencies.jvm.value ++ Dependencies.shared.value,
    scalaJSProjects := Seq(frontend),
    Assets / pipelineStages := Seq(scalaJSPipeline)
  )
  .dependsOn(common.jvm)

lazy val frontend = (project in file("frontend"))
  .enablePlugins(ScalaJSBundlerPlugin)
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

Global / scalaJSStage := {
  if (sys.env.get("SCALAJS_FULLOPT").fold(false)(_ == "true")) FullOptStage else FastOptStage
}
