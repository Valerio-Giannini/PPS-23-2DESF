package mvc

import bouncing_ball.{CollisionSystem, MovementSystem, Position, Speed}
import core.{ComponentTag, Entity, System, World}

import scala.util.Random

/** The main model managing the simulation-specific entities and systems. */
trait Simulation:
  def entities(): Iterable[Entity]
  var isRunning: Boolean
  def init(entityCount: Int, posX: Double, posY: Double): Unit
  def update(): Unit
  

object Simulation:
  private val world = World()
  var isRunning: Boolean = false
  
  def entities(): Iterable[Entity] =
    world.entities
  
  def init(entityCount: Int, posX: Double, posY: Double): Unit =
    initEntities(entityCount, posX, posY)
    initSystems()

  private def initEntities(entityCount: Int, posX: Double, posY: Double): Unit =
    (1 to entityCount).foreach { _ =>
      val entity = world.createEntity(Position(posX, posY), randomSpeed())
      world.addComponent(entity, Position(posX, posY))
      world.addComponent(entity, randomSpeed())
    }
  
  private def initSystems(): Unit =
    world.addSystem(MovementSystem())
    world.addSystem(CollisionSystem())

  def update(): Unit = world.update()

  def getEntityPositions: List[(Int, Position)] =
    world.entitiesWithAtLeastComponents(ComponentTag[Position]).toList.map { entity =>
      val pos = entity.get[Position].getOrElse(Position(0, 0))
      (entity.id, pos)
    }

  private def randomSpeed(): Speed =
    val dx = -1 + 2 * Random.nextDouble()
    val dy = -1 + 2 * Random.nextDouble()
    Speed(dx, dy)
