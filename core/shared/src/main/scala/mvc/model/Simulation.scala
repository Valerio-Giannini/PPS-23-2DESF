package mvc.model

import core.World
import dsl.DSL.*
import mvc.Observable

trait Simulation extends Observable:
  given world: World = newWorld
  //given report: Report = newReport
  //given param: Param = newParam
  //given stats: Stats = newStats
  

  def init(): Unit
  def condition: Boolean
  def isRunning: Boolean
  def tick(current_tick: Int): Unit =
    update(world)
  def endSimulation(): Unit

  def showStats(): Unit
  def showReport(): Unit
