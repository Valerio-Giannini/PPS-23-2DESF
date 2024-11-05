//package fixtures
//import core.{Entity, World}
//
//trait OuterWorldEntity:
//  val outerWorldEntity: Entity = Entity()
//
//trait EntityWithoutComponents(implicit world: World):
//  val entity: Entity = world.createEntity()
//
//trait EntityWithPosition(implicit world: World) extends ComponentsFixture:
//  val entity: Entity = world.createEntity(position)
//
//trait EntityWithPositionAndSpeed(implicit world: World)
//    extends ComponentsFixture:
//  val entity: Entity = world.createEntity(position, speed)
