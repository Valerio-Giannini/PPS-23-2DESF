package core

import scala.collection.mutable

/** Represents an Archetype in an Entity-Component-System (ECS) framework.
  *
  * An Archetype defines a collection of entities sharing the same set of components.
  */
sealed trait Archetype:

  /** Provides the set of [[ComponentTag]] associated with this Archetype.
    *
    * @return
    *   A Set containing the Component tags.
    */
  def componentTags: Set[ComponentTag[_]]

  /** The entities contained within this archetype.
    *
    * @return
    *   An iterable collection of [[Entity]] instances.
    */
  def entities: Iterable[Entity]

  /** Add an [[Entity]] to this archetype if it contains the required component types.
    *
    * @param entity
    *   The entity to add.
    * @return
    *   `true` if the entity was added, `false` otherwise.
    */
  def add(entity: Entity): Boolean

  /** Retrieves an [[Entity]] from this archetype if present.
    *
    * @param entity
    *   The entity to retrieve.
    * @return
    *   An Option of the entity.
    */
  def get(entityId: Int): Option[Entity]

  /** Removes an entity from this archetype if present.
    *
    * @param entity
    *   The entity to remove.
    * @return
    *   `true` if the entity was removed, `false` otherwise.
    */
  def remove(entity: Entity): Boolean

  /** Removes all entities from this archetype.
    */
  def clearEntities(): Unit

/** A Factory for [[Archetype]].
  */
object Archetype:

  def apply(componentTags: Set[ComponentTag[_]]): Archetype =
    ArchetypeImpl(componentTags)

  private class ArchetypeImpl(val componentTags: Set[ComponentTag[_]]) extends Archetype:
    private val entityContainer = mutable.HashMap.empty[Int, Entity]

    def entities: Iterable[Entity] = entityContainer.values

    def add(entity: Entity): Boolean =
      entity.componentTags == componentTags && entityContainer.put(entity.id, entity).isEmpty

    def get(entityId: Int): Option[Entity] =
      entityContainer get entityId

    def remove(entity: Entity): Boolean =
      entityContainer.remove(entity.id).isDefined

    def clearEntities(): Unit =
      entityContainer.clear()
