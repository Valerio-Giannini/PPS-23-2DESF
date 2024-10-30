package dsl.coreDSL

import core.*

/**
 * The `OuterWorld` trait provides methods to create new entities, optionally with components, in an outer-world context.
 *
 * Operators:
 *
 * Spawn a new entity without components
 * {{{
 * outerWorld.spawnEntity
 * }}}
 *
 * Spawn a new entity with specified components
 * {{{
 * outerWorld.spawnEntityWith(componentA, componentB)
 * }}}
 */
trait OuterWorld:
  /**
   * Spawns a new entity in the outer world without any components.
   *
   * @return the newly created [[Entity]] instance
   */
  def spawnEntity: Entity

  /**
   * Spawns a new entity in the outer world with specified components.
   *
   * @param component  the first component to add to the entity
   * @param components additional components to add to the entity
   * @return the newly created [[Entity]] instance with the specified components
   */
  def spawnEntityWith(component: Component, components: Component*): Entity

object OuterWorld:
  def apply(): OuterWorld = new OuterWorldImpl()

  private class OuterWorldImpl extends OuterWorld:
    override def spawnEntity: Entity                             = Entity()
    override def spawnEntityWith(component: Component, components: Component*): Entity = Entity(component+:components*)
