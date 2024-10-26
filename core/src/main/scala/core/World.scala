package core

/** This trait represents the central context in an Entity-Component-System (ECS) framework.
  *
  * A World manages all entities, components, and systems.
  */
trait World:

  /** Retrieves all entities within the world.
    *
    * @return
    *   An iterable collection of [[Entity]] present in the world.
    */
  def entities: Iterable[Entity]

  /** Adds a system to the world.
    *
    * @param system
    *   The [[System]] to add
    * @return
    *   A list of all systems currently registered in the world, including the newly added system.
    */
  def addSystem(system: System): List[System]

  /** Executes an update on all systems in the world.
    */
  def update(): Unit

  /** Creates a new entity with the specified components and adds it to the world.
    *
    * @param components
    *   A variable number of components.
    * @return
    *   The newly created [[Entity]] instance.
    */
  def createEntity(components: Component*): Entity

  /** Adds an existing entity to the world.
    *
    * @param entity
    *   The entity to add to the world.
    * @return
    *   The current World instance with the entity added.
    */
  def addEntity(entity: Entity): World

  /** Removes an entity from the world.
    *
    * @param entity
    *   The entity to remove from the world.
    * @return
    *   The current World instance with the entity removed.
    */
  def removeEntity(entity: Entity): World

  /** Removes all entities from the world.
    *
    * @return
    *   The current World instance with all entities cleared.
    */
  def clearEntities(): World

  /** Adds a component to a specified entity in the world, updating its archetype accordingly.
    *
    * @param entity
    *   The entity to which the component should be added.
    * @param component
    *   The [[Component]] to add.
    * @tparam C
    *   The type of the component, constrained to [[Component]].
    * @return
    *   The current World instance with the component added to the specified entity.
    */
  def addComponent[C <: Component: ComponentTag](entity: Entity, component: C): World

  /** Retrieves a specific component from an entity by type.
    *
    * @tparam C
    *   The type of the component to retrieve, constrained to [[Component]].
    * @param entity
    *   The entity from which the component should be retrieved.
    * @return
    *   An Option containing the component if it exists, otherwise None.
    */
  def getComponent[C <: Component: ComponentTag](entity: Entity): Option[C]

  /** Removes a specific component from an entity by type.
    *
    * @tparam C
    *   The type of the component to remove, constrained to [[Component]].
    * @param entity
    *   The entity from which the component should be removed.
    * @return
    *   The current World instance with the specified component removed from the entity.
    */
  def removeComponent[C <: Component: ComponentTag](entity: Entity): World

  /** Retrieves entities that have exactly the specified set of components.
    *
    * @param componentClasses
    *   A variable number of component tags defining the required components.
    * @return
    *   An iterable collection of [[Entity]] containing exactly the specified components.
    */
  def entitiesWithComponents(componentClasses: ComponentTag[?]*): Iterable[Entity]

  /** Retrieves entities that have at least the specified set of components.
    *
    * @param componentClasses
    *   A variable number of component tags defining the minimum required components.
    * @return
    *   An iterable collection of [[Entity]] instances containing at least the specified components.
    */
  def entitiesWithAtLeastComponents(componentClasses: ComponentTag[?]*): Iterable[Entity]

/**
 * A Factory for [[World]].
 */
object World:
  def apply(): World = new WorldImpl

  private class WorldImpl extends World:
    private var archetypes: Vector[Archetype] = Vector.empty
    private var systems: List[System]         = List.empty

    def entities: Iterable[Entity] =
      archetypes.flatMap(_.entities)

    def addSystem(system: System): List[System] =
      systems :+= system
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
        val newArchetype = Archetype(entity.componentTags)
        newArchetype.add(entity)
        archetypes :+= newArchetype
      this

    private def getArchetype(entity: Entity): Option[Archetype] =
      val componentTags = entity.componentTags
      archetypes.find(_.componentTags == componentTags)

    def removeEntity(entity: Entity): World =
      getArchetype(entity) match
      case Some(archetype) =>
        archetype.remove(entity)
      case None =>
      this

    def clearEntities(): World =
      archetypes.foreach(_.clearEntities())
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
      case None =>
      this

    private def entitiesByFilter(filter: Set[ComponentTag[_]] => Boolean): Iterable[Entity] =
      archetypes
        .filter(archetype => filter(archetype.componentTags))
        .flatMap(_.entities)

    def entitiesWithAtLeastComponents(componentClasses: ComponentTag[?]*): Iterable[Entity] =
      entitiesByFilter(componentClasses.toSet.subsetOf(_))

    def entitiesWithComponents(componentClasses: ComponentTag[?]*): Iterable[Entity] =
      entitiesByFilter(_ == componentClasses.toSet)
