package controller

import controller.SimulationState.ENDED
import renderSim.SimulationView
import simulation.Simulation

import scala.annotation.tailrec

enum SimulationState:
  case NOT_STARTED, RUNNING, STOPPED, ENDED

class SimulationController(simulation: Simulation, view: SimulationView):
  private var state: SimulationState = SimulationState.NOT_STARTED // dovrebbe andare nel model!

  def start():Unit =
    simulation.init
    _runSimulation
    _handleEndSimulation

  private def _runSimulation(using initial_tick: Int = 0): Unit =
    state = RUNNING
    @tailrec
    def _simulationLoop(current_tick: Int): Unit =
      if simulation.condition && state != STOPPED then
        println(s"Tick: $current_tick")
        simulation.tick(current_tick)
        _updateView
        println("-------------------")
        _simulationLoop(current_tick + 1)

    _simulationLoop(initial_tick)


  private def _handleEndSimulation =
    state = ENDED
    showReport// view

  private def _updateView() =
    view.renderSim(simulation.entities)
    view.renderStat(simulation.)

  def stop(): Unit = ???
