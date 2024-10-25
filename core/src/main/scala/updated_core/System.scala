package updated_core

/** This trait represents a generic System in an Entity Component System (ECS).
  *
  * Systems are responsible for processing Entities that have specific Components and applying some logics for
  * each [[World]]'s step.
  */
trait System:
  def update(world: World): Unit
