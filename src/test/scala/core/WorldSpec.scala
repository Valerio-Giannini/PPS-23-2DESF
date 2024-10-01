package core

import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class WorldSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach:

  var world: World = _

  override def beforeEach(): Unit =
    world = World()

  case class ComponentA() extends Component
  case class ComponentB() extends Component

  case class ComponentWithValue(value: Double) extends Component

  case class Position(x: Double, y: Double) extends Component
  case class Speed(vx: Double, vy: Double)  extends Component

  class IncreaseSpeedSystem extends System:

    override def update(world: World): Unit =
      for entity <- world.getEntities do
        world.getComponent[Speed](entity) match
        case Some(speed) =>
          world.addComponent(entity, speed.copy(speed.vx + 1, speed.vy + 1))
        case _ =>

  class AddEntitySystem extends System:
    override def update(world: World): Unit =
      world.createEntity()

  class MovementSystem extends System:
    override def update(world: World): Unit =
      for entity <- world.getEntities do
        world.getComponent[Position](entity) match
        case Some(pos) =>
          world.addComponent(entity, pos.copy(x = pos.x + 1, y = pos.y + 1))
        case _ =>

  class MovementWithSpeedSystem extends System:
    override def update(world: World): Unit =
      for entity <- world.getEntities do
        (world.getComponent[Position](entity), world.getComponent[Speed](entity)) match
        case (Some(pos), Some(speed)) =>
          world.addComponent(entity, pos.copy(pos.x + speed.vx, pos.y + speed.vy))
        case _ =>

  class MovementWithDecreasingSpeedSystem extends System:
    override def update(world: World): Unit =
      for entity <- world.getEntities do
        (world.getComponent[Position](entity), world.getComponent[Speed](entity)) match
        case (Some(pos), Some(speed)) =>
          world.addComponent(entity, pos.copy(pos.x + speed.vx, pos.y + speed.vy))
          world.addComponent(entity, speed.copy(speed.vx - 1, speed.vy - 1))
        case _ =>

  "A World" when:
    "initialized" should:
      "be empty" in:
        world.worldEntitiesToComponents.size shouldBe 0
      "increase the entity count when new entities are added" in:
        val entities = List.fill(100)(world.createEntity())
        world.worldEntitiesToComponents.size shouldBe 100
      "be empty after is been cleared from entities" in:
        val entities = List.fill(100)(world.createEntity())
        world.clearFromEntities()
        world.worldEntitiesToComponents.size shouldBe 0
    "managing entity" should:
      "allow adding already existing entity" in:
        val entity = Entity()
        world.addEntity(entity)
        world.worldEntitiesToComponents.size shouldBe 1
      "allow adding already existing entity with components" in:
        val entity = Entity()
        world.addEntity(entity, ComponentA(), ComponentB())
        world.worldEntitiesToComponents.size shouldBe 1
        world.worldEntitiesToComponents(entity) shouldBe Set(ComponentA(), ComponentB())
      "allow creation of an entity without components" in:
        val entity = world.createEntity()
        world.worldEntitiesToComponents.size shouldBe 1
        world.worldEntitiesToComponents(entity) shouldBe Set()
      "allow creation of an entity with multiple components" in:
        val entity = world.createEntity(ComponentA(), ComponentB())
        world.worldEntitiesToComponents(entity).size shouldBe 2
        world.worldEntitiesToComponents(entity) shouldBe Set(ComponentA(), ComponentB())
      "allow creation of an entity without duplicate components" in:
        val entity = world.createEntity(ComponentWithValue(2), ComponentWithValue(3))
        world.worldEntitiesToComponents(entity).size shouldBe 1
        world.worldEntitiesToComponents(entity) shouldBe Set(ComponentWithValue(3))
      "allow the removal of an entity" in:
        val entityA = world.createEntity()
        val entityB = world.createEntity()
        world.removeEntity(entityB)
        world.worldEntitiesToComponents.size shouldBe 1
      "allow the removal and reinsertion of an entity with its components" in:
        val entityA                = world.createEntity()
        val entityB                = world.createEntity(ComponentA(), ComponentWithValue(5))
        val e2Comp: Set[Component] = world.worldEntitiesToComponents(entityB)
        world.removeEntity(entityB)
        world.addEntity(entityB, e2Comp.toSeq*)
        world.worldEntitiesToComponents.size shouldBe 2
        world.worldEntitiesToComponents(entityB) shouldBe Set(ComponentA(), ComponentWithValue(5))
      "do nothing when trying to remove a non-existent entity" in:
        val entity         = world.createEntity()
        val outWorldEntity = Entity()
        val initialSize    = world.worldEntitiesToComponents.size
        world.removeEntity(outWorldEntity)
        world.worldEntitiesToComponents.size shouldBe initialSize
    "managing entity's components" should:
      "allow adding a new component" in:
        val entity = world.createEntity(ComponentA())
        world.addComponent(entity, ComponentB())
        world.worldEntitiesToComponents(entity).size shouldBe 2
        world.worldEntitiesToComponents(entity) shouldBe Set(ComponentA(), ComponentB())
      "allow updating an existing component" in:
        val entity = world.createEntity(ComponentWithValue(0))
        for i <- 1 to 100 do world.addComponent(entity, ComponentWithValue(i))
        world.worldEntitiesToComponents(entity) shouldBe Set(ComponentWithValue(100))
      "allow retrieval of an existing component" in:
        val entity = world.createEntity(ComponentWithValue(10))
        world.getComponent[ComponentWithValue](entity) shouldBe Some(ComponentWithValue(10))
      "allow remove an existing component" in:
        val entity = world.createEntity(ComponentA(), ComponentB())
        world.removeComponent(entity, ComponentB())
        world.worldEntitiesToComponents(entity).size shouldBe 1
        world.worldEntitiesToComponents(entity) shouldBe Set(ComponentA())
      "do nothing when trying to remove a component that does not exist" in:
        val entity = world.createEntity(ComponentA())
        world.removeComponent(entity, ComponentB())
        world.worldEntitiesToComponents(entity) shouldBe Set(ComponentA())
      "do nothing when retrieval of an existing component" in:
        val entity = world.createEntity(ComponentA())
        world.getComponent[ComponentB](entity) shouldBe None
    "managing non-existent entity" should:
      "throw an exception when adding a component" in:
        val entity = Entity()
        an[IllegalArgumentException] should be thrownBy world.addComponent(entity, ComponentA())
      "throw an exception when removing a component" in:
        val entity = Entity()
        an[IllegalArgumentException] should be thrownBy world.removeComponent(entity, ComponentA())
      "throw an exception when retrieval a component" in:
        val entity = Entity()
        an[IllegalArgumentException] should be thrownBy world.getComponent[ComponentA](entity)
    "managing a system" should:
      "allow adding a system" in:
        world.addSystem(IncreaseSpeedSystem())
        val entity = world.createEntity(Speed(0, 0))
        for _ <- 1 to 100 do world.update()
        world.getComponent[Speed](entity) shouldBe Some(Speed(100, 100))
      "allow the system to add new entities" in:
        world.addSystem(AddEntitySystem())
        for _ <- 1 to 100 do world.update()
        world.worldEntitiesToComponents.size shouldBe 100
      "allow updating with multiple systems" in:
        world.addSystem(IncreaseSpeedSystem())
        world.addSystem(MovementSystem())
        val entity = world.createEntity(Position(5, 5), Speed(0, 0))
        val tics   = 100
        for _ <- 1 to tics do world.update()
        world.getComponent[Speed](entity) shouldBe Some(Speed(tics, tics))
        world.getComponent[Position](entity) shouldBe Some(Position(5 + tics, 5 + tics))
      "allow updating multiple entities" in:
        world.addSystem(MovementSystem())
        val entityA = world.createEntity(Position(5, 5))
        val entityB = world.createEntity(Position(10, 10))
        val tics    = 100
        for _ <- 1 to tics do world.update()
        world.getComponent[Position](entityA) shouldBe Some(Position(5 + tics, 5 + tics))
        world.getComponent[Position](entityB) shouldBe Some(Position(10 + tics, 10 + tics))
      "allow updating a component using multiple components" in:
        world.addSystem(MovementWithSpeedSystem())
        val entity = world.createEntity(Position(0, 0), Speed(2, 2))
        val tics   = 100
        for _ <- 1 to tics do world.update()
        world.getComponent[Position](entity) shouldBe Some(Position(tics * 2, tics * 2))
      "allow updating multiple components" in:
        world.addSystem(MovementWithDecreasingSpeedSystem())
        val entity = world.createEntity(Position(0, 0), Speed(10, 10))
        world.update()
        world.getComponent[Position](entity) shouldBe Some(Position(10, 10))
        world.getComponent[Speed](entity) shouldBe Some(Speed(9, 9))
      "do nothing if entity doesn't have all the necessary components" in:
        world.addSystem(MovementWithSpeedSystem())
        val entity = world.createEntity(Position(0, 0), ComponentWithValue(5))
        val tics   = 100
        for _ <- 1 to tics do world.update()
        world.getComponent[Position](entity) shouldBe Some(Position(0, 0))
