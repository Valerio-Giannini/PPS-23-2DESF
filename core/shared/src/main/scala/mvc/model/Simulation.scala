package mvc.model

import core.World
import dsl.DSL.*
import mvc.controller.Controller


trait Simulation:
  val world: World = newWorld
  val report: Report = Report()
  val statistics: Statistic = Statistic()
  val parameters: Parameters = Parameters()
  val runCondition: Condition = Condition()

  private var controller: Controller = _

  def tick(currentTick: Int): Unit = {
    update(world)
  }