package dsl.simulationDSL

import mvc.model.Simulation

/**
 * Trait that represents a builder for statistics, which allows adding statistics with labels.
 * The builder provides a method to associate a value with a label and add it to the simulation's statistics.
 *
 * Operators:
 * {{{
 *  simulation track (stat) withLabel (label)
 * }}}
 */
trait StatBuilder:
  /**
   * Associates a label with a value and adds the statistic to the simulation's statistics.
   *
   * @param label the label that identifies the statistic.
   */
  def withLabel(label: String): Unit

object StatBuilder:
  def apply(value: => AnyVal)(using simulation: Simulation): StatBuilder = new StatBuilderImpl(value)

  private class StatBuilderImpl(value: => AnyVal)(using simulation: Simulation) extends StatBuilder:
    infix def withLabel(label: String): Unit = simulation.statistics.addStatisticEntry(label, value)
