package demo.mvc.controller

import demo.mvc.model.Simulation
import demo.mvc.view.SimulationViewImpl

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import scala.scalajs.js.timers.*
import scala.util.{Failure, Success}
import view.{ParamsView, ParamsViewImpl}
import com.raquo.laminar.api.L.*

trait Controller:
  def start(): Unit
  def end(): Unit
  def setParamsView(paramsView: ParamsView): Unit
  def setSimulationView(simulationView: SimulationView): Unit


class SimulationController(simulation: Simulation) extends Controller:
  private var paramView: ParamsView                 = ParamsViewImpl()
  private var simulationView: SimulationView    = _
  private val tickInterval: Int                     = 18 // ms
  private var currentTick: Int                      = 0
  private var simulationRunning: Boolean            = false

  def start(): Unit =
    simulation.parameters match
      case Some(params) =>
        paramView.init(params).onComplete {
          case Success(paramValues) =>
            paramView.close()
            simulation.setParams(paramValues)
            initializeAndRun()
          case Failure(exception) =>
            println(s"Errore nel caricamento dei parametri: ${exception.getMessage}")
        }
        paramView.show()
      case None =>
        initializeAndRun()

  private def initializeAndRun(): Unit =
    simulation.init()
    simulationView.show()
    simulationRunning = true
    currentTick = 0
    scheduleNextTick()

  private def scheduleNextTick(): Unit =
    if simulationRunning && simulation.condition && currentTick < 200 then
      setTimeout(tickInterval) {
        simulation.tick(currentTick)
        simulationView.update(simulation.entities, List.empty)
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

  def setSimulationView(simulationView: SimulationViewImpl): Unit =
    this.simulationView = simulationView
