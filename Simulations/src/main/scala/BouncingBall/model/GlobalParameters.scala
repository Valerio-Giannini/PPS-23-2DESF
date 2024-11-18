package BouncingBall.model

import mvc.model.{DoubleParameter, IntParameter}


object GlobalParameters:
  val deceleration =  DoubleParameter(0.01)
  val borderSize =  IntParameter(290)
  val ballRadius =  IntParameter(12)
