package main

import org.scalajs.dom
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure

object Main {
  def main(args: Array[String]): Unit = {
    val server = dom.document.getElementById("server")
    val client = dom.document.getElementById("client")

    client.innerText = Common.hello

    dom.ext.Ajax.get("/hello").onComplete {
      case Success(res) => server.innerText = res.responseText
      case Failure(ex)  => {
        server.innerText = "error"
        server.classList.add("red")
      }
    }
  }
}
