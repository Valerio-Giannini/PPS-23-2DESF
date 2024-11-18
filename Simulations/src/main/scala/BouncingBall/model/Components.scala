package BouncingBall.model

import core.{Component, ComponentTag}

case class Position(x: Double, y: Double) extends Component
case class Speed(vx: Double, vy: Double)  extends Component
case class Dimension(x: Int) extends Component

val POSITION = ComponentTag[Position]
val SPEED = ComponentTag[Speed]
val DIMENSION = ComponentTag[Dimension]