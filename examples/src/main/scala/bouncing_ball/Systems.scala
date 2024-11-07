package bouncing_ball

import core.{System, World}
import dsl.DSL.*

// Movement system: updates position using speed
class MovementSystem extends System:

  override def update(world: World): Unit =
    for
      entity <- from(world).entitiesHaving(POSITION, SPEED)
      pos    <- from(world).componentsOf(entity).get[Position]
      speed  <- from(world).componentsOf(entity).get[Speed]
    do into(world).componentsOf(entity).add(Position(pos.x + speed.vx, pos.y + speed.vy))

// Collision system: if entities collide, set speed to zero
class CollisionSystem extends System:
  override def update(world: World): Unit =
    val entities = from(world).entitiesHaving(POSITION, SPEED).toSeq
    val threshold = 20.0 // Soglia per la collisione

    for
      i <- entities.indices
      j <- (i + 1) until entities.size
      entityA = entities(i)
      entityB = entities(j)
      posA <- from(world).componentsOf(entityA).get[Position]
      posB <- from(world).componentsOf(entityB).get[Position]
      if isColliding(posA, posB, threshold)
    do
      println(s"Collision detected between Entity ${entityA.id} and Entity ${entityB.id}")
      into(world).componentsOf(entityA).add(Speed(0, 0))
      into(world).componentsOf(entityB).add(Speed(0, 0))

  private def isColliding(posA: Position, posB: Position, threshold: Double): Boolean =
    val distance = Math.hypot(posA.x - posB.x, posA.y - posB.y)
    distance < threshold


class PrintPositionAndSpeedOfEntitiesSystem extends System:

  override def update(world: World): Unit =
    for
      entity <- from(world).entitiesHaving(POSITION, SPEED)
      pos = from(world)
        .componentsOf(entity)
        .get[Position]
        .map(pos => s"Position(${pos.x}, ${pos.y})")
        .getOrElse("No Position")
      speed = from(world)
        .componentsOf(entity)
        .get[Speed]
        .map(speed => s"Speed(${speed.vx}, ${speed.vy})")
        .getOrElse("No Speed")
    do println(s"Entity ${entity.id}: $pos, $speed")
    println("-------------------")
