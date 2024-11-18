package core

import scala.annotation.targetName
import scala.compiletime.summonFrom

/** This trait represents a generic Component in an Entity Component System (ECS).
  *
  * A component represents a distinct aspect of an Entity.
  */
trait Component

object Component:

  extension [A <: Component: ComponentTag, B <: Component: ComponentTag](head: A)
    @targetName("conc")
    def :::(other: B): A ::: B ::: CNil = core.:::(head, core.:::(other, CNil))

  extension [A <: Component : ComponentTag](head: A)
    @targetName("conc_one")
    def :::(nil: CNil): A ::: CNil = core.:::(head, CNil)




