package BouncingBall.controller

import BouncingBall.view.{ParamsViewImpl, SimulationViewImpl}
import mvc.controller.Controller
import mvc.model.{Parameter, Simulation}
import mvc.view.{ParamsView, SimulationView}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.scalajs.js.timers.setTimeout
import scala.util.{Failure, Success}

class SimulationController(simulation: Simulation) extends Controller:
  private var paramView: ParamsView = ParamsViewImpl()
  private var simulationView: SimulationView = _
  private val tickInterval: Int = 18 // ms
  private var currentTick: Int = 0
  private var simulationRunning: Boolean = false

  def start(): Unit =
    simulation.parameters.getRequestedParams match
      case p if p.nonEmpty =>
        paramView.init(p).onComplete {
          case Success(paramValues) =>
            paramView.close()
            simulation.parameters.parsedParam(paramValues.map((id, value) => Parameter(id,value)).toList)
            initializeAndRun()
          case Failure(exception) =>
            println(s"Errore nel caricamento dei parametri: ${exception.getMessage}")
        }
        paramView.show()
      case _ =>
        initializeAndRun()

  private def initializeAndRun(): Unit =
    simulation.init()
    simulationView.show()
    simulationRunning = true
    currentTick = 0
    scheduleNextTick()

  private def scheduleNextTick(): Unit =
    if simulation.runCondition && currentTick < 20000 then
      setTimeout(tickInterval) {
        simulation.tick(currentTick)
        simulationView.update(simulation.entities, simulation.statistics.snapshot)
        currentTick += 1
        scheduleNextTick()
      }
    else
      end()

  def end(): Unit =
    simulationRunning = false
    simulation.endSimulation()
    simulationView.close()
    println("Simulazione terminata")

  def setParamsView(paramsView: ParamsView): Unit =
    this.paramView = paramsView

  def setSimulationView(simulationView: SimulationView): Unit =
    this.simulationView = simulationView
