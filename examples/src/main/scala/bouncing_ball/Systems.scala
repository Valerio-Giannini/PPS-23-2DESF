package bouncing_ball

import view.model.{System, WorldBase}

// Movement system: updates position using speed
class MovementSystem extends System:

  override def update(world: WorldBase): Unit =
    for entity <- world.getEntities do
      (world.getComponent[Position](entity), world.getComponent[Speed](entity)) match
      case (Some(pos), Some(speed)) =>
        world.addComponent(entity, pos.copy(pos.x + speed.vx, pos.y + speed.vy))
      case _ =>

// Collision system: if entities collide, set speed to zero
class CollisionSystem extends System:

  override def update(world: WorldBase): Unit =
    val entities = world.getEntities
    for i <- entities.indices do
      for j <- (i + 1) until entities.size do
        val entityA = entities(i)
        val entityB = entities(j)
        (world.getComponent[Position](entityA), world.getComponent[Position](entityB)) match
        case (Some(posA), Some(posB)) if posA.x == posB.x && posA.y == posB.y =>
          println(s"Collision detected between Entity ${entityA.id} and Entity ${entityB.id}")
          world.addComponent(entityA, Speed(0, 0))
          world.addComponent(entityB, Speed(0, 0))
        case _ =>
