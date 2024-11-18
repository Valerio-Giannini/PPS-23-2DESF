package core

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

  /** Provides the set of [[ComponentTag]] corresponding to the components associated with this entity.
    *
    * @return
    *   A set containing the tags of each component within the entity.
    */
  def componentTags: Set[ComponentTag[_]]

/** A Factory for [[Entity]].
  */
object Entity:

  def apply[C <: ComponentChain: ComponentChainTag](components: C): Entity =
    val componentsMap: Map[ComponentTag[_], Component] =
      summon[ComponentChainTag[C]].tags.zip(components).toMap
    SimpleEntity(IdGenerator.nextId(), componentsMap)

  def apply[C <: Component: ComponentTag](component: C): Entity =
    val componentsMap: Map[ComponentTag[_], Component] =
      Map(summon[ComponentTag[C]] -> component)
    SimpleEntity(IdGenerator.nextId(), componentsMap)

  def apply(): Entity =
    SimpleEntity(IdGenerator.nextId(), Map.empty)

  private case class SimpleEntity(ID: Int, private val componentsMap: Map[ComponentTag[_], Component]) extends Entity:

    override def id: Int = this.ID

    override def add[C <: Component: ComponentTag](component: C): Entity =
      val newComponentsMap = componentsMap + (summon[ComponentTag[C]] -> component)
      SimpleEntity(id, newComponentsMap)

    override def get[C <: Component: ComponentTag]: Option[C] =
      componentsMap.get(summon[ComponentTag[C]]).map(_.asInstanceOf[C])

    override def remove[C <: Component: ComponentTag]: Entity =
      val newComponentsMap = componentsMap - summon[ComponentTag[C]]
      SimpleEntity(id, newComponentsMap)

    override def componentTags: Set[ComponentTag[_]] = componentsMap.keySet

  /** Internal object dedicated to generating unique identifiers for each entity.
    */
  private object IdGenerator:
    private var currentId: Int = 0

    def nextId(): Int =
      currentId = currentId + 1
      currentId
