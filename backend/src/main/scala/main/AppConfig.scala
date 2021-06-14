package main

import pureconfig._
import pureconfig.generic.semiauto._

case class AppConfig(host: String, port: Int, fullopt: Boolean) {
  val jsResource =
    if (fullopt) "frontend-opt-bundle.js" else "frontend-fastopt-bundle.js"
}

object AppConfig {
  implicit val reader = deriveReader[AppConfig]
}
