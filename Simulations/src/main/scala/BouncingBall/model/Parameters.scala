package BouncingBall.model

import mvc.model.Simulation

class GlobalParameter[T <: AnyVal](var value: T, val id: String):
  def apply(): T = value

object Deceleration extends GlobalParameter[Double](0, "deceleration")

object BallRadius extends GlobalParameter[Double](12, "ballRadius")

object BorderSize extends GlobalParameter[Double](290, "borderSize")

