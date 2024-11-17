package dsl.coreDSL

import core.*


/** The `Into` trait provides methods to create, add, and manage entities and systems within a `World`. It enables
  * spawning entities with optional components, adding components to entities, and including systems in the `World`.
  *
  * Operators:
  *
  * Spawn a new entity
  * {{{
  * into(world).spawnNewEntity
  * }}}
  *
  * Spawn a new entity with specified components
  * {{{
  * into(world).spawnNewEntityWith(componentA, componentB)
  * }}}
  *
  * Add an existing entity to the world
  * {{{
  * into(world).spawnEntity(entity)
  * }}}
  *
  * Access components of an entity
  * {{{
  * into(world).componentsOf(entity)
  * }}}
  *
  * Add a system to the world
  * {{{
  * into(world).include(system)
  * }}}
  */
trait Into:
  /** Spawns a new entity, without components, within the world.
    *
    * @return
    *   The newly created [[Entity]] instance
    */
  def spawnNewEntity: Entity


  // TODO


  def spawnNewEntityWith[C <: ComponentChain : ComponentChainTag](components: C): Entity

  def spawnNewEntityWith[C <: Component : ComponentTag](component: C): Entity

  /** Adds an existing entity to the world.
    *
    * @param entity
    *   The entity to add to the world.
    * @return
    *   The added [[Entity]] instance in the [[World]]
    */
  def spawnEntity(entity: Entity): Entity

  /** Creates a [[IntoComponentBuilder]] to access or modify components associated with a specific entity.
    *
    * @param entity
    *   the entity whose components are to be accessed or modified
    * @return
    *   a [[IntoComponentBuilder]] instance for the specified entity
    */
  def componentsOf(entity: Entity): IntoComponentBuilder

  /** Adds a system to the world.
    *
    * @param system
    *   The [[System]] to add
    * @return
    *   The current World instance with the added system
    */
  def include(system: System): World

object Into:
  def apply(world: World): Into = IntoImpl(world)
  private class IntoImpl(world: World) extends Into:

    override def spawnNewEntity: Entity = world.createEntity()

    override def spawnNewEntityWith[C <: ComponentChain : ComponentChainTag](components: C): Entity =
      world.createEntity(components)

    override def spawnNewEntityWith[C <: Component : ComponentTag](component: C): Entity =
      world.createEntity(component)

    override def spawnEntity(entity: Entity): Entity =
      world.addEntity(entity)
      world.entity(entity).get

    override def componentsOf(entity: Entity): IntoComponentBuilder = IntoComponentBuilder(world, entity)

    override def include(system: System): World = world.addSystem(system)

/** The [[IntoComponentBuilder]] trait allows adding components to a specified entity in the `World`.
  *
  * Operations:
  *
  * Add component
  * {{{
  *   from(world).componentsOf(entity).add(component)
  * }}}
  */
trait IntoComponentBuilder:
  /** Adds a component to a specified entity in the world.
   * @param component
   * The [[Component]] to add.
   * @tparam C
   * The type of the component, constrained to [[Component]].
   * @return
   * The updated entity with the new component
   */
  def add[C <: Component: ComponentTag](component: C): Entity

object IntoComponentBuilder:
  def apply(world: World, entity: Entity): IntoComponentBuilder = IntoComponentBuilderImpl(world, entity)

  private class IntoComponentBuilderImpl(world: World, entity: Entity) extends IntoComponentBuilder:

    override def add[C <: Component: ComponentTag](component: C): Entity =
      world.addComponent[C](entity, component)
      world.entity(entity).get
