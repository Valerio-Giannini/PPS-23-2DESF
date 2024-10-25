package updated_core

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.immutable.HashMap
import scala.reflect.ClassTag

sealed trait Entity:
  def id: Int
  def add[C <: Component: ComponentTag](component: C): Entity
  def get[C <: Component: ComponentTag]: Option[C]
  def remove[C <: Component: ComponentTag]: Entity
  def componentTags: Set[ComponentTag[_]]

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

    def componentTags: Set[ComponentTag[_]] = componentsMap.keySet

  private object IdGenerator:
    private val currentId: AtomicInteger = new AtomicInteger(0)

    def nextId(): Int =
      currentId.incrementAndGet()
