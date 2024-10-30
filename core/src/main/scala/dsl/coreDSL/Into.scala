package dsl.coreDSL

import core.*

trait Into:
  def spawnNewEntity: Entity
  def spawnNewEntityWith(component: Component, components: Component*): Entity
  def spawnEntity(entity: Entity): Entity
  def componentsOf(entity: Entity): IntoComponentBuilder
  def include(system: System): Unit

object Into:
  def apply(world: World): Into = IntoImpl(world)

  private class IntoImpl(world: World) extends Into:
    override def spawnNewEntity: Entity = world.createEntity()

    override def spawnNewEntityWith(component: Component, components: Component*): Entity =
      world.createEntity(component +: components*)

    override def spawnEntity(entity: Entity): Entity =
      world.addEntity(entity)
      world.entity(entity).get

    override def componentsOf(entity: Entity): IntoComponentBuilder = IntoComponentBuilder(world, entity)

    override def include(system: System): Unit = world.addSystem(system)

trait IntoComponentBuilder:
  def add[C <: Component: ComponentTag](component: C): Entity

object IntoComponentBuilder:
  def apply(world: World, entity: Entity): IntoComponentBuilder = IntoComponentBuilderImpl(world, entity)

  private class IntoComponentBuilderImpl(world: World, entity: Entity) extends IntoComponentBuilder:

    override def add[C <: Component: ComponentTag](component: C): Entity =
      world.addComponent[C](entity, component)
      world.entity(entity).get
