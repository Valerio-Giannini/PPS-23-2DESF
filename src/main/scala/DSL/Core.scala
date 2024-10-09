package DSL

import core.*
import scala.reflect.ClassTag

object outerWorldDSL:
  def emptyEntity: entityOfOuterWorld = entityOfOuterWorld()

case class entityOfOuterWorld():
  def create: Entity = Entity()

object worldDSL:
  def from(world: World): fromWorld = fromWorld(world)
  def on(world: World): onWorld = onWorld(world)

case class fromWorld(world: World):
  def getEntitiesCount: Int = world.worldEntitiesToComponents.size
  def reset(): Unit = world.clearFromEntities()

  def removeEntity(entity: Entity): Unit = world.removeEntity(entity)
  def getComponents: componentActions[Entity, Set[Component]] =
    componentActions(world.worldEntitiesToComponents)

  def getComponent[T <: Component : ClassTag]: componentActions[Entity, Option[T]] =
    componentActions(world.getComponent[T])

  def removeComponent(component:Component): componentActions[Entity, Unit] =
    componentActions(utils.removeComponent(world, component))

case class onWorld(world: World):
  def createEntity: Entity = world.createEntity()
  def createEntity(components: Component*): Entity = world.createEntity(components.toSeq*)
  def addEntity(entity: Entity, components: Component*): Unit = utils.addEntity(world, entity, components.toSet)
  def addEntity(entity: Entity, components: Set[Component]): Unit = utils.addEntity(world, entity, components) //world.addEntity(entity, components.toSeq*)

  def addComponent(component:Component): componentActions[Entity, Unit] =
    componentActions(utils.addComponent(world, component))

object utils:
  def addEntity(world: World, entity: Entity, components: Set[Component]): Unit = world.addEntity(entity, components.toSeq*)
  def addComponent(world: World, component: Component)(entity: Entity): Unit = world.addComponent(entity, component)
  def removeComponent(world: World, component: Component)(entity: Entity): Unit = world.removeComponent(entity, component)

case class componentActions[A <: Entity, B](action: A => B):
  def of(entity: A): B = action(entity)
  def to(entity: A): B = action(entity)



