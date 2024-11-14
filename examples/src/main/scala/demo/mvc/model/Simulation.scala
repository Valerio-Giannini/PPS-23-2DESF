package demo.mvc.model

import core.{Entity, System, World}
import demo.mvc.controller.{Controller, SimulationController}
import dsl.DSL.*
import view.init.ViewParameter

trait Simulation:
  given world: World = newWorld

  private var controller: Controller = _
  private var askedParams: Option[List[ViewParameter]] = None
  private var params: List[(String, AnyVal)]           = List.empty

  def init(): Unit

  def condition: Boolean
  def isRunning: Boolean
  def entities: Iterable[Entity] = world.entities

  def tick(currentTick: Int): Unit =
    world.update() // Aggiorna ogni sistema

  def endSimulation(): Unit

  def include(system: System): Unit =
    world.addSystem(system)

  def askParam(param: ViewParameter): Unit =
    askedParams = askedParams match
      case Some(params) => Some(params :+ param)
      case None         => Some(List(param))

  def parameters: Option[List[ViewParameter]] = this.askedParams

  def setParams(paramsFromView: Iterable[(String, AnyVal)]): Unit =
    params = paramsFromView.toList
