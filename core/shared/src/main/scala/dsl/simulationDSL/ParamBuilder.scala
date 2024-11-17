package dsl.simulationDSL

import mvc.model.{Parameter, Simulation, ViewParameter}

trait ParamBuilder:
  def withRange(min: AnyVal, max: AnyVal): ParamBuilder
  def withMin(min: AnyVal): ParamBuilder
  def withMax(min: AnyVal): ParamBuilder
  def withLabel(label: String): Unit

object ParamBuilder:
  def apply(param: Parameter[_])(using simulation: Simulation): ParamBuilder = new ParamBuilderImpl(param)

  private class ParamBuilderImpl(param: Parameter[_], minValue: Option[AnyVal] = None, maxValue: Option[AnyVal] = None)(using simulation: Simulation) extends ParamBuilder:
    def withMin(min: AnyVal): ParamBuilder =
      maxValue match
        case None => new ParamBuilderImpl(param, minValue =Some(min))
        case _ => new ParamBuilderImpl(param, minValue = Some(min), maxValue =  maxValue)

    def withMax(max: AnyVal): ParamBuilder =
      minValue match
        case None => new ParamBuilderImpl(param, maxValue =  Some(max))
        case _ => new ParamBuilderImpl(param, minValue = minValue, maxValue = Some(max))

    def withRange(min: AnyVal, max: AnyVal): ParamBuilder =
      new ParamBuilderImpl(param, Some(min), Some(max))

    def withLabel(label: String): Unit = simulation.parameters.askParam(ViewParameter(param, label, minValue, maxValue))
