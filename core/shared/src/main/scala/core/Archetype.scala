package core

import scala.collection.mutable

/**
 * Represents an Archetype in an Entity-Component-System (ECS) framework.
 *
 * An Archetype defines a collection of entities sharing the same set of components.
 * By grouping entities based on their component types, archetypes optimize entity management,
 * allowing efficient querying and manipulation of entities with a specific composition.
 */
sealed trait Archetype:

  /**
   * Provides the set of [[ComponentTag]] corresponding to the components associated with this Archetype.
   *
   * @return A set containing the tags of each component within the entity.
   */
  def componentTags: Set[ComponentTag[_]]

  /**
   * The collection of entities contained within this archetype.
   *
   * @return An iterable collection of [[Entity]] instances.
   */
  def entities: Iterable[Entity]

  /**
   * Adds an entity to this archetype if it contains the required component types.
   *
   * @param entity The entity to add.
   * @return This Archetype instance, potentially updated with the added entity.
   */
  def add(entity: Entity): Archetype

  /**
   * Retrieves an entity from this archetype if present.
   *
   * @param entity The entity to retrieve.
   * @return An Option containing the entity if it is present in the archetype, None otherwise.
   */
  def get(entity: Entity): Option[Entity]

  /**
   * Removes an entity from this archetype if present.
   *
   * @param entity The entity to remove.
   * @return This Archetype instance, potentially updated without the specified entity.
   */
  def remove(entity: Entity): Archetype

  /**
   * Removes all entities from this archetype.
   *
   * @return This Archetype instance with all entities cleared.
   */
  def clearEntities(): Archetype

/**
 * A Factory for [[Archetype]].
 */
object Archetype:

  def apply(componentTags: ComponentTag[_]*): Archetype =
    ArchetypeImpl(componentTags.toSet)

  def apply(componentTags: Set[ComponentTag[_]]): Archetype =
    ArchetypeImpl(componentTags)

  private class ArchetypeImpl(tags: Set[ComponentTag[_]]) extends Archetype:
    private val entityContainer: mutable.HashSet[Entity] = mutable.HashSet.empty

    def componentTags: Set[ComponentTag[_]] = tags

    def entities: Iterable[Entity] = entityContainer

    def add(entity: Entity): Archetype =
      if tags == entity.componentTags  then
        entityContainer.add(entity)
      this

    def get(entity: Entity): Option[Entity] =
      Some(entity).filter(entityContainer.contains)

    def remove(entity: Entity): Archetype =
      entityContainer -= entity
      this

    def clearEntities(): Archetype =
      entityContainer.clear()
      this

