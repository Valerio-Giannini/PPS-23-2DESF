package dsl

import core.*
import dsl.DSL.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CoreDSLSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach:

  var world: World = _

  override def beforeEach(): Unit =
    world = newWorld

  "The CoreDSL" should:
    "provide a from operator" which:
      "allows getting the number of entities with numberOfEntities" in:
        from(world).numberOfEntities shouldBe 0
      "allows to get all entities with allEntities" in:
        val numEntities = 10
        List.fill(numEntities)(into(world).spawnNewEntity)
        from(world).allEntities shouldBe a[Iterable[Entity]]
        from(world).allEntities should have size numEntities
      "allows to remove entities with kill" in:
        val entity = into(world).spawnNewEntity
        from(world).numberOfEntities shouldBe 1
        from(world).kill(entity)
        from(world).numberOfEntities shouldBe 0
      "allow to get entities with exactly the specified set of components with entitiesHavingOnly" in:
        val numEntities = 10
        (1 to numEntities).foreach { _ =>
          into(world).spawnNewEntityWith(C1(1))
          into(world).spawnNewEntityWith(C2(2))
          into(world).spawnNewEntityWith(C1(1), C2(2))
        }
        val entitiesWithC1      = from(world).entitiesHavingOnly(ComponentTag[C1])
        val entitiesWithC1AndC2 = from(world).entitiesHavingOnly(ComponentTag[C1], ComponentTag[C2])
        entitiesWithC1 should have size numEntities
        entitiesWithC1AndC2 should have size numEntities
      "allow to get entities with the minimum required set of components with entitiesHaving " in:
        val numEntities = 10
        (1 to numEntities).foreach { _ =>
          into(world).spawnNewEntityWith(C1(1))
          into(world).spawnNewEntityWith(C2(2))
          into(world).spawnNewEntityWith(C1(1), C2(2))
        }
        val entitiesWithAtLeastC1 = from(world).entitiesHaving(ComponentTag[C1])
        entitiesWithAtLeastC1 should have size numEntities * 2

      "allows to retrieve an existing component with componentOf and get" in:
        val c1         = C1(1)
        val entity     = into(world).spawnNewEntityWith(c1, C2(2))
        val c1OfEntity = from(world).componentsOf(entity).get[C1]
        c1OfEntity shouldBe Some(c1)
        entity.componentTags should have size 2
      "allows to remove an existing component with componentOf and remove" in:
        val c2     = C2(2)
        var entity = into(world).spawnNewEntityWith(C1(1), c2)
        entity = from(world).componentsOf(entity).remove[C1]
        entity.componentTags should have size 1
        entity.componentTags should not contain ComponentTag[C1]
    "provide an into operator" which:
      "allows to spawn new entities into the world" that:
        "have no components with spawnNewEntity" in:
          val numEntities = 10
          List.fill(numEntities)(into(world).spawnNewEntity)
          from(world).numberOfEntities shouldBe numEntities
        "have components with spawnNewEntityWith" in:
          val entity = into(world).spawnNewEntityWith(C1(1), C2(2), C3(3))
          from(world).allEntities should contain(entity)
          entity.componentTags should have size 3
      "allows to spawn an existing entity into the world" that:
        "has no components with spawnEntity" in:
          val outerWorldEntity = outerWorld.spawnEntity
          into(world).spawnEntity(outerWorldEntity)
          from(world).numberOfEntities shouldBe 1
        "have components with spawnNewEntityWith" in:
          val outerWorldEntity = outerWorld.spawnEntityWith(C1(1), C2(2), C3(1))
          val entity           = into(world).spawnEntity(outerWorldEntity)
          from(world).numberOfEntities shouldBe 1
          entity.componentTags should have size 3
      "allows to add a new component into an entity with component of and add" in:
        val c2     = C2(2)
        val c3     = C3(3)
        var entity = into(world).spawnNewEntityWith(C1(1))
        entity = into(world).componentsOf(entity).add(c2)
        entity = into(world).componentsOf(entity).add(c3)

        entity.componentTags should have size 3
        entity.componentTags should contain(ComponentTag[C2])
        entity.componentTags should contain(ComponentTag[C3])

      "allows to include systems with include" in:
        into(world).include(IncrementC1System())
        val baseValue    = 0
        val numIteration = 100
        val entity       = into(world).spawnNewEntityWith(C1(0))
        (1 to numIteration).foreach(_ => update(world))
        from(world).componentsOf(from(world).entity(entity).get).get[C1] shouldBe Some(
          C1(baseValue + numIteration)
        )
    "provide a reset operator" which:
      "allows to remove all entities" in:
        List.fill(100)(into(world).spawnNewEntity)
        reset(world)
        from(world).numberOfEntities shouldBe 0
    "provide an outerWorld operator" which:
      "allows to create an outer world entity" in:
        val outerWorldEntity = outerWorld.spawnEntity
        outerWorldEntity shouldBe a[Entity]
        from(world).numberOfEntities shouldBe 0
