package dsl

import core.*

import scala.reflect.ClassTag

/**
 * CoreDSL provides a set of operations to interact with and manipulate the elements of the ECS core using an english-like syntax.
 */
object coreDSL:

  /**
   * Creates a new `fromWorld` context to extract data from the provided `world`.
   *
   * @param world the world instance to operate on
   * @return an instance of `fromWorld` for querying the world
   */
  def from(world: World): fromWorld = fromWorld(world)

  /**
   * Creates a new `intoWorld` context to add data into the provided `world`.
   *
   * @param world the world instance to operate on
   * @return an instance of `intoWorld` for modifying the world
   */
  def into(world: World): intoWorld = intoWorld(world)

  /**
   * Resets the world by removing all entities from it.
   *
   * @param world the world instance to reset
   */
  def reset(world: World): Unit = world.clearFromEntities()

  /**
   * Updates the world, allowing all systems to process their logic.
   *
   * @param world the world instance to update
   */
  def update(world: World): Unit = world.update()

  /**
   * Creates a new `Entity` that exists outside of the world.
   *
   * @return a new `Entity` instance
   */
  def outerWorldEntity: Entity = Entity()

/**
 * `fromWorld` provides an interface for querying and extracting information from a `World`.
 *
 * @param world the world instance to operate on
 */
case class fromWorld(world: World):
  private def _removeComponent(component: Component)(entity: Entity): Unit =
    world.removeComponent(entity, component)

  /**
   * Returns the number of entities present in the world.
   *
   * @return the total count of entities in the world
   */
  def getEntitiesCount: Int = world.worldEntitiesToComponents.size

  /**
   * Returns all entities in the world.
   *
   * @return a list of all `Entity` instances in the world
   */
  def getEntities: List[Entity] = world.getEntities

  /**
   * Removes a specific entity from the world.
   *
   * @param entity the entity to remove
   */
  def removeEntity(entity: Entity): Unit = world.removeEntity(entity)

  /**
   * Returns all components associated with entities in the world.
   *
   * @return an instance of `componentActionsForEntity` that retrieves all components for an entity
   */
  def getComponents: componentActionsForEntity[Entity, Set[Component]] =
    componentActionsForEntity(world.worldEntitiesToComponents)

  /**
   * Returns a specific component of type `T` for a given entity.
   *
   * @tparam T the component type to retrieve
   * @return an instance of `componentActionsForEntity` to get the component for an entity
   */
  def getComponent[T <: Component : ClassTag]
  : componentActionsForEntity[Entity, Option[T]] =
    componentActionsForEntity(world.getComponent[T])

  /**
   * Removes a specific component from a given entity.
   *
   * @param component the component to remove
   * @return an instance of `componentActionsForEntity` to remove the component from an entity
   */
  def removeComponent(component: Component): componentActionsForEntity[Entity, Unit] =
    componentActionsForEntity(_removeComponent(component))

/**
 * `intoWorld` provides an interface for adding entities, components, and systems to a `World`.
 *
 * @param world the world instance to operate on
 */
case class intoWorld(world: World):
  private def _spawnEntity(entity: Entity, components: Set[Component]): Unit =
    world.addEntity(entity, components.toSeq *)

  private def _addComponent(component: Component)(entity: Entity): Unit =
    world.addComponent(entity, component)

  /**
   * Spawns a new entity in the world without any components.
   *
   * @return the newly created `Entity`
   */
  def spawnNewEntity: Entity = world.createEntity()

  /**
   * Spawns a new entity in the world with the specified components.
   *
   * @param components the components to add to the new entity
   * @return the newly created `Entity`
   */
  def spawnNewEntityWith(components: Component*): Entity =
    world.createEntity(components.toSeq *)

  /**
   * Spawns an existing entity in the world without any components.
   *
   * @param entity the entity to add
   */
  def spawnEntity(entity: Entity): Unit = _spawnEntity(entity, Set())

  /**
   * Spawns an existing entity in the world with the specified components.
   *
   * @param entity     the entity to add
   * @param components the components to associate with the entity
   */
  def spawnEntityWith(entity: Entity, components: Component*): Unit =
    _spawnEntity(entity, components.toSet)

  /**
   * Adds a component to an existing entity in the world.
   *
   * @param component the component to add
   * @return an instance of `componentActionsForEntity` to add the component to the entity
   */
  def addComponent(component: Component): componentActionsForEntity[Entity, Unit] =
    componentActionsForEntity(_addComponent(component))

  /**
   * Includes a system into the world, allowing it to execute its logic on the entities.
   *
   * @param system the system to add to the world
   */
  def includeSystem(system: System): Unit = world.addSystem(system)

/**
 * A utility class that wraps an action to be performed on entities in the world.
 *
 * @tparam Entity the type of the entity
 * @tparam T      the return type of the action
 * @param action the function to apply to an entity
 */
case class componentActionsForEntity[Entity, T](action: Entity => T):

  /**
   * Applies the action for the specified entity.
   *
   * @param entity the entity to apply the action to
   * @return the result of the action
   */
  def of(entity: Entity): T = {
    printf(action.toString())
    action(entity)
  }

  /**
   * Applies the action to the specified entity.
   *
   * @param entity the entity to apply the action to
   * @return the result of the action
   */
  def to(entity: Entity): T = action(entity)

