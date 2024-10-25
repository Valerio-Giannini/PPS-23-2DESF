package updated_core

import scala.collection.mutable

sealed trait Archetype:
  def componentTags: Set[ComponentTag[_]]
  def entities: Iterable[Entity]
  def add(entity: Entity): Archetype
  def get(entity: Entity): Option[Entity]
  def remove(entity: Entity): Archetype
  def clearEntities(): Archetype

object Archetype:

  def apply(componentTags: ComponentTag[?]*): Archetype =
    ArchetypeImpl(componentTags.toSet)

  def apply(componentTags: Set[ComponentTag[_]]): Archetype =
    ArchetypeImpl(componentTags)

  private class ArchetypeImpl(tags: Set[ComponentTag[_]]) extends Archetype:
    private val entityContainer: mutable.ArrayBuffer[Entity] = mutable.ArrayBuffer.empty

    def componentTags: Set[ComponentTag[_]] = tags

    def entities: Iterable[Entity] = entityContainer

    def add(entity: Entity): Archetype =
      if tags == entity.componentTags && !entityContainer.contains(entity) 
      then entityContainer += entity
      this

    def get(entity: Entity): Option[Entity] =
      entityContainer.find(_.equals(entity))

    def remove(entity: Entity): Archetype =
      get(entity) match
      case Some(e) => entityContainer -= entity
      case _ => 
      this

    def clearEntities(): Archetype =
      entityContainer.clear()
      this
