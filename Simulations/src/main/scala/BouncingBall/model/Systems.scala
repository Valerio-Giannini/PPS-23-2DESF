package BouncingBall.model

import BouncingBall.model.GlobalParameters.{ballRadius, borderSize, deceleration}
import core.{System, World}
import dsl.DSL.*

// Movement system: updates position using speed
class MovementSystem extends System:
  override def update(world: World): Unit =
    for
      entity <- from(world).entitiesHaving(POSITION, SPEED)
      pos    <- from(world).componentsOf(entity).get[Position]
      speed  <- from(world).componentsOf(entity).get[Speed]
    do
      val newX = pos.x + speed.vx
      val newY = pos.y + speed.vy

      val newVx = speed.vx match
        case vx if vx > 0  => math.max(0.0, vx - deceleration())
        case vx if vx < 0  => math.min(0.0, vx + deceleration())
        case _             => 0.0

      val newVy = speed.vy match
        case vy if vy > 0  => math.max(0.0, vy - deceleration())
        case vy if vy < 0  => math.min(0.0, vy + deceleration())
        case _             => 0.0

      val finalVx = if math.abs(newVx) < deceleration() then 0.0 else newVx
      val finalVy = if math.abs(newVy) < deceleration() then 0.0 else newVy

      into(world)
        .componentsOf(entity)
        .add(pos.copy(newX, newY))

      into(world)
        .componentsOf(entity)
        .add(speed.copy(finalVx, finalVy))


class BoundaryBounceSystem extends System:
  override def update(world: World): Unit =
    for
      entity <- from(world).entitiesHaving(POSITION, SPEED)
      pos <- from(world).componentsOf(entity).get[Position]
      speed <- from(world).componentsOf(entity).get[Speed]
    do
      var newVx = speed.vx
      var newVy = speed.vy
      var newX = pos.x
      var newY = pos.y

      if pos.x > borderSize() - ballRadius() then
        //newX = borderSize() - ballRadius() - 1
        newVx = -newVx
        println(s"* Entity ${entity.id} collision on x >")

      else if pos.x < -borderSize() + ballRadius() then
        //newX = -borderSize() + ballRadius() + 1
        newVx = -newVx
        println(s"* Entity ${entity.id} collision on x <")

      if pos.y > borderSize() - ballRadius() then
        //newY = borderSize() - ballRadius() - 1
        newVy = -newVy
        println(s"* Entity ${entity.id} collision on y >")

      else if pos.y < -borderSize() + ballRadius() then
        //newY = -borderSize() + ballRadius() + 1
        newVy = -newVy
        println(s"* Entity ${entity.id} collision on y <")

      val entity2 = into(world).componentsOf(entity).add(Position(newX, newY))
      into(world).componentsOf(entity2).add(Speed(newVx, newVy))

class CollisionSystem extends System:
  override def update(world: World): Unit =
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
      // Calcola il vettore normale della collisione
      val dx = posB.x - posA.x
      val dy = posB.y - posA.y
      val distance = Math.sqrt(dx * dx + dy * dy)
      val nx = dx / distance
      val ny = dy / distance

      // Proietta le velocità lungo la normale
      val vA = speedA.vx * nx + speedA.vy * ny
      val vB = speedB.vx * nx + speedB.vy * ny

      // Calcola le velocità tangenziali
      val tAx = speedA.vx - vA * nx
      val tAy = speedA.vy - vA * ny
      val tBx = speedB.vx - vB * nx
      val tBy = speedB.vy - vB * ny

      // Inverti la componente normale per lo scambio (collisione elastica)
      val factor = 0.9 // coefficiente di restituzione
      val newVA = -vA * factor
      val newVB = -vB * factor

      // Ricostruisci le nuove velocità
      val newSpeedA = Speed(
        newVA * nx + tAx,
        newVA * ny + tAy
      )
      val newSpeedB = Speed(
        newVB * nx + tBx,
        newVB * ny + tBy
      )

      into(world).componentsOf(entityA).add(newSpeedA)
      into(world).componentsOf(entityB).add(newSpeedB)


  private def isCollision(posA: Position, posB: Position): Boolean =
    val distance = math.sqrt(math.pow(posA.x - posB.x, 2) + math.pow(posA.y - posB.y, 2))
    val threshold = 2* ballRadius()
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
    do
      println(s"Entity ${entity.id}: $pos, $speed")



