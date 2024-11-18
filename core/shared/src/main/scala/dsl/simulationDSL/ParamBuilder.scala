package dsl.simulationDSL

import mvc.model.{Parameter, Simulation, ViewParameter}

/**
 * Trait for configuring simulation parameters.
 * Provides methods to define constraints and labels for a parameter before adding it to the simulation.
 *
 * Operations:
 *
 * Sets both the minimum and maximum values for the parameter.
 * {{{
 *   simulation askParam (param) withRange (0,2) withLabel (label)
 * }}}
 *
 * Sets the minimum value for the parameter.
 * {{{
 *   simulation askParam (param) withMin (2) withLabel (label)
 * }}}
 *
 * Sets the maximum value for the parameter.
 * {{{
 *   simulation askParam (param) withMax (2) withLabel (label)
 * }}}
 *
 * Assigns a label to the parameter and finalizes its configuration.
 * {{{
 *   simulation askParam (param) withLabel (label)
 * }}}
 */
trait ParamBuilder:
  /**
   * Sets both the minimum and maximum values for the parameter.
   *
   * @param min the minimum allowed value for the parameter.
   * @param max the maximum allowed value for the parameter.
   * @return a new instance of `ParamBuilder` with the updated range.
   */
  def withRange(min: AnyVal, max: AnyVal): ParamBuilder

  /**
   * Sets the minimum value for the parameter.
   *
   * @param min the minimum allowed value for the parameter.
   * @return a new instance of `ParamBuilder` with the updated minimum value.
   */
  def withMin(min: AnyVal): ParamBuilder

  /**
   * Sets the maximum value for the parameter.
   *
   * @param max the maximum allowed value for the parameter.
   * @return a new instance of `ParamBuilder` with the updated maximum value.
   */
  def withMax(max: AnyVal): ParamBuilder

  /**
   * Assigns a label to the parameter and finalizes its configuration.
   * This method integrates the parameter into the simulation's parameter system.
   *
   * @param label the label for the parameter.
   */
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
