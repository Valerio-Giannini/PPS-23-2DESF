package updated_core

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
    "managing entities" should :
      "allow to add only a matching entity" in :
        val archetype = Archetype(ComponentTag[C1])
        val entity = Entity(C1(1))

        archetype.add(entity)
        archetype.entities should contain(entity)

        val entity2 = Entity(C2(2))

        archetype.add(entity2)
        archetype.entities shouldNot contain(entity2)
