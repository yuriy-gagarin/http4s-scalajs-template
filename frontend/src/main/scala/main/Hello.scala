package main

import org.scalajs.dom
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import scala.concurrent.ExecutionContext.Implicits.global

object Hello {

  abstract class ServerResponseState extends Product with Serializable {
    def createHtmlElement = this match {
      case Awaiting => <.span("waiting...")
      case Error    => <.span(^.className := "red","error")
      case Complete(m) => <.span(m)
    }
  }
  case object Awaiting extends ServerResponseState
  case object Error extends ServerResponseState
  case class  Complete(message: String) extends ServerResponseState

  case class State(serverResponse: ServerResponseState)

  class Backend($: BackendScope[Unit, State]) {

    def init = getServerHello

    private def getServerHello: AsyncCallback[Unit] = for {
      req <- AsyncCallback.fromFuture(dom.ext.Ajax.get("/hello")) 
      _   <- $.modStateAsync((state: State) => state.copy(serverResponse = Complete(req.responseText)))
    } yield ()


    def render(s: State): VdomElement = {
      <.div(
        <.h1(<.span(Common.hello)),
        <.h1(s.serverResponse.createHtmlElement)
      )      
    }
  }

  val Main = ScalaComponent.builder[Unit]
    .initialState(State(Awaiting))
    .renderBackend[Backend]
    .componentDidMount(_.backend.init.toCallback)
    .build
}