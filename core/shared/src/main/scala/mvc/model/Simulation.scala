package mvc.model

import core.{Entity, System, World}
import dsl.DSL.*
import mvc.controller.Controller


trait Simulation:
  var world: World = newWorld
  val report: Report = Report()
  val statistics: Statistics = Statistics
  val parameters: Parameters = Parameters()
  val condition: Condition = Condition()

  private var controller: Controller = _

  def init(): Unit
  def runCondition: Boolean = condition.evaluate
  def entities: Iterable[Entity] = world.entities
  def tick(currentTick: Int): Unit =
    world.update()
  def endSimulation(): Unit
  def include(system: System): Unit =
    world.addSystem(system)

  def showReport(): Unit
//  def setParameters(parma: Parameters): Unit =
//    parameters = parma