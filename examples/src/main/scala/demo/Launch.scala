package demo

import mvc.controller.SimulationController
import mvc.view.SimulationViewImpl
import view.init.ViewParameter

object Launch:

  def main(args: Array[String]): Unit =
    val simulation = MySim()
    val controller = SimulationController(simulation)
    controller.setSimulationView(SimulationViewImpl())
    controller.start()

//    controller

/*  Utente
given simulation: Simulation = MySim()    //  val simulation = MySim()

// Aggiunta dei system
simulation.include(mySystem1())
simulation.include(mySystem2())
simulation.include(mySystem3())

// aggiunta dei paramteri della simulazione
simulation.askParam("label").initialValue(0).range(0,20)

// report
simulation.defineLinearGraph("label", data)

// statistics
simulation.defineStat("label", function)

// aggiunta delle entità
simulation.spawnNewEntityWith(Position(1,1), Speed(1,1))
simulation.spawnNewEntityWith(Position(2,2), Speed(1,1))

simulation.setCOntroller
simulati.setView

// simulation start
simulation.start


 */
