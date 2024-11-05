package core

import scala.reflect.ClassTag

/** This trait represents a generic Component in an Entity Component System (ECS).
  *
  * A component represents a distinct aspect of an Entity.
  */
trait Component

/** Type alias for [[ComponentTag]], a specialized ClassTag for [[Component]] types.
  */
type ComponentTag[C <: Component] = ClassTag[C]

object ComponentTag:
  def apply[C <: Component: ClassTag]: ComponentTag[C] = summon[ClassTag[C]]
