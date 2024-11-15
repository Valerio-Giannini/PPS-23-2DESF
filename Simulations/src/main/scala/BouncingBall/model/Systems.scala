package BouncingBall.model

import core.{System, World}
import dsl.DSL.*
import SimulationParameters.*

// Movement system: updates position using speed
class MovementSystem extends System:
  override def update(using world: World): Unit =
    for
      entity <- from(world).entitiesHaving(POSITION, SPEED)
      pos    <- from(world).componentsOf(entity).get[Position]
      speed  <- from(world).componentsOf(entity).get[Speed]
    do
      val newX = pos.x + speed.vx
      val newY = pos.y + speed.vy

      val newVx = speed.vx match
        case vx if vx > 0  => math.max(0.0, vx - deceleration)
        case vx if vx < 0  => math.min(0.0, vx + deceleration)
        case _             => 0.0

      val newVy = speed.vy match
        case vy if vy > 0  => math.max(0.0, vy - deceleration)
        case vy if vy < 0  => math.min(0.0, vy + deceleration)
        case _             => 0.0

      val finalVx = if math.abs(newVx) < deceleration then 0.0 else newVx
      val finalVy = if math.abs(newVy) < deceleration then 0.0 else newVy

      into(world)
        .componentsOf(entity)
        .add(pos.copy(newX, newY))

      into(world)
        .componentsOf(entity)
        .add(speed.copy(finalVx, finalVy))


class BoundaryBounceSystem extends System:
  override def update(using world: World): Unit =
    for
      entity <- from(world).entitiesHaving(POSITION, SPEED)
      pos <- from(world).componentsOf(entity).get[Position]
      speed <- from(world).componentsOf(entity).get[Speed]
    do
      var newVx = speed.vx
      var newVy = speed.vy
      var newX = pos.x
      var newY = pos.y

      if pos.x > borderSize - ballRadius then
        newX = borderSize - ballRadius - 1
        newVx = -newVx
        println(s"* Entity ${entity.id} collision on x >")

      else if pos.x < -borderSize + ballRadius then
        newX = -borderSize + ballRadius + 1
        newVx = -newVx
        println(s"* Entity ${entity.id} collision on x <")

      if pos.y > borderSize - ballRadius then
        newY = borderSize - ballRadius - 1
        newVy = -newVy
        println(s"* Entity ${entity.id} collision on y >")

      else if pos.y < -borderSize + ballRadius then
        newY = -borderSize + ballRadius + 1
        newVy = -newVy
        println(s"* Entity ${entity.id} collision on y <")

      val entity2 = into(world).componentsOf(entity).add(Position(newX, newY))
      into(world).componentsOf(entity2).add(Speed(newVx, newVy))

class CollisionSystem extends System:
  override def update(using world: World): Unit =
    val entities = from(world).entitiesHaving(POSITION, SPEED).toSeq
    for
      i <- entities.indices
      j <- (i + 1) until entities.size
      entityA = entities(i)
      entityB = entities(j)

      posA <- from(world).componentsOf(entityA).get[Position]
      posB <- from(world).componentsOf(entityB).get[Position]
      speedA <- from(world).componentsOf(entityA).get[Speed]
      speedB <- from(world).componentsOf(entityB).get[Speed]

      if isCollision(posA, posB)
    do
      val factor = 0.5
      val newSpeedA = Speed(-speedA.vx * factor, -speedA.vy * factor)
      val newSpeedB = Speed(-speedB.vx * factor, -speedB.vy * factor)

      into(world).componentsOf(entityA).add(newSpeedA)
      into(world).componentsOf(entityB).add(newSpeedB)

      val displacement = 1
      val newPosA = Position(posA.x + speedA.vx * displacement, posA.y + speedA.vy * displacement)
      val newPosB = Position(posB.x + speedB.vx * displacement, posB.y + speedB.vy * displacement)

      into(world).componentsOf(entityA).add(newPosA)
      into(world).componentsOf(entityB).add(newPosB)

  private def isCollision(posA: Position, posB: Position): Boolean =
    val distance = math.sqrt(math.pow(posA.x - posB.x, 2) + math.pow(posA.y - posB.y, 2))
    val threshold = 2 * ballRadius
    distance < threshold

class PrintPositionAndSpeedOfEntitiesSystem extends System:
  override def update(using world: World): Unit =
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
    do
      println(s"Entity ${entity.id}: $pos, $speed")



