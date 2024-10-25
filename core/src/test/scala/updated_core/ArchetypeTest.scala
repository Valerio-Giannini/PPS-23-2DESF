package updated_core

import org.scalatest.Inside.inside
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ArchetypeTest extends AnyWordSpec with Matchers:

  "An Archetype" when:
    "created" should:
      "have a unique set of ComponentTag independent from order" in:
        val archetype1 = Archetype(ComponentTag[C1], ComponentTag[C2])
        val archetype2 = Archetype(ComponentTag[C2], ComponentTag[C1])
        val archetype3 = Archetype(ComponentTag[C1], ComponentTag[C2], ComponentTag[C3])

        archetype1.equalsTo(archetype2.componentTags) shouldBe true
        archetype1.equalsTo(archetype3.componentTags) shouldBe false
    "managing entities" should:
      "allow to add only a matching entity" in:
        val archetype = Archetype(ComponentTag[C1])
        val entity    = Entity(C1(1))

        archetype.add(entity)
        archetype.entities should contain(entity)

        val entity2 = Entity(C2(2))

        archetype.add(entity2)
        archetype.entities shouldNot contain(entity2)

      "do nothing when trying to add the same entity twice" in :
        val archetype = Archetype(ComponentTag[C1])
        val entityA    = Entity(C1(1))
        archetype.add(entityA)
        val archetypeWithEntityA = archetype.entities
        archetype.add(entityA)
        archetype.entities shouldBe archetypeWithEntityA

      "allow to get an entity" in:
        val archetype = Archetype(ComponentTag[C1])
        val entity    = Entity(C1(1))

        archetype.add(entity)
        val retrievedEntity = archetype.get(entity)
        inside(retrievedEntity) { case Some(e) =>
          e.id shouldEqual entity.id
        }

        val entity2          = Entity(C2(2))
        val retrievedEntity2 = archetype.get(entity2)
        retrievedEntity2 should matchPattern { case None => }
      "allow to remove an entity" in:
        val archetype = Archetype(ComponentTag[C1])
        val entity    = Entity(C1(1))

        archetype.add(entity)
        archetype.remove(entity)
        archetype.entities should not contain entity

        val entity2 = Entity(C2(2))
        archetype.remove(entity2)
        archetype.entities should not contain entity2
      "allow to clear all entities" in:
        val archetype = Archetype(ComponentTag[C1])
        val entity1   = Entity(C1(1))
        val entity2   = Entity(C1(2))

        archetype.add(entity1)
        archetype.add(entity2)
        archetype.clearEntities()

        archetype.entities shouldBe empty
