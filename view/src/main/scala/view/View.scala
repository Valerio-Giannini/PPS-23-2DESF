//package view
//
//import com.raquo.laminar.api.L.*
//import core.Entity
//import mvc.model.ViewParameter
//import org.scalajs.dom
//import view.init.*
//import view.report.*
//
//import scala.concurrent.{Future, Promise}
//
//
//
//class ParamsViewImpl extends ParamsView:
//  var parameters: Iterable[ViewParameter]  = _
//  var results: Map[String, AnyVal] => Unit = _
//
//  override def show(): Unit =
//    val paramsConfig = RenderInit.renderInit(parameters, results)
//    // Setta Il container dove visualizzare il render
//    val container = dom.document.getElementById("init-container")
//    // Renderizza l'elemento di configurazione nel contenitore della simulazione
//    render(container, paramsConfig)
//
//  override def close(): Unit =
//    val container = dom.document.getElementById("init-container")
//    container.innerHTML = ""
//
//  override def init(params: Iterable[ViewParameter]): Future[Iterable[(String, AnyVal)]] =
//    parameters = params
//    val promise = Promise[Iterable[(String, AnyVal)]]()
//    // Funzione `onSave` per completare la promise con i risultati configurati
//    val onSave: Map[String, AnyVal] => Unit = resultsParams => promise.success(resultsParams.toList)
//    results = onSave
//
//    // Restituisce il Future che verrà completato quando tutti i parametri saranno configurati
//    promise.future
//
//object ReportViewImpl extends ReportView:
//  var reportInfos: List[(String, List[(AnyVal, AnyVal)])] = _
//
//  override def show(): Unit =
//    val container   = dom.document.getElementById("report-container")
//    val renderInfos = renderReport(reportInfos)
//    render(container, renderInfos)
//
//  override def close(): Unit =
//    val container = dom.document.getElementById("report-container")
//    container.innerHTML = ""
//
//  def report(infos: List[(String, List[(AnyVal, AnyVal)])]): Unit =
//    reportInfos = infos
