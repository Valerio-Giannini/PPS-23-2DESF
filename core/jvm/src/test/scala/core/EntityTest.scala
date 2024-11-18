package core

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
        Entity().componentTags should have size 0
        Entity(C1(1)).componentTags should have size 1
        Entity(C1(1) :: C2(2)).componentTags should have size 2
        Entity(C1(1) :: C2(2) :: C3(3)).componentTags should have size 3

      "not have multiple component of the same type" in:
        Entity(C1(1) :: C1(2)).componentTags should have size 1

    "managing components" should:
      "allow adding a new component" in:
        val entity        = Entity()
        val updatedEntity = entity.add(C1(1))
        updatedEntity.get[C1] shouldBe Some(C1(1))

      "allow retrieving an existing component" in:
        val entity = Entity(C1(1))
        entity.get[C1] shouldBe Some(C1(1))

      "allow removing a component" in:
        val entity        = Entity(C1(1))
        val updatedEntity = entity.remove[C1]
        updatedEntity.get[C1] shouldBe None

      "updating a component" in:
        val entity        = Entity(C1(1))
        val updatedEntity = entity.add(C1(2))
        updatedEntity.get[C1] shouldBe Some(C1(2))
