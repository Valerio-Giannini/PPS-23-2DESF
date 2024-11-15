package BouncingBall.model

import mvc.model.{ParameterID, Parameters}


case object Deceleration extends ParameterID

//
//object accessPAram:
//  import Parameters.given
//  extension (parameters: Parameters)
//    def deceleration: Double = parameters.retrieveParamValue[Double](Deceleration)