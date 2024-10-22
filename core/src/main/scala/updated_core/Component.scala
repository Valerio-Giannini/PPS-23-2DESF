package updated_core

import scala.reflect.ClassTag

/** This trait represents a generic Component in an Entity Component System (ECS).
  *
  * A component represents a distinct aspect of an Entity.
  */
trait Component

type ComponentTag[C <: Component] = ClassTag[C]

object ComponentTag:
  def apply[C <: Component: ClassTag]: ComponentTag[C] = summon[ClassTag[C]]