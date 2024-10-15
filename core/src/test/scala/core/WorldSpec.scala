package core

import fixtures.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class WorldSpec
    extends AnyWordSpec
    with Matchers
    with BeforeAndAfterEach
    with ComponentsFixture:

  implicit var world: World = _

  override def beforeEach(): Unit =
    world = World()

  "A World" when:
    "initialized" should:
      "be empty" in:
        world.worldEntitiesToComponents should have size 0
    "managing the presence of entities" should:
      "increase the entity count when new entities are added" in:
        List.fill(100)(world.createEntity())
        world.worldEntitiesToComponents should have size 100
      "return empty after is been cleared from entities" in:
        List.fill(100)(world.createEntity())
        world.worldEntitiesToComponents should have size 100
        world.clearFromEntities()
        world.worldEntitiesToComponents should have size 0
    "managing outer world entity" should:
      "allow adding it" in new OuterWorldEntity:
        world.addEntity(outerWorldEntity)
        world.worldEntitiesToComponents should have size 1
      "allow adding it with components" in new OuterWorldEntity:
        world.addEntity(outerWorldEntity, position, speed)
        world.worldEntitiesToComponents should have size 1
        world.worldEntitiesToComponents(outerWorldEntity) shouldBe Set(
          position,
          speed
        )
      "do nothing when trying to remove it" in new OuterWorldEntity:
        world.createEntity()
        world.removeEntity(outerWorldEntity)
        world.worldEntitiesToComponents should have size 1
    "managing entity" should:
      "allow creation of an entity without components" in:
        val entity = world.createEntity()
        world.worldEntitiesToComponents should have size 1
        world.worldEntitiesToComponents(entity) shouldBe Set()
      "allow creation of an entity without duplicate components" in:
        val entity =
          world.createEntity(Position(0, 0), Position(1, 1))
        world.worldEntitiesToComponents(entity).size shouldBe 1
        world.worldEntitiesToComponents(entity) shouldBe Set(
          Position(1, 1)
        )
      "allow the removal of an entity" in:
        world.createEntity()
        world.removeEntity(world.createEntity())
        world.worldEntitiesToComponents should have size 1
      "allow the removal and reinsertion of an entity with its components" in new EntityWithPositionAndSpeed:
        val componentsOfEntity: Set[Component] =
          world.worldEntitiesToComponents(entity)
        world.removeEntity(entity)
        world.addEntity(entity, componentsOfEntity.toSeq*)
        world.worldEntitiesToComponents should have size 1
        world.worldEntitiesToComponents(
          entity
        ) shouldBe Set(
          position,
          speed
        )
    "managing entity's components" should:
      "allow adding a new component" in new EntityWithPosition:
        world.addComponent(entity, speed)
        world.worldEntitiesToComponents(entity).size shouldBe 2
        world.worldEntitiesToComponents(entity) shouldBe Set(
          position,
          speed
        )
      "allow updating an existing component" in new EntityWithPosition:
        for i <- 1 to 100 do
          world.addComponent(entity, Position(i, i))
        world.worldEntitiesToComponents(entity) shouldBe Set(
          Position(100, 100)
        )
      "allow the retrieval of an existing component" in new EntityWithPosition:
        world.getComponent[Position](entity) shouldBe Some(
          position
        )
      "allow remove an existing component" in new EntityWithPositionAndSpeed:
        world.removeComponent(entity, speed)
        world.worldEntitiesToComponents(
          entity
        ) shouldBe Set(position)
      "do nothing when trying to remove a non-existing component" in new EntityWithPosition:
        world.removeComponent(entity, speed)
        world.worldEntitiesToComponents(entity).size shouldBe 1
        world.worldEntitiesToComponents(entity) shouldBe Set(
          position
        )
      "do nothing when retrieval of a non-existing component" in new EntityWithPosition:
        world.getComponent[Speed](entity) shouldBe None
    "dealing with operations on outer world entity" should:
      "throw an exception when trying to add a component" in new OuterWorldEntity:
        an[IllegalArgumentException] should be thrownBy world.addComponent(
          outerWorldEntity,
          position
        )
      "throw an exception when trying to remove a component" in new OuterWorldEntity:
        an[IllegalArgumentException] should be thrownBy world.removeComponent(
          outerWorldEntity,
          position
        )
      "throw an exception when trying to retrieval a component" in new OuterWorldEntity:
        an[IllegalArgumentException] should be thrownBy world
          .getComponent[Position](outerWorldEntity)
    "managing a system" should:
      "allow adding a system" in new MovementSystem
        with EntityWithPositionAndSpeed:
        for _ <- 1 to 100 do world.update()
        world.getComponent[Position](entity) shouldBe Some(
          Position(100, 100)
        )
      "allow the system to add new entities" in new AddEntitySystem:
        for _ <- 1 to 100 do world.update()
        world.worldEntitiesToComponents should have size 100
      "allow updating multiple entities" in new MovementSystem:
        val entityA: Entity = world.createEntity(Position(5, 5), speed)
        val entityB: Entity = world.createEntity(Position(10, 10), speed)
        val tics            = 100
        for _ <- 1 to tics do world.update()
        world.getComponent[Position](entityA) shouldBe Some(
          Position(5 + tics, 5 + tics)
        )
        world.getComponent[Position](entityB) shouldBe Some(
          Position(10 + tics, 10 + tics)
        )
      "do nothing if entity doesn't have all the necessary components" in new MovementSystem
        with EntityWithPosition:
        val tics = 10
        for _ <- 1 to tics do world.update()
        world.getComponent[Position](entity) shouldBe Some(
          Position(0, 0)
        )
