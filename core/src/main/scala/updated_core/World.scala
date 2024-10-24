package updated_core

trait World:
  def entities: Iterable[Entity]
  def createEntity(components: Component*): Entity
  def addEntity(entity: Entity): World
  def removeEntity(entity: Entity): World
  def clearEntities(): World

class SimpleWorld extends World:
  private var archetypes: Map[Set[ComponentTag[_]], Archetype] = Map.empty

  def entities: Iterable[Entity] =
    archetypes.values.flatMap(_.entities)

  def createEntity(components: Component*): Entity =
    val newEntity = Entity(components*)
    addEntity(newEntity)
    newEntity

  def addEntity(entity: Entity): World =
    getArchetype(entity) match
    case Some(archetype) =>
      archetype.add(entity)
    case None =>
      val componentTags = entity.componentTags
      val newArchetype  = Archetype(componentTags)
      newArchetype.add(entity)
      archetypes += (newArchetype.componentTags -> newArchetype)
    this

  private def getArchetype(entity: Entity): Option[Archetype] =
    val componentTags = entity.componentTags
    archetypes.get(componentTags)

  def removeEntity(entity: Entity): World =
    getArchetype(entity) match
    case Some(archetype) =>
      archetype.remove(entity)
    case None =>
    this

  def clearEntities(): World =
    archetypes.values.foreach(_.clearEntities())
    this
