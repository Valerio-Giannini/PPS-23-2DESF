package mvc.controller

import mvc.model.Simulation

import mvc.view.RunningSimulationView

trait Controller:
  def start(): Unit
  def end(): Unit

class SimulationController(simulation: Simulation, view: RunningSimulationView) extends Controller:

  def start(): Unit =
    simulation.addObserver(view)
    simulation.init()
    runSimulationLoop()

  private def runSimulationLoop(): Unit =
    var currentTick = 0
    while simulation.condition && currentTick < 50 do
      simulation.tick(currentTick)
      currentTick += 1
    end()

  def end(): Unit =
    simulation.endSimulation()
    //view.displayReport(simulation.showReport()) // Mostra il report finale
