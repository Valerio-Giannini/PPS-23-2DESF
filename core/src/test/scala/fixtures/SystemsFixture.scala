package fixtures

import core.{System, World}

private class Movement extends System:
  override def update(world: World): Unit =
    for
      entity <- world.getEntities
      pos    <- world.getComponent[Position](entity)
      speed  <- world.getComponent[Speed](entity)
    do
      world
        .addComponent(entity, pos.copy(pos.x + speed.vx, pos.y + speed.vy))

private class AddEntity extends System:
  override def update(world: World): Unit =
    world.createEntity()

trait MovementSystem(implicit world: World):
  world.addSystem(Movement())

trait AddEntitySystem(implicit world: World):
  world.addSystem(AddEntity())
  
  
  
  