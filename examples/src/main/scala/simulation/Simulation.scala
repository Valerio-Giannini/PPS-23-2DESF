package simulation

import core.{Entity, World}
import dsl.DSL.*

import scala.annotation.tailrec

trait Simulation:
  given world: World = newWorld

  def init: Unit =
    setParams
    initWorld

  def setParams: Unit
  
  def entities: Iterable[Entity] = entities

  def initWorld: Unit

  def condition: Boolean

  def tick(current_tick: Int): Unit =
    update(world)
    updateReport(current_tick)

  def showStats: Unit

  def updateReport(current_tick: Int): Unit

  def showReport: Unit


