package updated_core

trait World:
  def entities: Iterable[Entity]
  
class SimpleWorld extends World:
  private var archetypes: Map[Set[ComponentTag[_]], Archetype] = Map.empty
  
  def entities: Iterable[Entity] =
    archetypes.values.flatMap(_.entities)
