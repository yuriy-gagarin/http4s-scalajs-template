package main

import pureconfig._
import pureconfig.generic.semiauto._

case class AppConfig(host: String, port: Int, jsResource: String)

object AppConfig {
  implicit val reader = deriveReader[AppConfig]
}