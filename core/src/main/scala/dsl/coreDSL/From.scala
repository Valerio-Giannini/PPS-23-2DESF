package dsl.coreDSL

import core.*

trait From:
  def allEntities: Iterable[Entity]

  def entity(entity: Entity): Option[Entity]

  def numberOfEntities: Int

  def kill(entity: Entity): World

  def componentsOf(entity: Entity): FromComponentBuilder

  def entitiesHavingOnly(componentTag: ComponentTag[?], componentTags: ComponentTag[?]*): Iterable[Entity]

  def entitiesHaving(componentTag: ComponentTag[?], componentTags: ComponentTag[?]*): Iterable[Entity]

object From:
  def apply(world: World): From = new FromImpl(world)

  private class FromImpl(world: World) extends From:
    override def allEntities: Iterable[Entity]                     = world.entities.toSeq.sortBy(_.id)
    override def entity(entity: Entity): Option[Entity]            = world.entity(entity)
    override def numberOfEntities: Int                             = world.entities.size
    override def kill(entity: Entity): World                       = world.removeEntity(entity)
    override def componentsOf(entity: Entity): FromComponentBuilder = FromComponentBuilder(world, entity)

    override def entitiesHavingOnly(componentTag: ComponentTag[?], componentTags: ComponentTag[?]*): Iterable[Entity] =
      world.entitiesWithComponents(componentTag +: componentTags*).toSeq.sortBy(_.id)

    override def entitiesHaving(componentTag: ComponentTag[?], componentTags: ComponentTag[?]*): Iterable[Entity] =
      world.entitiesWithAtLeastComponents(componentTag +: componentTags*).toSeq.sortBy(_.id)

trait FromComponentBuilder:
  def get[C <: Component: ComponentTag]: Option[C]
  def remove[C <: Component: ComponentTag]: Entity

object FromComponentBuilder:
  def apply(world: World, entity: Entity): FromComponentBuilder = FromComponentOfImpl(world, entity)

  private class FromComponentOfImpl(world: World, entity: Entity) extends FromComponentBuilder:
    override def get[C <: Component: ComponentTag]: Option[C] = world.getComponent[C](entity)

    override def remove[C <: Component: ComponentTag]: Entity =
      world.removeComponent[C](entity)
      world.entity(entity).get
