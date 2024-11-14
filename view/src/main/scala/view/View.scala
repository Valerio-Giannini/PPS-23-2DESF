package view

import com.raquo.laminar.api.L.*
import core.Entity
import org.scalajs.dom
import view.init.*
import view.report.*

import scala.concurrent.{Future, Promise}

trait View:
  /** La view è composta in blocchi in base al cosa sta succedendo nella simulazione ?? app= generico per lanciare la
    * sim init= container dedicato al configurazione dei parametri simulation= container dedicato al render della
    * simulazione in corso report= container dedicato ai grafici della reportistica Mostra a schermo il blocco richiesto
    * dall'utente.
    */

  /** mostra a schermo questa view con i relativi dati
    */
  def show(): Unit

  /** chiudi la view attuale
    */
  def close(): Unit

trait ParamsView extends View:
  /** Inizializza i campi di questa view in base ai parametri richiesti
    * @param params
    * @return
    */
  def init(params: Iterable[ViewParameter]): Future[Iterable[(String, AnyVal)]]

trait SimulationView extends View:
  def update(entities: Iterable[Entity], statsInfos: List[(String, AnyVal)]): Unit

trait ReportView extends View:
  def report(infos: List[(String, List[(AnyVal, AnyVal)])]): Unit

class ParamsViewImpl extends ParamsView:
  var parameters: Iterable[ViewParameter]  = _
  var results: Map[String, AnyVal] => Unit = _

  override def show(): Unit =
    val paramsConfig = RenderInit.renderInit(parameters, results)
    // Setta Il container dove visualizzare il render
    val container = dom.document.getElementById("init-container")
    // Renderizza l'elemento di configurazione nel contenitore della simulazione
    render(container, paramsConfig)

  override def close(): Unit =
    val container = dom.document.getElementById("init-container")
    container.innerHTML = ""

  override def init(params: Iterable[ViewParameter]): Future[Iterable[(String, AnyVal)]] =
    parameters = params
    val promise = Promise[Iterable[(String, AnyVal)]]()
    // Funzione `onSave` per completare la promise con i risultati configurati
    val onSave: Map[String, AnyVal] => Unit = resultsParams => promise.success(resultsParams.toList)
    results = onSave

    // Restituisce il Future che verrà completato quando tutti i parametri saranno configurati
    promise.future

object ReportViewImpl extends ReportView:
  var reportInfos: List[(String, List[(AnyVal, AnyVal)])] = _

  override def show(): Unit =
    val container   = dom.document.getElementById("report-container")
    val renderInfos = renderReport(reportInfos)
    render(container, renderInfos)

  override def close(): Unit =
    val container = dom.document.getElementById("report-container")
    container.innerHTML = ""

  def report(infos: List[(String, List[(AnyVal, AnyVal)])]): Unit =
    reportInfos = infos
