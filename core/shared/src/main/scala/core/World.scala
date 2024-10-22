package core

/** Factory for creating a new instance of [[World]].
  */
object World:
  def apply(): WorldTrait = WorldImpl()

  private case class WorldImpl() extends WorldTrait:
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
    )(using tag: scala.reflect.ClassTag[T]): Option[T] =
      entitiesToComponents
        .get(entity) match
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
