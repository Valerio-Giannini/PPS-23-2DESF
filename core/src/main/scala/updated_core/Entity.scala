package updated_core

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.immutable.HashMap
import scala.reflect.ClassTag

/** This trait represents a generic Entity in an Entity Component System (ECS).
  *
  * An Entity contains components.
  */
sealed trait Entity:

  /** Retrieves the unique id associated with this entity.
    *
    * @return
    *   The id of the entity.
    */
  def id: Int

  /** Add the specified component to the [[Entity]], producing a new [[Entity]] instance that includes the updated
    * component set.
    *
    * @param component
    *   The [[Component]] to be added
    * @tparam C
    *   The type of the component, constrained to [[Component]].
    * @return
    *   A new [[Entity]] instance with the component added.
    */
  def add[C <: Component: ComponentTag](component: C): Entity

  /** Retrieves a specific [[Component]] by its type.
    *
    * @tparam C
    *   The type of the component to retrieve, constrained to [[Component]].
    * @return
    *   An `Option` containing the component if present, otherwise `None`.
    */
  def get[C <: Component: ComponentTag]: Option[C]

  /** Removes the specified component, producing a new [[Entity]] instance without that component.
    * @tparam C
    *   The type of the component to remove, constrained to [[Component]].
    * @return
    *   A new instance with the specified component removed.
    */
  def remove[C <: Component: ComponentTag]: Entity

  /**
   * Provides the set of [[ComponentTag]] corresponding to the components associated with this entity.
   *
   * @return A set containing the tags of each component within the entity.
   */
  def componentTags: Set[ComponentTag[_]]

/**
 * A Factory for [[Entity]].
 */
object Entity:
  
  def apply(components: Component*): Entity =
    val initialMap = HashMap.empty[ComponentTag[_], Component]
    val map: HashMap[ComponentTag[_], Component] = components.foldLeft(initialMap) { (acc, component) =>
      val tag = ClassTag(component.getClass)
      acc.updated(tag, component)
    }
    SimpleEntity(IdGenerator.nextId(), map)

  private case class SimpleEntity(ID: Int, private val componentsMap: Map[ComponentTag[_], Component])
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

  /**
   * Internal object dedicated to generating unique identifiers for each entity.
   */
  private object IdGenerator:
    private val currentId: AtomicInteger = new AtomicInteger(0)

    def nextId(): Int =
      currentId.incrementAndGet()
