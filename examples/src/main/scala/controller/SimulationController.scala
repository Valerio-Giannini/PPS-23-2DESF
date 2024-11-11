package controller

import controller.SimulationState.*
import renderSim.SimulationView
import simulation.{Simulation, Stats}
import view.ParamsView

import scala.annotation.tailrec

enum SimulationState:
  case NOT_STARTED, RUNNING, STOPPED, ENDED

class SimulationController(simulation: Simulation, view: SimulationView):
  import simulation.given

  private var state: SimulationState = SimulationState.NOT_STARTED // dovrebbe andare nel model!

  def start(): Unit =
    simulation.init
    _runSimulation
    _handleEndSimulation()

  private def _runSimulation(using initial_tick: Int = 0): Unit =
    state = SimulationState.RUNNING
    @tailrec
    def _simulationLoop(current_tick: Int): Unit =
      if /* simulation.condition &&*/ state != SimulationState.STOPPED then // case match su state
        //println(s"Tick: $current_tick")
        simulation.tick(current_tick)
        _updateView()
       // println("-------------------")
        _simulationLoop(current_tick + 1)

    _simulationLoop(initial_tick)

  private def _handleEndSimulation(): Unit =
    state = ENDED
    //showReport// view


  private def _updateView(): Unit =
    view.renderSim(simulation.entities, Stats.statsSnapshot)

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