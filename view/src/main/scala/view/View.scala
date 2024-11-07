package view

import init.*
import core.Entity
import scala.concurrent.{Future, Promise}
import com.raquo.laminar.api.L.*
import org.scalajs.dom

trait View:
  def show(container: org.scalajs.dom.Element, content: Div): Unit
  def close(container: org.scalajs.dom.Element): Unit

trait ParamsView:
  def init(params: Iterable[ViewParameter]): Future[Iterable[(String, AnyVal)]]


//trait ReportView extends View:
//  def render(Iterable[Plot]): Unit

object ViewImpl extends View:
  def show(container: org.scalajs.dom.Element, content: Div): Unit =
    render(container, content) // Renderizza il contenuto nel container specificato

  def close(container: org.scalajs.dom.Element): Unit =
    container.innerHTML = "" // Rimuove il contenuto dal container


object ParamsViewImpl extends ParamsView:

  override def init(params: Iterable[ViewParameter]): Future[Iterable[(String, AnyVal)]] =
    val promise = Promise[Iterable[(String, AnyVal)]]()

    // Funzione `onSave` per completare la promise con i risultati configurati
    val onSave: Map[String, AnyVal] => Unit = results => {
      promise.success(results.toList)
    }

    // Chiama `renderParametersConfig` con `paramsList` e `onSave`
    val paramsConfig = RenderInit.renderInit(params, onSave)
    //Setta Il container dove visualizzare il render
    val container = dom.document.getElementById("simulation-container")
    // Renderizza l'elemento di configurazione nel contenitore della simulazione
    ViewImpl.show(container, paramsConfig)

    // Restituisce il Future che verrà completato quando tutti i parametri saranno configurati
    promise.future



