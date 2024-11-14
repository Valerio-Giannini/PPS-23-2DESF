package BouncingBall.modelz

import mvc.model.{ParameterID, Parameters}


case object Deceleration extends ParameterID

object Dece

object Parameters:
  def apply(): Parameters = new ParametersImpl
  private class ParametersImpl extends Parameters:
    def deceleration: Double = retrieveParamValue(Deceleration)