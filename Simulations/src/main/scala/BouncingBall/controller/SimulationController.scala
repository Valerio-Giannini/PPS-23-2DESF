package BouncingBall.controller

import BouncingBall.view.{ParamsViewImpl, ReportViewImpl, SimulationViewImpl}
import mvc.controller.Controller
import mvc.model.{Parameter, Simulation}
import mvc.view.{ParamsView, ReportView, SimulationView}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.timers.setTimeout
import scala.util.{Failure, Success}

import dsl.DSL.*

class SimulationController(sim: Simulation) extends Controller:
  private var paramView: ParamsView = ParamsViewImpl()
  private val reportView: ReportView = ReportViewImpl()
  private var simulationView: SimulationView = SimulationViewImpl()
  private val tickInterval: Int = 18 // ms
  private var currentTick: Int = 0

  given Simulation = sim
  
  def start(): Unit =
    simulation.askedParameters match
      case p if p.nonEmpty =>
        paramView.init(p).onComplete {
          case Success(paramValues) =>
            paramView.close()
            simulation.updateParameters(paramValues)
            initializeAndRun()
          case Failure(exception) =>
            println(s"Errore nel caricamento dei parametri: ${exception.getMessage}")
        }
        paramView.show()
      case _ =>
        initializeAndRun()

  private def initializeAndRun(): Unit =
    simulationView.show()
    currentTick = 1
    scheduleNextTick()

  private def scheduleNextTick(): Unit =
    if simulation.shouldRun then
      setTimeout(tickInterval) {
        simulation.update(currentTick)
        simulationView.update(from(sim.world).allEntities, simulation.stats)
        currentTick += 1
        scheduleNextTick()
      }
    else
      end()

  def end(): Unit =
    simulationView.close()

    simulation.report match
      case r if r.nonEmpty =>
        reportView.init(r)

        reportView.show()
      case _ =>


