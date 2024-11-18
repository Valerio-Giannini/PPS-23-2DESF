package BouncingBall.model

import BouncingBall.model.GlobalParameters.{ballRadius, borderSize, deceleration}
import core.{System, World}
import dsl.DSL.*

/**
 * A system responsible for updating the positions and velocities of entities
 * based on their current speed. It applies deceleration to simulate friction
 * or resistance.
 */
class MovementSystem extends System:
  /**
   * Updates the world by recalculating the positions and speeds of entities
   * that have both `Position` and `Speed` components.
   *
   * - Adjusts positions based on velocity.
   * - Reduces velocity using deceleration until it approaches zero.
   *
   * @param world the simulation world containing entities and components.
   */
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

/**
 * A system that ensures entities remain within the boundaries of the world.
 * Entities bounce off the edges by reversing their velocity upon collision.
 */
class BoundaryBounceSystem extends System:

  /**
   * Updates the world by checking entities with `Position` and `Speed` components
   * for boundary collisions. Adjusts positions and reverses velocity for collisions.
   *
   * @param world the simulation world containing entities and components.
   */
  override def update(world: World): Unit =
    for
      entity <- from(world).entitiesHaving(POSITION, SPEED, DIMENSION)
      pos <- from(world).componentsOf(entity).get[Position]
      speed <- from(world).componentsOf(entity).get[Speed]
      dim <- from(world).componentsOf(entity).get[Dimension]
    do
      var newVx = speed.vx
      var newVy = speed.vy
      var newX = pos.x
      var newY = pos.y

      if pos.x > borderSize() - dim.x then
        newX = borderSize() - dim.x - 1
        newVx = -newVx
        println(s"* Entity ${entity.id} collision on x >")

      else if pos.x < -borderSize() + dim.x then
        newX = -borderSize() + dim.x + 1
        newVx = -newVx
        println(s"* Entity ${entity.id} collision on x <")

      if pos.y > borderSize() - dim.x then
        newY = borderSize() - dim.x - 1
        newVy = -newVy
        println(s"* Entity ${entity.id} collision on y >")

      else if pos.y < -borderSize() + dim.x then
        newY = -borderSize() + dim.x + 1
        newVy = -newVy
        println(s"* Entity ${entity.id} collision on y <")

      val entity2 = into(world).componentsOf(entity).add(Position(newX, newY))
      into(world).componentsOf(entity2).add(Speed(newVx, newVy))

/**
 * A system that handles collisions between entities.
 * Adjusts velocities of entities upon collision to simulate a bounce.
 */
class CollisionSystem extends System:
  /**
   * Updates the world by checking for collisions between entities with `Position`
   * and `Speed` components. Calculates new velocities based on collision physics.
   *
   * @param world the simulation world containing entities and components.
   */
  override def update(world: World): Unit =
    val entities = from(world).entitiesHaving(POSITION, SPEED, DIMENSION).toSeq
    for
      i <- entities.indices
      j <- (i + 1) until entities.size
      entityA = entities(i)
      entityB = entities(j)

      posA <- from(world).componentsOf(entityA).get[Position]
      posB <- from(world).componentsOf(entityB).get[Position]
      speedA <- from(world).componentsOf(entityA).get[Speed]
      speedB <- from(world).componentsOf(entityB).get[Speed]
      dimA <- from(world).componentsOf(entityA).get[Dimension]
      dimB <- from(world).componentsOf(entityA).get[Dimension]

      // Perform collision detection
      if isCollision(posA, posB, dimA, dimB)
    do
      // Calculate the new Dimension after a collision
      val newDimA = Dimension(dimA.x + 1)
      val newDimB = Dimension(dimB.x + 1)

      // Calculate the vector separating the two entities
      val dx = posB.x - posA.x
      val dy = posB.y - posA.y
      val distance = Math.sqrt(dx * dx + dy * dy)

      // Normalize the collision vector
      val nx = dx / distance
      val ny = dy / distance

      // Compute the velocity components along the collision normal
      val vA = speedA.vx * nx + speedA.vy * ny
      val vB = speedB.vx * nx + speedB.vy * ny

      // Compute the tangential velocity components
      val tAx = speedA.vx - vA * nx
      val tAy = speedA.vy - vA * ny
      val tBx = speedB.vx - vB * nx
      val tBy = speedB.vy - vB * ny

      // Apply a damping factor to reduce velocity after the collision
      val factor = 0.9
      val newVA = -vA * factor
      val newVB = -vB * factor

      // Calculate new velocities for both entities
      val newSpeedA = Speed(
        newVA * nx + tAx,// Combine normal and tangential components
        newVA * ny + tAy
      )
      val newSpeedB = Speed(
        newVB * nx + tBx,
        newVB * ny + tBy
      )

      into(world).componentsOf(entityA).add(newSpeedA)
      into(world).componentsOf(entityA).add(newDimA)
      into(world).componentsOf(entityB).add(newSpeedB)
      into(world).componentsOf(entityB).add(newDimB)

  /**
   * Determines if two entities are colliding.
   *
   * @param posA the position of the first entity.
   * @param posB the position of the second entity.
   * @return `true` if the entities are colliding, `false` otherwise.
   */
  private def isCollision(posA: Position, posB: Position,dimA: Dimension, dimB: Dimension): Boolean =
    val distance = math.sqrt(math.pow(posA.x - posB.x, 2) + math.pow(posA.y - posB.y, 2))
    val threshold = dimA.x + dimB.x
    distance < threshold

/**
 * A system that prints the position and speed of all entities.
 */
class PrintPositionAndSpeedOfEntitiesSystem extends System:

  /**
   * Prints the `Position` and `Speed` components of all entities that have them.
   *
   * @param world the simulation world containing entities and components.
   */
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



