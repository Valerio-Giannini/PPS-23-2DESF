package simulation

import core.World
import dsl.DSL.*

trait Simulation:
  given world: World = newWorld

  var tick: Int = 1

  def init: Unit =
    setParams
    initWorld
    runSimulation
    showReport


  def setParams: Unit

  def initWorld: Unit

  def condition: Boolean

  def runSimulation: Unit =
    while condition
    do
      println(s"Tick: ${tick}")

      update(world)
      updateReport
      showStats
      println("-------------------")

      tick += 1
  def showStats: Unit

  def updateReport: Unit
  def showReport: Unit


