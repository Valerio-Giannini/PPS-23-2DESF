package dsl.coreDSL

import core.*
import core.given

  /** The `From` trait provides a set of methods to query and manipulate entities within a `World`. It enables entity
  * retrieval, component filtering, entity counting, and targeted entity modifications.
  *
  * Operators:
  *
  * Retrieves all entities
  * {{{
  *  from(world).allEntities
  * }}}
  *
  * Retrieves a specific entity
  * {{{
  * from(world) entity (entity)
  * }}}
  * Get the number of entities in the world
  * {{{
  * from(world).numberOfEntities
  * }}}
  * Remove an entity
  * {{{
  * from(world) kill (entity)
  * }}}
  * Access components of an entity
  * {{{
  * from(world) componentsOf(entity)
  * }}}
  * Retrieves entities that have exactly the specified set of components.
  * {{{
  * from(world).entitiesHavingOnly[ComponentA ::: ComponentB]
  * }}}
  * Retrieves entities that have at least the specified set of components.
  * {{{
  * from(world).entitiesHaving(ComponentA ::: ComponentB)
  * }}}
  */
trait FromWord:
  /** Retrieves all entities within the world.
    *
    * @return
    *   An iterable collection of [[Entity]] present in the world.
    */
  def allEntities: Iterable[Entity]

  /** Retrieves an entity within the world that match the provided entity.
    *
    * @param entity
    *   The entity to look for
    * @return
    *   An option containing the [[Entity]] present in the world if exists, None otherwise
    */
  infix def entity(entity: Entity): Option[Entity]

  /** Provides the total count of entities in the world.
    *
    * @return
    *   The number of entities
    */
  def numberOfEntities: Int

  /** Removes an entity from the world.
    *
    * @param entity
    *   The entity to remove from the world.
    * @return
    *   The current World instance with the entity removed.
    */
  infix def kill(entity: Entity): World

  /** Creates a [[FromComponentBuilder]] to access or modify components associated with a specific entity.
    *
    * @param entity
    *   the entity whose components are to be accessed or modified
    * @return
    *   a [[FromComponentBuilder]] instance for the specified entity
    */
  def componentsOf(entity: Entity): FromComponentBuilder

  /** Retrieves entities that have exactly the specified set of components.
    *
    * @tparam C
    *   The type of the component, constrained to [[ComponentChain]].
    * @return
    *   An iterable collection of [[Entity]] containing exactly the specified components.
    */
  def entitiesHavingOnly[C <: ComponentChain : ComponentChainTag]: Iterable[Entity]

  /** Retrieves entities that have exactly the specified set of components.
   *
   * @tparam C
   * The type of the component, constrained to [[ComponentChain]].
   * @return
   * An iterable collection of [[Entity]] containing exactly the specified components.
   */
  def entitiesHavingOnly[C <: Component: ComponentTag]: Iterable[Entity]

  /** Retrieves entities that have at least the specified set of components.
   *
   * @tparam C
   *   The type of the component, constrained to [[Component]].
   * @return
   *   An iterable collection of [[Entity]] instances containing at least the specified components.
   */
  def entitiesHaving[C <: Component : ComponentTag]: Iterable[Entity]

  /** Retrieves entities that have at least the specified set of components.
   *
   * @tparam C
   * The type of the component, constrained to [[Component]].
   * @return
   * An iterable collection of [[Entity]] containing exactly the specified components.
   */
  def entitiesHaving[C <: ComponentChain : ComponentChainTag]: Iterable[Entity]


object FromWord:
  def apply(world: World): FromWord = new FromWordImpl(world)

  private class FromWordImpl(world: World) extends FromWord:
    override def allEntities: Iterable[Entity]                      = world.entities.toSeq.sortBy(_.id)
    infix override def entity(entity: Entity): Option[Entity]             = world.entity(entity)
    override def numberOfEntities: Int                              = world.entities.size
    infix override def kill(entity: Entity): World                        = world.removeEntity(entity)
    override def componentsOf(entity: Entity): FromComponentBuilder = FromComponentBuilder(world, entity)

    override def entitiesHavingOnly[C <: ComponentChain : ComponentChainTag]: Iterable[Entity] =
      world.entitiesWithComponents[C]

    override def entitiesHavingOnly[C <: Component: ComponentTag]: Iterable[Entity] =
      world.entitiesWithComponents[C]

    override def entitiesHaving[C <: Component : ComponentTag]: Iterable[Entity] =
      world.entitiesWithAtLeastComponents[C]

    override def entitiesHaving[C <: ComponentChain : ComponentChainTag]: Iterable[Entity] =
      world.entitiesWithAtLeastComponents[C]


/** The [[FromComponentBuilder]] trait provides methods to access and modify components of a specific entity. It enables
  * retrieving or removing components by their type.
  *
  * Operations:
  *
  * Get component
  * {{{
  *   from(world).componentsOf(entity).get[componentTag]
  * }}}
 * Remove component
 * {{{
 *   from(world).componentsOf(entity).remove[componentTag]
 * }}}
  */
trait FromComponentBuilder:

  /** Retrieves a specific component from an entity by type.
    *
    * @tparam C
    *   The type of the component to retrieve, constrained to [[Component]].
    * @return
    *   An Option containing the component if it exists, otherwise None.
    */
  def get[C <: Component: ComponentTag]: Option[C]

  /** Removes a specific component from an entity by type.
    *
    * @tparam C
    *   The type of the component to remove, constrained to [[Component]].
    * @return
    *   The current World instance with the specified component removed from the entity.
    */
  def remove[C <: Component: ComponentTag]: Entity

object FromComponentBuilder:
  def apply(world: World, entity: Entity): FromComponentBuilder = new FromComponentOfImpl(world, entity)

  private class FromComponentOfImpl(world: World, entity: Entity) extends FromComponentBuilder:
    override def get[C <: Component: ComponentTag]: Option[C] = world.getComponent[C](entity)

    override def remove[C <: Component: ComponentTag]: Entity =
      world.removeComponent[C](entity)
      world.entity(entity).get
