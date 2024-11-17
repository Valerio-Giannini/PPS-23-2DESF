package core

import org.scalatest.Inside.inside
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ArchetypeTest extends AnyWordSpec with Matchers:

  "An Archetype" when:
    "created" should:
      "have a unique set of ComponentTag independent from order" in:
        val setC1C2 = summon[ComponentChainTag[C1 :: C2 :: CNil]].tags
        val setC2C1 = summon[ComponentChainTag[C2 :: C1 :: CNil]].tags
        val setC1C2C3 = summon[ComponentChainTag[C1 :: C2 :: C3 :: CNil]].tags

        val archetype1 = Archetype(setC1C2)
        val archetype2 = Archetype(setC2C1)
        val archetype3 = Archetype(setC1C2C3)

        archetype1.componentTags shouldEqual archetype2.componentTags
        archetype1.componentTags should not equal archetype3.componentTags
    "managing entities" should:
      "allow to add only a matching entity" in:
        val setC1 = summon[ComponentChainTag[C1 :: CNil]].tags
        val archetype = Archetype(setC1)
        val entity    = Entity(C1(1))
        archetype.add(entity)
        archetype.entities should contain(entity)

      "do nothing when trying to add a non matching entity" in:
        val setC1 = summon[ComponentChainTag[C1 :: CNil]].tags
        val archetype = Archetype(setC1)
        val entity = Entity(C2(2))
        archetype.add(entity)
        archetype.entities shouldNot contain(entity)

      "do nothing when trying to add the same entity twice" in :
        val setC1 = summon[ComponentChainTag[C1 :: CNil]].tags
        val archetype = Archetype(setC1)
        val entityA    = Entity(C1(1))
        archetype.add(entityA)
        val archetypeWithEntityA = archetype.entities
        archetype.add(entityA)
        archetype.entities shouldBe archetypeWithEntityA

      "allow to get an entity" in:
        val setC1 = summon[ComponentChainTag[C1 :: CNil]].tags
        val archetype = Archetype(setC1)
        val entity    = Entity(C1(1))

        archetype.add(entity)
        val retrievedEntity = archetype.get(entity)
        inside(retrievedEntity) { case Some(e) =>
          e.id shouldEqual entity.id
        }

        val entity2          = Entity(C2(2))
        val retrievedEntity2 = archetype.get(entity2)
        retrievedEntity2 shouldBe None
      "allow to remove an entity" in:
        val setC1 = summon[ComponentChainTag[C1 :: CNil]].tags
        val archetype = Archetype(setC1)
        val entity    = Entity(C1(1) :: CNil)

        archetype.add(entity)
        archetype.remove(entity)
        archetype.entities should not contain entity

        val entity2 = Entity(C2(2))
        archetype.remove(entity2)
        archetype.entities should not contain entity2
      "allow to clear all entities" in:
        val setC1 = summon[ComponentChainTag[C1 :: CNil]].tags
        val archetype = Archetype(setC1)
        val entity1   = Entity(C1(1))
        val entity2   = Entity(C1(2))

        archetype.add(entity1)
        archetype.add(entity2)
        archetype.clearEntities()

        archetype.entities shouldBe empty
