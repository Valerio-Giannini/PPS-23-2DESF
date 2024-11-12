package controller

import com.raquo.laminar.api.L.*
import controller.SimulationState.*
import view.SimulationView
import simulation.{Simulation, Stats}
import view.ParamsView
import view.ViewImpl.*

import scala.annotation.tailrec

enum SimulationState:
  case NOT_STARTED, RUNNING, STOPPED, ENDED

class SimulationController(simulation: Simulation, view: SimulationView):
  import simulation.given

  private var state: SimulationState = SimulationState.NOT_STARTED // dovrebbe andare nel model!

  def start(): Unit =
    simulation.init
    println("Init complete")
    _runSimulation
    _handleEndSimulation()

  private def _runSimulation(using initial_tick: Int = 0): Unit =
    state = SimulationState.RUNNING
    println("State set to RUNNING")

    val entities = world.entities
    println(s"Entities acquired: ${entities}")
    val statsInfos = Stats.statsSnapshot
    println("Stats acquired!")

    view.renderSim(entities, statsInfos)

//    @tailrec
//    def _simulationLoop(current_tick: Int): Unit =
//      if /* simulation.condition &&*/ state == RUNNING then // case match su state
//        //println(s"Tick: $current_tick")
//        simulation.tick(current_tick)
//        _updateView()
//       // println("-------------------")
//        _simulationLoop(current_tick + 1)
//
//    _simulationLoop(initial_tick)

    EventStream.periodic(50)
      .takeWhile(_ => state != STOPPED)
      .foreach { _ =>
        world.update()
        _updateView()
      }(unsafeWindowOwner)

  private def _handleEndSimulation(): Unit =
    state = ENDED
    
    
    //showReport// view

  private def _updateView(): Unit =
    val newEntities = world.entities
    val newStats = Stats.statsSnapshot
    println("New Data acquired!")
    view.renderNext(newEntities, newStats)

  def stop(): Unit =
    state = STOPPED






//import scala.concurrent.{Future, ExecutionContext}
//import scala.util.{Success, Failure}
//
//// Imposta un ExecutionContext. In un'applicazione reale, potresti voler usare un ExecutionContext globale o personalizzato.
//given ExecutionContext = ExecutionContext.global
//
//// Funzione da eseguire su un thread separato
//def myFunction(): Unit = {
//  println(s"Esecuzione su un thread separato: ${Thread.currentThread().getName}")
//}
//
//// Avvia l'esecuzione della funzione su un thread separato
//val futureResult = Future {
//  myFunction()
//}
//
//// Gestisci il completamento del Future
//futureResult.onComplete {
//  case Success(_) => println("Esecuzione completata con successo.")
//  case Failure(exception) => println(s"Si è verificato un errore: ${exception.getMessage}")
//}