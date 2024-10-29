package dsl.coreDSL

import core.*

trait OuterWorld:
  def spawnEntity: Entity
  def spawnEntityWith(component: Component, components: Component*): Entity

object OuterWorld:
  def apply(): OuterWorld = new OuterWorldImpl()

  private class OuterWorldImpl extends OuterWorld:
    override def spawnEntity: Entity                             = Entity()
    override def spawnEntityWith(component: Component, components: Component*): Entity = Entity(component+:components*)
