package simulation

import core.World
import dsl.DSL.*

import scala.annotation.tailrec

trait Simulation:
  given world: World = newWorld

  def init: Unit =
    setParams
    initWorld

  def setParams: Unit

  def initWorld: Unit

  def condition: Boolean

  def tick(current_tick: Int): Unit =
    println(s"Tick: $current_tick")
    update(world)
    updateReport(current_tick)
    showStats
    println("-------------------")

  def runSimulation(using initial_tick: Int = 0): Unit =
    @tailrec
    def _simulationLoop(current_tick: Int): Unit =
      if condition then
        tick(current_tick)
        _simulationLoop(current_tick+1)
    _simulationLoop(initial_tick)
    showReport

  def showStats: Unit

  def updateReport(current_tick: Int): Unit

  def showReport: Unit


