package main

import pureconfig._
import pureconfig.generic.semiauto._

case class AppConfig(host: String, port: Int, staticPath: String)

object AppConfig {
  implicit val reader = deriveReader[AppConfig]
}