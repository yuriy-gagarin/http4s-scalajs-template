package main

import org.scalajs.dom
import scala.concurrent.ExecutionContext.Implicits.global

object Main {
  def main(args: Array[String]): Unit = {
    val server = dom.document.getElementById("server")
    val client = dom.document.getElementById("client")

    client.innerText = Common.hello

    dom.ext.Ajax.get("/hello").map { req =>
      server.innerText = req.responseText
    }
  }
}
