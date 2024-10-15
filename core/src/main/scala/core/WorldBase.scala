package core

/** This trait represents the base interface for World used in both JVM and JS environments. */
trait WorldBase:
  def createEntity(components: Component*): Entity
  def addEntity(entity: Entity, components: Component*): Unit
  def removeEntity(entity: Entity): Unit
  def clearFromEntities(): Unit
  def getEntities: List[Entity]
  def addComponent(entity: Entity, component: Component): Unit
  def getComponent[T <: Component](entity: Entity)(implicit tag: scala.reflect.ClassTag[T]): Option[T]
  def removeComponent(entity: Entity, component: Component): Unit
  def worldEntitiesToComponents: Map[Entity, Set[Component]]
  def addSystem(system: System): Unit
  def update(): Unit
