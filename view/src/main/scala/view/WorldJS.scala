package view

import model.{Entity, Component, System, WorldBase}

/** This trait represents the core of the ECS (Entity-Component-System) architecture.
 *
 * A [[WorldJS]] manages entities, components, and systems for Scala.js.
 */
trait WorldJS extends WorldBase:
  def createEntity(components: Component*): Entity
  def addEntity(entity: Entity, components: Component*): Unit
  def removeEntity(entity: Entity): Unit
  def clearFromEntities(): Unit
  def getEntities: List[Entity]
  def addComponent(entity: Entity, component: Component): Unit
  def getComponent[T <: Component](entity: Entity)(implicit
                                                   tag: scala.reflect.ClassTag[T]
  ): Option[T]
  def removeComponent(entity: Entity, component: Component): Unit
  def worldEntitiesToComponents: Map[Entity, Set[Component]]
  def addSystem(system: System): Unit
  def update(): Unit

/** Factory for creating a new instance of [[WorldJS]]. */
object WorldJS:
  def apply(): WorldJS = new WorldJSImpl()

  private class WorldJSImpl extends WorldJS with WorldBase:
    private var entitiesToComponents: Map[Entity, Set[Component]] = Map()
    private var systems: List[System]                             = List()

    def createEntity(components: Component*): Entity =
      val entity = Entity()
      entitiesToComponents += (entity -> Set())
      components.foreach(addComponent(entity, _))
      entity

    def addEntity(entity: Entity, components: Component*): Unit =
      entitiesToComponents += (entity -> Set())
      components.foreach(addComponent(entity, _))

    def removeEntity(entity: Entity): Unit =
      entitiesToComponents -= entity

    def clearFromEntities(): Unit =
      entitiesToComponents = Map()

    def getEntities: List[Entity] = entitiesToComponents.keys.toList

    def addComponent(entity: Entity, component: Component): Unit =
      entitiesToComponents.get(entity) match
        case Some(components) =>
          val updatedComponents = components.filterNot(_.getClass == component.getClass) + component
          entitiesToComponents += (entity -> updatedComponents)
        case None => throw new IllegalArgumentException("Entity does not exist")

    def getComponent[T <: Component](
                                      entity: Entity
                                    )(implicit tag: scala.reflect.ClassTag[T]): Option[T] =
      entitiesToComponents.get(entity) match
        case Some(existingComponents) =>
          existingComponents.collectFirst { case component: T => component }
        case None => throw new IllegalArgumentException("Entity does not exist")

    def removeComponent(entity: Entity, component: Component): Unit =
      entitiesToComponents.get(entity) match
        case Some(existingComponents) =>
          val updatedComponents = existingComponents.filterNot(_ == component)
          entitiesToComponents += (entity -> updatedComponents)
        case None => throw new IllegalArgumentException("Entity does not exist")

    def worldEntitiesToComponents: Map[Entity, Set[Component]] = entitiesToComponents

    def addSystem(system: System): Unit =
      systems = system :: systems

    def update(): Unit =
      systems.foreach(_.update(this))
