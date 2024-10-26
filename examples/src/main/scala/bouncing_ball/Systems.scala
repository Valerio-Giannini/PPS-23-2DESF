package bouncing_ball

import core.{ComponentTag, System, World}

// Movement system: updates position using speed
class MovementSystem extends System:

  override def update(world: World): Unit =
    for entity <- world.entitiesWithAtLeastComponents(ComponentTag[Position]) do
      (entity.get[Position], entity.get[Speed]) match
      case (Some(pos), Some(speed)) =>
        world.addComponent(entity, pos.copy(pos.x + speed.vx, pos.y + speed.vy))
      case _ =>

// Collision system: if entities collide, set speed to zero
class CollisionSystem extends System:

  override def update(world: World): Unit =
    // Retrieve all entities with a Position component and convert to Seq for combinations
    val entitiesWithPosition = world.entitiesWithAtLeastComponents(ComponentTag[Position]).toSeq

    // Check for collisions between each unique pair of entities with a Position
    for
      Seq(entityA, entityB) <- entitiesWithPosition.combinations(2)
      posA <- entityA.get[Position]
      posB <- entityB.get[Position]
      if posA.x == posB.x && posA.y == posB.y
    do
      println(s"Collision detected between Entity ${entityA.id} and Entity ${entityB.id}")
      // Set the Speed of both entities to zero upon collision
      world.addComponent(entityA, Speed(0, 0))
      world.addComponent(entityB, Speed(0, 0))
