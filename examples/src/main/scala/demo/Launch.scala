package demo

import mvc.controller.SimulationController

object Launch:

  def main(args: Array[String]): Unit =
    val simulation = MySim()
    val view       = ConsoleView()
    val controller = SimulationController(simulation, view)

    controller.start()
