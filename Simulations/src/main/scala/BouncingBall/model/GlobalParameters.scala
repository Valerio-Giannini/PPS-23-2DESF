package BouncingBall.model

import mvc.model.{DoubleParameter, IntParameter}


object GlobalParameters:
  val deceleration: DoubleParameter =  DoubleParameter(0.06)
  val borderSize: IntParameter=  IntParameter(290)
  val ballRadius: IntParameter =  IntParameter(12)

