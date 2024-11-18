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
  private val paramView: ParamsView = ParamsViewImpl()
  private val reportView: ReportView = ReportViewImpl()
  private val simulationView: SimulationView = SimulationViewImpl()
  private val tickInterval: Int = 18 // ms

  given Simulation = sim
  
  override def start(): Unit =
    simulation.askedParameters match
      case p if p.nonEmpty =>
        paramView.init(p).onComplete {
          case Success(paramValues) =>
            paramView.close()
            simulation.updateParameters(paramValues)
            initializeAndRun()
          case Failure(exception) =>
        }
        paramView.show()
      case _ =>
        initializeAndRun()

  private def initializeAndRun(): Unit =
    simulationView.show()
    scheduleNextTick(1)

  private def scheduleNextTick(currentTick: Int): Unit =
    simulation.shouldRun match
      case true => setTimeout(tickInterval) {
        simulation.update(currentTick)
        simulationView.update(from(sim.world).allEntities, simulation.stats)
        scheduleNextTick(currentTick+1)
      }
      case _ => end()

  override def end(): Unit =
    simulationView.close()

    simulation.report match
      case r if r.nonEmpty =>
        reportView.init(r)

        reportView.show()
      case _ =>


