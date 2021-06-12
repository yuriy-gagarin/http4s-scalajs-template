package main

import org.scalajs.dom
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object Main {
  def main(args: Array[String]): Unit = {
    Hello.Main().renderIntoDOM(dom.document.getElementById("root"))
  }
}
