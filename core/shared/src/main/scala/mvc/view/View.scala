package mvc.view

import core.Entity
import mvc.Observer

import scala.concurrent.Future

trait View extends Observer:
  def show(): Unit
  def close(): Unit

trait ParamsView extends View:
  def init(params: Iterable[ViewParameter]): Future[Iterable[(String, AnyVal)]]

trait RunningSimulationView extends View:
  def render(entities: Iterable[Entity], statsInfos: List[(String, AnyVal)]): Unit

trait ReportView:
  def report(infos: List[(String, List[(AnyVal, AnyVal)])]): Unit

case class ViewParameter( // maybe, use a trait hierarchies?
    label: String,
    value: AnyVal,
    minValue: Option[AnyVal] = None,
    maxValue: Option[AnyVal] = None
)
