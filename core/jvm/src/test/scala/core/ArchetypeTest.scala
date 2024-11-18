package core

import org.scalatest.Inside.inside
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ArchetypeTest extends AnyWordSpec with Matchers:
  val setC1: Set[ComponentTag[_]] = summon[ComponentChainTag[C1 :: CNil]].tags

  "An Archetype" when:
    "created" should:
      "have the same set of ComponentTags regardless of their order" in:
        val setC1C2 = summon[ComponentChainTag[C1 :: C2 :: CNil]].tags
        val setC2C1 = summon[ComponentChainTag[C2 :: C1 :: CNil]].tags
        val setC1C2C3 = summon[ComponentChainTag[C1 :: C2 :: C3 :: CNil]].tags

        val archetype1 = Archetype(setC1C2)
        val archetype2 = Archetype(setC2C1)
        val archetype3 = Archetype(setC1C2C3)

        archetype1.componentTags shouldEqual archetype2.componentTags
        archetype1.componentTags should not equal archetype3.componentTags

    "managing entities" should:
      "allow adding an entity if it matches the required components" in:
        val archetype = Archetype(setC1)
        val entity = Entity(C1(1))
        archetype.add(entity)
        archetype.entities should contain(entity)

      "do nothing when attempting to add an entity with non-matching components" in:
        val archetype = Archetype(setC1)
        val entity = Entity(C2(2))
        archetype.add(entity)
        archetype.entities shouldNot contain(entity)

      "ignore attempts to add the same entity more than once" in:
        val archetype = Archetype(setC1)
        val entity = Entity(C1(1))
        archetype.add(entity)
        val initialEntities = archetype.entities
        archetype.add(entity)
        archetype.entities shouldBe initialEntities

      "allow retrieving an entity if it exists in the archetype" in:
        val archetype = Archetype(setC1)
        val entity = Entity(C1(1))

        archetype.add(entity)
        val retrievedEntity = archetype.get(entity)
        inside(retrievedEntity) { case Some(e) =>
          e.id shouldEqual entity.id
        }

        val entity2 = Entity(C2(2))
        val retrievedEntity2 = archetype.get(entity2)
        retrievedEntity2 shouldBe None

      "allow removing an entity if it exists" in:
        val archetype = Archetype(setC1)
        val entity = Entity(C1(1))

        archetype.add(entity)
        archetype.remove(entity)
        archetype.entities should not contain entity

        val entity2 = Entity(C2(2))
        archetype.remove(entity2)
        archetype.entities should not contain entity2

      "allow clearing all entities in the archetype" in:
        val archetype = Archetype(setC1)
        val entity1 = Entity(C1(1))
        val entity2 = Entity(C1(2))

        archetype.add(entity1)
        archetype.add(entity2)
        archetype.clearEntities()

        archetype.entities shouldBe empty
