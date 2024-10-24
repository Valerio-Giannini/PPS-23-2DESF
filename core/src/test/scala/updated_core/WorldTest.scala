package updated_core

import org.scalatest.BeforeAndAfterEach
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
      "allow adding an already existing entity" in :
        val entity = Entity()
        world.addEntity(entity)
        world.entities should contain (entity)
      "allow creation of an entity without components" in :
        val entity = world.createEntity()
        world.entities should contain(entity)
      "allow creation of an entity with multiple components" in :
        val entity = world.createEntity(C1(1), C2(2))
        world.entities should contain(entity)
        entity.componentTags should have size 2
      "allow creation of an entity without duplicate components" in :
        val entity = world.createEntity(C1(1), C1(2))
        world.entities should contain(entity)
        entity.componentTags should have size 1