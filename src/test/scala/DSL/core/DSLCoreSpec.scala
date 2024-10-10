package DSL

import DSL.outerWorldDSL.emptyEntity
import DSL.worldDSL.{from, on}
import core.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DSLCoreSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach:
  case class Position(x: Double, y: Double) extends Component
  case class Speed(vx: Double, vy: Double)  extends Component

  class MovementSystem extends System:
    override def update(world: World): Unit =
      for
        entity <- from(world).getEntities
        pos    <- from(world).getComponent[Position].of(entity)
        speed  <- from(world).getComponent[Speed].of(entity)
      do
        on(world)
          .addComponent(pos.copy(pos.x + speed.vx, pos.y + speed.vy))
          .to(entity)

  class CollisionSystem extends System:
    override def update(world: World): Unit =
      val entities = from(world).getEntities
      for
        i <- entities.indices
        j <- (i + 1) until entities.size
        entityA = entities(i)
        entityB = entities(j)
        posA <- from(world).getComponent[Position].of(entityA)
        posB <- from(world).getComponent[Position].of(entityB)
        if posA.x == posB.x && posA.y == posB.y
      do
        on(world).addComponent(Speed(0, 0)).to(entityA)
        on(world).addComponent(Speed(0, 0)).to(entityB)


  var world: World                = _
  var positionComponent: Position = _
  var speedComponent: Speed       = _

  override def beforeEach(): Unit =
    world = World()
    positionComponent = Position(0, 0)
    speedComponent = Speed(1, 1)

  "A World" when:
    "initialized" should:
      "be empty" in:
        from(world).getEntitiesCount shouldBe 0
      "increase the entity count when new entities are added" in:
        List.fill(100)(on(world).createEntity)
        from(world).getEntitiesCount shouldBe 100
      "be empty after is been cleared from entities" in:
        List.fill(100)(on(world).createEntity)
        from(world).reset()
        from(world).getEntitiesCount shouldBe 0
    "managing entity" should:
      "allow adding already existing entity" in:
        on(world).addEntity(emptyEntity.create)
        from(world).getEntitiesCount shouldBe 1
      "allow adding already existing entity with components" in:
        val entity = emptyEntity.create
        on(world).addEntity(entity, positionComponent, speedComponent)
        from(world).getEntitiesCount shouldBe 1
        from(world).getComponents.of(entity) shouldBe Set(
          positionComponent,
          speedComponent
        )
      "allow creation of an entity without components" in:
        val entity = on(world).createEntity
        from(world).getEntitiesCount shouldBe 1
        from(world).getComponents.of(entity) shouldBe Set()
      "allow creation of an entity without duplicate components" in:
        val entity =
          on(world).createEntity(positionComponent, positionComponent)
        from(world).getEntitiesCount shouldBe 1
        from(world).getComponents.of(entity) shouldBe Set(positionComponent)
      "allow the removal of an entity" in:
        val entityA = on(world).createEntity
        val entityB = on(world).createEntity
        from(world).removeEntity(entityB)
        from(world).getEntitiesCount shouldBe 1
      "allow the removal and reinsertion of an entity with its components" in:
        val entityA = on(world).createEntity
        val entityB = on(world).createEntity(positionComponent, speedComponent)
        val componentsOfEntityB = from(world).getComponents.of(entityB)
        from(world).removeEntity(entityB)
        on(world).addEntity(entityB, componentsOfEntityB)
        from(world).getComponents.of(entityB).size shouldBe 2
        from(world).getComponents.of(entityB) shouldBe Set(
          positionComponent,
          speedComponent
        )
      "do nothing when trying to remove a non-existent entity" in:
        on(world).createEntity
        from(world).removeEntity(emptyEntity.create)
        from(world).getEntitiesCount shouldBe 1
    "managing entity's components" should:
      "allow adding a new component" in:
        val entity = on(world).createEntity(positionComponent)
        on(world).addComponent(speedComponent).to(entity)
        from(world).getComponents.of(entity).size shouldBe 2
        from(world).getComponents.of(entity) shouldBe Set(
          positionComponent,
          speedComponent
        )
      "allow retrieval of an existing component" in:
        from(world)
          .getComponent[Position]
          .of(on(world).createEntity(positionComponent)) shouldBe Some(
          positionComponent
        )
      "allow updating an existing component" in:
        val entity = on(world).createEntity(positionComponent)
        for i <- 1 to 100 do on(world).addComponent(Position(i, i)).to(entity)
        from(world).getComponent[Position].of(entity) shouldBe Some(
          Position(100, 100)
        )
      "allow remove an existing component" in:
        val entity = on(world).createEntity(positionComponent, speedComponent)
        from(world).removeComponent(speedComponent).of(entity)
        from(world).getComponents.of(entity).size shouldBe 1
        from(world).getComponents.of(entity) shouldBe Set(positionComponent)
      "do nothing when trying to remove a component that does not exist" in:
        val entity = on(world).createEntity(positionComponent)
        from(world).removeComponent(speedComponent).of(entity)
        from(world).getComponents.of(entity).size shouldBe 1
        from(world).getComponents.of(entity) shouldBe Set(positionComponent)
      "do nothing when retrieval of a non-existing component" in:
        from(world)
          .getComponent[Speed]
          .of(on(world).createEntity(positionComponent)) shouldBe None
    "managing a system" should:
      "allow adding a system" in:
        on(world).addSystem(MovementSystem())
        val entity = on(world).createEntity(positionComponent, speedComponent)
        for _ <- 1 to 100 do on(world).update
        from(world).getComponent[Position].of(entity) shouldBe Some(
          Position(100, 100)
        )