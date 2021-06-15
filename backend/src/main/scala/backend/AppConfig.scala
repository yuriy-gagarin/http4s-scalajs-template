package backend

case class AppConfig(host: String, port: Int, fullopt: Boolean) {
  val jsResource =
    if (fullopt) "frontend-opt-bundle.js" else "frontend-fastopt-bundle.js"
}

object AppConfig {
  def fromEnvironment: AppConfig = {
    val fullopt = sys.env.get("SCALAJS_FULLOPT").fold(false)(_ == "true")
    val host    = sys.env.get("SCALAJS_HOST").getOrElse("0.0.0.0")
    val port    = sys.env.get("SCALAJS_PORT").flatMap(_.toIntOption).getOrElse(3333)

    AppConfig(host, port, fullopt)
  }
}
