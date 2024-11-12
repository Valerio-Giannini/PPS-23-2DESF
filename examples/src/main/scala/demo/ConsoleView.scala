package demo

import core.Entity
import mvc.view.RunningSimulationView

class ConsoleView extends RunningSimulationView:

  override def update(event: String): Unit =
    println(s"ConsoleView.update => $event")

  override def show(): Unit =
    println("Showing simulation prints")

  override def close(): Unit =
    println("Closing simulation prints")

  override def render(entities: Iterable[Entity], statsInfos: List[(String, AnyVal)]): Unit =
    println(entities.size)
    println(statsInfos)
