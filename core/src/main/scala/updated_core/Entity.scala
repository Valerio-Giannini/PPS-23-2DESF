package updated_core

import scala.collection.immutable.HashMap
import scala.reflect.ClassTag

sealed trait Entity:
  def id: Int
  def add[C <: Component: ComponentTag](component: C): Entity
  def get[C <: Component: ComponentTag]: Option[C]
  def remove[C <: Component : ComponentTag]: Entity
  
object Entity:

  def apply(): Entity =
    SimpleEntity(IdGenerator.nextId(), HashMap.empty)

  def apply(components: Component*): Entity =
    val initialMap = HashMap.empty[ComponentTag[_], Component]
    val map: HashMap[ComponentTag[_], Component] = components.foldLeft(initialMap) { (acc, component) =>
      val tag = ClassTag(component.getClass)
      acc.updated(tag, component)
    }
    SimpleEntity(IdGenerator.nextId(), map)

  private final case class SimpleEntity(ID: Int, private val componentsMap: Map[ComponentTag[_], Component])
      extends Entity:

    def id: Int = this.ID

    def add[C <: Component: ComponentTag](component: C): Entity =
      val newComponentsMap = componentsMap + (summon[ComponentTag[C]] -> component)
      SimpleEntity(id, newComponentsMap)

    def get[C <: Component: ComponentTag]: Option[C] =
      componentsMap.get(summon[ComponentTag[C]]).map(_.asInstanceOf[C])

    def remove[C <: Component: ComponentTag]: Entity =
      val newComponentsMap = componentsMap - summon[ComponentTag[C]]
      SimpleEntity(id, newComponentsMap)
  

  private object IdGenerator:
    private var currentId: Int = 0

    def nextId(): Int =
      currentId += 1
      currentId
