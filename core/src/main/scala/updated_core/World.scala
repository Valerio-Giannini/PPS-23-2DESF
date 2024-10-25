package updated_core

trait World:
  def entities: Iterable[Entity]
  def addSystem(system: System): List[System]
  def update(): Unit
  def createEntity(components: Component*): Entity
  def addEntity(entity: Entity): World
  def removeEntity(entity: Entity): World
  def clearEntities(): World
  def addComponent[C <: Component: ComponentTag](entity: Entity, component: C): World
  def getComponent[C <: Component: ComponentTag](entity: Entity): Option[C]
  def removeComponent[C <: Component: ComponentTag](entity: Entity): World
  def entitiesWithComponents(componentClasses: ComponentTag[?]*): Iterable[Entity]
  def entitiesWithAtLeastComponents(componentClasses: ComponentTag[?]*): Iterable[Entity]

class SimpleWorld extends World:
  private var archetypes: Map[Set[ComponentTag[_]], Archetype] = Map.empty
  private var systems: List[System]                            = List.empty

  def entities: Iterable[Entity] =
    archetypes.values.flatMap(_.entities)

  def addSystem(system: System): List[System] =
    systems = systems :+ system
    systems

  def update(): Unit =
    systems.foreach(_.update(this))

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

  def addComponent[C <: Component: ComponentTag](entity: Entity, component: C): World =
    getArchetype(entity) match
    case Some(archetype) =>
      archetype.remove(entity)
      val updatedEntity = entity.add(component)
      addEntity(updatedEntity)
    case None =>
      this

  def getComponent[C <: Component: ComponentTag](entity: Entity): Option[C] =
    getArchetype(entity) match
    case Some(archetype) =>
      archetype.get(entity) match
      case Some(e) => e.get[C]
      case _       => None
    case _ => None

  def removeComponent[C <: Component: ComponentTag](entity: Entity): World =
    getArchetype(entity) match
    case Some(archetype) =>
      archetype.remove(entity)
      val updatedEntity = entity.remove[C]
      addEntity(updatedEntity)
    case _ =>
    this

  private def entitiesByFilter(filter: Set[ComponentTag[_]] => Boolean): Iterable[Entity] =
    archetypes.values
      .filter(archetype => filter(archetype.componentTags))
      .flatMap(_.entities)

  def entitiesWithAtLeastComponents(componentClasses: ComponentTag[_]*): Iterable[Entity] =
    entitiesByFilter(componentClasses.toSet.subsetOf(_))

  def entitiesWithComponents(componentClasses: ComponentTag[_]*): Iterable[Entity] =
    entitiesByFilter(_ == componentClasses.toSet)


