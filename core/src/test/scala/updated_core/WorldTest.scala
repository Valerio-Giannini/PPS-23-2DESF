package updated_core

import org.scalatest.BeforeAndAfterEach
import org.scalatest.Inside.inside
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class WorldTest extends AnyWordSpec with Matchers with BeforeAndAfterEach:

  var world: World = _

  override def beforeEach(): Unit =
    world = SimpleWorld()

  "A World" when:
    "initialized" should:
      "be empty" in:
        world.entities shouldBe empty
    "managing entity" should:
      "allow adding an already existing entity" in:
        val entity = Entity()
        world.addEntity(entity)
        world.entities should contain(entity)
      "allow creation of an entity without components" in:
        val entity = world.createEntity()
        world.entities should contain(entity)
      "allow creation of an entity with multiple components" in:
        val entity = world.createEntity(C1(1), C2(2))
        world.entities should contain(entity)
        entity.componentTags should have size 2
      "allow creation of an entity without duplicate components" in:
        val entity = world.createEntity(C1(1), C1(2))
        world.entities should contain(entity)
        entity.componentTags should have size 1
      "allow the removal of an entity" in:
        val entityA = world.createEntity()
        val entityB = world.createEntity()
        world.removeEntity(entityB)
        world.entities should have size 1
      "allow the removal and reinsertion of an entity with its components" in:
        val entity = world.createEntity(C1(1), C2(2))
        world.entities should contain(entity)
        world.removeEntity(entity)
        world.entities shouldBe empty
        world.addEntity(entity)
        world.entities should contain(entity)
      "do nothing when trying to remove a non-existent entity" in:
        val outWorldEntity = Entity()
        world.removeEntity(outWorldEntity)
        world.entities shouldBe empty
      "allow to clear all of them" in:
        val numEntities = 10
        List.fill(numEntities)(world.createEntity())
        world.clearEntities()
        world.entities shouldBe empty
      "allow to get entities with specific component types" in:
        val numEntities = 10
        (1 to numEntities).foreach { _ =>
          world.createEntity(C1(1))
          world.createEntity(C2(2))
          world.createEntity(C1(1), C2(2))
        }
        val entitiesWithC1 = world.entitiesWithComponents(ComponentTag[C1])
        all(entitiesWithC1.map(_.get[C1])) should not be empty
        entitiesWithC1 should have size numEntities
        val entitiesWithAtLeastC1 = world.entitiesWithAtLeastComponents(ComponentTag[C1])
        all(entitiesWithAtLeastC1.map(_.get[C1])) should not be empty
        entitiesWithAtLeastC1 should have size numEntities*2

    "managing entity's components" should:
      "allow adding a new component" in:
        val entity = world.createEntity(C1(1))
        world.addComponent(entity, C2(2))
        inside(world.entities.find(_.id == entity.id)) {
          case Some(e) => e.componentTags should have size 2
          case None    => fail("Entity not found")
        }
      "allow updating an existing component" in:
        val entity   = world.createEntity(C1(1))
        val newValue = 2
        world.addComponent(entity, C1(newValue))
        inside(world.entities.find(_.id == entity.id)) {
          case Some(e) =>
            e.get[C1] should not be empty
            e.get[C1] shouldBe Some(C1(newValue))
          case None => fail("Entity not found")
        }
      "allow retrieval of an existing component" in:
        val entity = world.createEntity(C1(1))
        world.getComponent[C1](entity) shouldBe Some(C1(1))
      "do nothing when retrieval of an existing component" in:
        val entity = world.createEntity(C1(1))
        world.getComponent[C2](entity) shouldBe None
      "allow remove an existing component" in:
        val entity = world.createEntity(C1(1), C2(2))
        world.removeComponent[C2](entity)
        inside(world.entities.find(_.id == entity.id)) {
          case Some(e) =>
            e.componentTags should have size 1
            e.get[C2] shouldBe None
          case None => fail("Entity not found")
        }
      "do nothing when trying to remove a component that does not exist" in:
        val entity = world.createEntity(C1(1))
        world.removeComponent[C2](entity)
        inside(world.entities.find(_.id == entity.id)) {
          case Some(e) =>
            e.componentTags should have size 1
            e.get[C1] shouldBe Some(C1(1))
          case None => fail("Entity not found")
        }
