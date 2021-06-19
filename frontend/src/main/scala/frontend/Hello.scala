package frontend

import org.scalajs.dom
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure

import common.Common
import japgolly.scalajs.react.extra.Ajax

object Hello {

  sealed abstract class ServerResponseState extends Product with Serializable {
    def createHtmlElement = this match {
      case Awaiting => <.span("waiting...")
      case Error    => <.span(^.className := "red","error")
      case Complete(m) => <.span(m)
    }
  }
  case object Awaiting extends ServerResponseState
  case object Error extends ServerResponseState
  case class  Complete(message: String) extends ServerResponseState

  case class State(serverResponse: ServerResponseState, formText: String, serverEchoResponse: ServerResponseState)

  class Backend($: BackendScope[Unit, State]) {

    def init = getServerHello

    private def getServerHello: AsyncCallback[Unit] =
      AsyncCallback.fromFuture(dom.ext.Ajax.get("/hello")).flatMap { req =>
        $.modStateAsync(_.copy(serverResponse = Complete(req.responseText)))
      }.handleError { e =>
        $.modStateAsync(_.copy(serverResponse = Error))
      }

    private def handleSumbit(data: String)(e: ReactEventFromInput): Callback = {
      e.preventDefaultCB.asAsyncCallback >>
      AsyncCallback.fromFuture(dom.ext.Ajax.post("/hello", data = data)).flatMap { req =>
        $.modStateAsync(_.copy(serverEchoResponse = Complete(req.responseText)))
      }.handleError { e =>
        $.modStateAsync(_.copy(serverEchoResponse = Error))
      }
    }.toCallback

    private def onChange(e: ReactEventFromInput): Callback = {
      val newValue = e.target.value
      $.modState(_.copy(formText = newValue))
    }

    def render(s: State): VdomElement = {
      <.div(
        <.h1(<.span(Common.hello)),
        <.h1(s.serverResponse.createHtmlElement),
        <.form(
          ^.onSubmit ==> handleSumbit(s.formText),
          <.input(
            ^.tpe := "text",
            ^.onChange ==> onChange,
            ^.value := s.formText),
          <.button("Post")
        ),
        <.h1(s.serverEchoResponse.createHtmlElement)
      )      
    }
  }

  val Main = ScalaComponent.builder[Unit]
    .initialState(State(Awaiting, "", Awaiting))
    .renderBackend[Backend]
    .componentDidMount(_.backend.init.toCallback)
    .build
}