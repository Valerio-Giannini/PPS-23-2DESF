package core

/** This trait represents the core of the ECS (Entity-Component-System) architecture.
  *
  * A [[World]] manage entities, components, and systems.
  */
trait World extends WorldBase:
  /** Create a new [[Entity]] with the given components.
    *
    * @param components
    *   the [[Component]] instances to associate with the new entity.
    * @return
    *   the created entity.
    */
  def createEntity(components: Component*): Entity

  /** Add an existing [[Entity]] to the world, associating the provided components with it.
    *
    * @param entity
    *   the entity to add.
    * @param components
    *   the [[Component]] instances to associate with the entity.
    */
  def addEntity(entity: Entity, components: Component*): Unit

  /** Remove the specified [[Entity]] from the world.
    *
    * @param entity
    *   the entity to remove.
    */
  def removeEntity(entity: Entity): Unit

  /** Remove all entities from the world.
    */
  def clearFromEntities(): Unit

  /** Get all entities currently present in the world.
    *
    * @return
    *   a list of all entities.
    */
  def getEntities: List[Entity]

  /** Add a [[Component]] to the specified [[Entity]].
    *
    * @param entity
    *   the entity to add the component to.
    * @param component
    *   the component to add.
    */
  def addComponent(entity: Entity, component: Component): Unit

  /** Retrieve a specific [[Component]] from the given [[Entity]].
    *
    * @param entity
    *   the entity to retrieve the component from.
    * @param tag
    *   implicit [[scala.reflect.ClassTag]] to identify the component type.
    * @tparam T
    *   the type of the component.
    * @return
    *   an [[Option]] containing the component if it exists, or `None` if not found.
    */
  def getComponent[T <: Component](entity: Entity)(implicit
      tag: scala.reflect.ClassTag[T]
  ): Option[T]

  /** Remove a specific [[Component]] from the given [[Entity]].
    *
    * @param entity
    *   the entity to remove the component from.
    * @param component
    *   the component to remove.
    */
  def removeComponent(entity: Entity, component: Component): Unit

  /** Get a mapping of all entities in the world to their associated [[Component]] sets.
    *
    * @return
    *   a map where the key is an [[Entity]] and the value is a set of components.
    */
  def worldEntitiesToComponents: Map[Entity, Set[Component]]

  /** Add a [[System]] to the world to manage entities and components.
    *
    * @param system
    *   the system to add.
    */
  def addSystem(system: System): Unit

  /** Update the world by running all the systems with the given delta time.
    *
    * @param deltaTime
    *   the time that has passed since the last update, used to drive the logic in systems.
    */
  def update(): Unit

/** Factory for creating a new instance of [[World]].
  */
object World:
  def apply(): World = WorldImpl()

  private case class WorldImpl() extends World:
    private var entitiesToComponents: Map[Entity, Set[Component]] = Map()
    private var systems: List[System]                             = List()

    def createEntity(components: Component*): Entity =
      val entity = Entity()
      entitiesToComponents += (entity -> Set())
      components.foreach(addComponent(entity, _))
      entity

    def addEntity(entity: Entity, components: Component*): Unit =
      entitiesToComponents += (entity -> Set())
      components.foreach(addComponent(entity, _))

    def removeEntity(entity: Entity): Unit =
      entitiesToComponents -= entity

    def clearFromEntities(): Unit =
      entitiesToComponents = Map()

    def getEntities: List[Entity] = entitiesToComponents.keys.toList

    def addComponent(entity: Entity, component: Component): Unit =
      entitiesToComponents.get(entity) match
      case Some(components) =>
        val updatedComponents = components.filterNot(_.getClass == component.getClass) + component
        entitiesToComponents += (entity -> updatedComponents)
      case None => throw new IllegalArgumentException("Entity does not exist")

    def getComponent[T <: Component](
        entity: Entity
    )(implicit tag: scala.reflect.ClassTag[T]): Option[T] =
      entitiesToComponents
        .get(entity) match
      case Some(existingComponents) =>
        existingComponents.collectFirst { case component: T => component }
      case None => throw new IllegalArgumentException("Entity does not exist")

    def removeComponent(entity: Entity, component: Component): Unit =
      entitiesToComponents.get(entity) match
      case Some(existingComponents) =>
        val updatedComponents = existingComponents.filterNot(_ == component)
        entitiesToComponents += (entity -> updatedComponents)
      case None => throw new IllegalArgumentException("Entity does not exist")

    def worldEntitiesToComponents: Map[Entity, Set[Component]] = entitiesToComponents

    def addSystem(system: System): Unit =
      systems = system :: systems

    def update(): Unit =
      systems.foreach(_.update(this))
