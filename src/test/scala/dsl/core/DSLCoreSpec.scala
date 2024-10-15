package dsl

import core.*
import dsl.coreDSL.*
import fixtures.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DSLCoreSpec
    extends AnyWordSpec
    with Matchers
    with BeforeAndAfterEach
    with ComponentsFixture:

  class SpawnEntities extends System:
    override def update(world: World): Unit =
      into(world).spawnNewEntity

  class MovementSystem extends System:
    override def update(world: World): Unit =
      for
        entity <- from(world).getEntities
        pos    <- from(world).getComponent[Position].of(entity)
        speed  <- from(world).getComponent[Speed].of(entity)
      do
        into(world)
          .addComponent(pos.copy(pos.x + speed.vx, pos.y + speed.vy))
          .to(entity)

  var world: World = _

  override def beforeEach(): Unit =
    world = World()

  "The CoreDSL" should:
    "provide a from operator" which:
      "allows getting the number of entities with getEntitiesCount" in:
        from(world).getEntitiesCount shouldBe 0
      "allows to get all entities with getEntities" in:
        List.fill(100)(into(world).spawnNewEntity)
        from(world).getEntities should have size 100
      "allows to remove entities with removeEntity" in :
        into(world).spawnNewEntity
        from(world).removeEntity(into(world).spawnNewEntity)
        from(world).getEntitiesCount shouldBe 1

      "allows to get all components of an entity with getComponents" in:
        val entity = into(world).spawnNewEntityWith(position, speed)
        from(world).getComponents.of(entity) shouldBe Set(position, speed)
      "allows the retrieval of an existing component with getComponent" in:
        from(world)
          .getComponent[Position]
          .of(into(world).spawnNewEntityWith(position)) shouldBe Some(
          position
        )
      "allows to remove an existing component with removeComponent" in:
        val entity = into(world).spawnNewEntityWith(position, speed)
        from(world).removeComponent(speed).of(entity)
        from(world).getComponents.of(entity) should have size 1
        from(world).getComponents.of(entity) shouldBe Set(position)
    "provide an into operator" which:
      "allows to spawn new entities" that:
        "have no components with spawnNewEntity" in:
          List.fill(100)(into(world).spawnNewEntity)
          from(world).getEntitiesCount shouldBe 100
        "have components with spawnNewEntityWith" in:
          val entity = into(world).spawnNewEntityWith(position)
          from(world).getEntitiesCount shouldBe 1
          from(world).getComponents.of(entity) shouldBe Set(
            position
          )
      "allows to spawn entities" that:
        "have no components with spawnEntity" in:
          into(world).spawnEntity(outerWorldEntity)
          from(world).getEntitiesCount shouldBe 1
        "have components with spawnEntityWith" in:
          val entity = outerWorldEntity
          into(world).spawnEntityWith(entity, position, speed)
          from(world).getEntitiesCount shouldBe 1
          from(world).getComponents.of(entity) shouldBe Set(
            position,
            speed
          )
      "allows to add new components with addComponent" in:
        val entity = into(world).spawnNewEntityWith(position)
        into(world).addComponent(speed).to(entity)
        from(world).getComponents.of(entity) should have size 2
        from(world).getComponents.of(entity) shouldBe Set(
          position,
          speed
        )
      "allows to include systems" in:
        into(world).includeSystem(MovementSystem())
        val entity = into(world).spawnNewEntityWith(position, speed)
        for _ <- 1 to 100 do update(world)
        from(world).getComponent[Position].of(entity) shouldBe Some(
          Position(100, 100)
        )
    "provide a reset operator" which:
      "allows to remove all entities" in:
        List.fill(100)(into(world).spawnNewEntity)
        reset(world)
        from(world).getEntitiesCount shouldBe 0
    "provide an update operator" which:
      "allows to execute the systems" in:
        into(world).includeSystem(SpawnEntities())
        for _ <- 1 to 100 do update(world)
        from(world).getEntitiesCount shouldBe 100
    "provide an emptyEntity operator" which:
      "allows to create an outer world entity" in:
        val entity = outerWorldEntity
        entity shouldBe a[Entity]
        from(world).getEntitiesCount shouldBe 0
