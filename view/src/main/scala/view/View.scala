package view

import init.*
import report.*
import core.Entity
import scala.concurrent.{Future, Promise}
import com.raquo.laminar.api.L.*
import org.scalajs.dom

trait View:
  def show(container: org.scalajs.dom.Element, content: Div): Unit
  def close(container: org.scalajs.dom.Element): Unit

trait ParamsView:
  def init(params: Iterable[ViewParameter]): Future[Iterable[(String, AnyVal)]]

trait ReportView:
  def stats(infos: List[(String, AnyVal)]): Div
  def report(infos: List[(String, List[(AnyVal, AnyVal)])]): Unit


object ViewImpl extends View:
  def show(container: org.scalajs.dom.Element, content: Div): Unit =
    render(container, content) // Renderizza il contenuto nel container specificato

  def close(container: org.scalajs.dom.Element): Unit =
    container.innerHTML = "" // Rimuove il contenuto dal container


object ParamsViewImpl extends ParamsView:

  override def init(params: Iterable[ViewParameter]): Future[Iterable[(String, AnyVal)]] =
    val promise = Promise[Iterable[(String, AnyVal)]]()

    // Funzione `onSave` per completare la promise con i risultati configurati
    val onSave: Map[String, AnyVal] => Unit = results => 
      promise.success(results.toList)
    
    // Chiama `renderParametersConfig` con `paramsList` e `onSave`
    val paramsConfig = RenderInit.renderInit(params, onSave)
    
    //Setta Il container dove visualizzare il render
    val container = dom.document.getElementById("init-container")
    // Renderizza l'elemento di configurazione nel contenitore della simulazione
    ViewImpl.show(container, paramsConfig)
    
    // Restituisce il Future che verrà completato quando tutti i parametri saranno configurati
    promise.future

object ReportViewImpl extends ReportView:

  def stats(infos: List[(String, AnyVal)]): Div =
    div(
      position := "absolute",
      bottom := "0px", // Posiziona il riquadro in basso
      right := "0px", // Posiziona il riquadro a destra
      width := "200px",
      padding := "10px",
      backgroundColor := "rgba(0, 0, 0, 0.7)", // Sfondo semi-trasparente per leggibilità
      color := "white", // Colore del testo per contrastare lo sfondo
      borderRadius := "8px",
      border := "1px solid #ccc",
      fontSize := "12px",
      overflowY := "auto", // Permette lo scrolling verticale se necessario
      children <-- Val(
        infos.map { case (key, value) =>
          div(
            s"$key = $value",
            marginBottom := "5px" // Spaziatura tra le righe
          )
        }
      )
    )

  def report(infos: List[(String, List[(AnyVal, AnyVal)])]): Unit =
    val container = dom.document.getElementById("report-container")
    val renderInfos = renderReport(infos)
    ViewImpl.show(container, renderInfos)

