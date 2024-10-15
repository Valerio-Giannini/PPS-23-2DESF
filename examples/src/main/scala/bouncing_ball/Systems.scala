package bouncing_ball

import core.System
import core.World
import dsl.coreDSL.{from, into}

// Movement system: updates position using speed
class MovementSystem extends System:
  override def update(world: World): Unit =
    for
      entity <- from(world).getEntities
      pos    <- from(world).getComponent[Position].of(entity)
      speed  <- from(world).getComponent[Speed].of(entity)
    do
      into(world)
        .addComponent(pos.copy(pos.x + speed.vx, pos.y + speed.vy))
        .to(entity)

// Collision system: if entities collide, set speed to zero
class CollisionSystem extends System:
  override def update(world: World): Unit =
    val entities = from(world).getEntities
    for
      i <- entities.indices
      j <- (i + 1) until entities.size
      entityA = entities(i)
      entityB = entities(j)
      posA <- from(world).getComponent[Position].of(entityA)
      posB <- from(world).getComponent[Position].of(entityB)
      if posA.x == posB.x && posA.y == posB.y
    do
      println(s"Collision detected between Entity ${entityA.id} and Entity ${entityB.id}")
      into(world)
        .addComponent(Speed(0,0))
        .to(entityA)
      into(world)
        .addComponent(Speed(0,0))
        .to(entityB)

class PrintPositionAndSpeedOfEntitiesSystem extends System:
  override def update(world: World): Unit =
    for
      entity <- from(world).getEntities
      pos = from(world).getComponent[Position].of(entity).map(pos => s"Position(${pos.x}, ${pos.y})").getOrElse("No Position")
      speed = from(world).getComponent[Speed].of(entity).map(speed => s"Speed(${speed.vx}, ${speed.vy})").getOrElse("No Speed")
    do
      println(s"Entity ${entity.id}: $pos, $speed")
    println("-------------------")