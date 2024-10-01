package core

/** This trait represents an entity with a unique identifier.
 */
trait Entity:
  /** The unique identifier of the entity.
   *
   * @return
   * the ID of this entity.
   */
  def id: Entity.ID

/** Factory and utilities for creating and managing [[core.Entity]] instances.
 */
object Entity:
  opaque type ID = Int

  /** Internal object responsible for generating unique IDs for entities.
   */
  private object EntityIDGenerator:
    private var currentID: ID = 0

    def nextID: ID =
      currentID += 1
      currentID

  private case class EntityImpl(id: ID) extends Entity

  def apply(): Entity =
    EntityImpl(EntityIDGenerator.nextID)


@main def app: Unit =
  val x = 5
  printf(s"${x}")
