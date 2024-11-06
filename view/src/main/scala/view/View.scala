package view

import core.Entity

import scala.concurrent.Future

trait View:
  def show(): Unit
  def close(): Unit

trait SimulationView extends View:
  def render(entities: Iterable[Entity], Iterable[Stat]): Unit

trait ParamsView extends View:
  def init(Iterable[ViewParameter]): Future[Iterable[String, Anyval]]

trait ReportView extends View:
  def render(Iterable[Plot]): Unit


object SimulationViewImpl extends SimulationView:

  override def render(entities: Iterable[Entity], Iterable[Stat]): Unit = ???

  override def renderPlot(Iterable: Nothing): Unit = ???

  private def renderWorld(Iterable [Entity]): Unit

  private def renderStats(Iterable [Stat]): Unit // le cose che stanno in show


