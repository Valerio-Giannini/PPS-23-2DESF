package dsl.coreDSL

import core.*

/** CoreDSL provides a set of operations to interact with and manipulate the elements of the ECS core using an
  * english-like syntax.
  *
  * Operators:
  *
  * Creates a new world
  * {{{
  *  newWorld
  * }}}
  * Query and manipulate elements
  * {{{
  * from(world)
  * }}}
  * Add elements
  * {{{
  * into(world)
  * }}}
 *  Remove all entities from the world
  * {{{
  * reset(world)
  * }}}
 * Execute the systems of the world
  * {{{
  * update(world)
  * }}}
 * Create an entity outside the world
  * {{{
  *  outerWorld
  * }}}
  */
trait CoreDSL:
  /** Creates a new, empty instance of `World`.
    *
    * @return
    *   a new `World` instance
    */
  def newWorld: World

  /** Initializes a `From` instance based on the given `world`, typically to manipulate or
    * query the elements.
    *
    * @param world
    *   the `World` instance to be used as the starting context
    * @return
    *   a `From` instance initialized with the given `world`
    */
  def from(world: World): FromWord

  /** Initializes an `Into` instance for the given `world`, typically to add an element to the world
    *
    * @param world
    *   the `World` instance to be used as the target context
    * @return
    *   an `Into` instance initialized with the given `world`
    */
  def into(world: World): IntoWord

  /**
   * Resets the given `world` instance by clearing all its entities
   *
   * @param world the `World` instance to reset
   * @return a `World` instance with cleared entities
   */
  def reset(world: World): World

  /**
   * Updates the given `world` instance by executing the systems of the world
   *
   * @param world the `World` instance to update
   */
  def update(world: World): Unit

  /**
   * Provides an instance of `OuterWorld`, to manage a entity outside the world
   *
   * @return an `OuterWorld` instance
   */
  def outerWorld: OuterWorld


object CoreDSL extends CoreDSL:
  override def newWorld: World          = World()
  override def from(world: World): FromWord = FromWord(world)

  override def into(world: World): IntoWord = IntoWord(world)

  override def outerWorld: OuterWorld = OuterWorld()

  override def reset(world: World): World = world.clearEntities()

  override def update(world: World): Unit = world.update()
