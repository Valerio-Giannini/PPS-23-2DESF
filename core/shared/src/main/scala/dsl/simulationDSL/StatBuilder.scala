package dsl.simulationDSL

import mvc.model.Simulation

trait StatBuilder:
  def withLabel(label: String): Unit

object StatBuilder:
  def apply(value: => AnyVal)(using simulation: Simulation): StatBuilder = new StatBuilderImpl(value)

  private class StatBuilderImpl(value: => AnyVal)(using simulation: Simulation) extends StatBuilder:
    def withLabel(label: String): Unit = simulation.statistics.addStatisticEntry(label, value)
