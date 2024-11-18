package mvc.model

import core.World
import dsl.DSL.*
import mvc.controller.Controller

/**
 * Trait representing a simulation.
 * Manages the core components of a simulation, including the world, report, statistics, parameters, and run condition.
 * Provides functionality for updating the simulation state at each tick.
 */
trait Simulation:
  val world: World = newWorld
  val report: Report = Report()
  val statistics: Statistic = Statistic()
  val parameters: Parameters = Parameters()
  val runCondition: Condition = Condition()

  private var controller: Controller = _

  /**
   * Updates the simulation state at a given tick.
   *
   * @param currentTick the current simulation tick (iteration) number.
   */
  def tick(currentTick: Int): Unit = {
    update(world)
  }