package main

import pureconfig._
import pureconfig.generic.semiauto._

case class AppConfig(host: String, port: Int, fullopt: Boolean) {
  val jsResource =
    if (fullopt) "frontend-opt-bundle.js" else "frontend-fastopt-bundle.js"
}

object AppConfig {
  implicit val reader = ConfigReader.fromCursor[AppConfig] { cur =>
    for {
      root <- cur.asObjectCursor

      hostc    <- root.atKey("host")
      portc    <- root.atKey("port")
      fulloptc <- root.atKey("fullopt")

      host    <- hostc.asString
      port    <- portc.asInt
      fullopt <- fulloptc.asString.map(_ == "true")

    } yield AppConfig(host, port, fullopt)
  }
}
