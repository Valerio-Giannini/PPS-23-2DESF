package updated_core

sealed trait Archetype:
  def componentTags: Set[ComponentTag[_]]

  def equalsTo(componentTags: Set[ComponentTag[_]]): Boolean

object Archetype:

  def apply(componentTags: ComponentTag[_]*): Archetype =
    ArchetypeImpl(componentTags.toSet)

  def apply(componentTags: Set[ComponentTag[_]]): Archetype =
    ArchetypeImpl(componentTags)

  private class ArchetypeImpl(tags: Set[ComponentTag[_]]) extends Archetype:

    def componentTags: Set[ComponentTag[_]] = tags

    def equalsTo(componentTags: Set[ComponentTag[_]]): Boolean =
      componentTags == tags
