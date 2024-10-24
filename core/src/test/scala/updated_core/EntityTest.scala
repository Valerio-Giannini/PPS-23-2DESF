package updated_core

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class EntityTest extends AnyWordSpec with Matchers:

  "An Entity" when:
    "created" should:
      "have a unique Integer ID" in:
        val entity1 = Entity()
        val entity2 = Entity()
        entity1.id should not be entity2.id
        entity1.id shouldBe a[Int]
        entity2.id shouldBe a[Int]

      "allow a variable number of components" in:
        Entity()
        Entity(C1(1))
        Entity(C1(1), C2(2))
        Entity(C1(1), C2(2), C3(3))

      "not have multiple component of the same type" in:
        val entity = Entity(C1(1), C1(2))
        entity.componentTags.size shouldBe 1

    "managing components" should:
      "allow adding a new component" in:
        val entity        = Entity()
        val updatedEntity = entity.add(C1(1))

      "allow retrieving an existing component" in:
        val entity = Entity(C1(1))
        entity.get[C1] should matchPattern { case Some(C1(1)) => }

      "allow removing a component" in:
        val entity        = Entity(C1(1))
        val updatedEntity = entity.remove[C1]
        updatedEntity.get[C1] shouldBe None

      "updating a component" in:
        val entity        = Entity(C1(1))
        val updatedEntity = entity.add(C1(2))
        updatedEntity.get[C1] should matchPattern { case Some(C1(2)) => }
