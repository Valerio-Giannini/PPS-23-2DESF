package bouncing_ball

import core.{Component, ComponentTag}

case class Position(x: Double, y: Double) extends Component
case class Speed(vx: Double, vy: Double)  extends Component

val POSITION = ComponentTag[Position]
val SPEED = ComponentTag[Speed]