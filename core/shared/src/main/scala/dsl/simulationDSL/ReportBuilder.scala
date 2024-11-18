package dsl.simulationDSL

import mvc.model.{DataTracker, ReportEntry, Simulation}


/**
 * Trait for building and configuring reports in a simulation.
 * Provides methods to set labels for the axes of a chart and to assign a general label to the report.
 *
 * Operations:
 *
 * Sets the label for the X-axis of the report's chart.
 * {{{
 *   simulation chart (chart) withYLabel (labelY) withLabel (label)
 * }}}
 *
 * Sets the label for the Y-axis of the report's chart.
 * {{{
 *   simulation chart (chart) withYLabel (labelY) withLabel (label)
 * }}}
 *
 * Sets the label of the report's chart.
 * {{{
 *   simulation chart (chart) withLabel (label)
 * }}}
 */
trait ReportBuilder:
  /**
   * Sets the label for the X-axis of the report's chart.
   *
   * @param axisX the label for the X-axis.
   * @return a new instance of `ReportBuilder` with the updated X-axis label.
   */
  def withXLabel(axisX: String): ReportBuilder

  /**
   * Sets the label for the Y-axis of the report's chart.
   *
   * @param axisY the label for the Y-axis.
   * @return a new instance of `ReportBuilder` with the updated Y-axis label.
   */
  def withYLabel(axisY: String): ReportBuilder


  /**
   * Assigns a general label to the report and finalizes its configuration.
   * This method is used to add the report entry to the simulation's report system.
   *
   * @param label the general label for the report.
   */
  def withLabel(label: String): Unit

object ReportBuilder:
  def apply(data: DataTracker)(using simulation: Simulation): ReportBuilder = new ReportBuilderImpl(data)

  private class ReportBuilderImpl(data: DataTracker, labelX: Option[String] = None, labelY: Option[String] = None)(using simulation: Simulation) extends ReportBuilder:

    override def withXLabel(axisX: String): ReportBuilder =
      labelY match
        case None => new ReportBuilderImpl(data, labelX = Some(axisX))
        case _ => new ReportBuilderImpl(data, labelX = Some(axisX), labelY = labelY)

    override def withYLabel(axisY: String): ReportBuilder =
      labelX match
        case None => new ReportBuilderImpl(data, labelY = Some(axisY))
        case _ => new ReportBuilderImpl(data, labelX = labelX, labelY = Some(axisY))

    override def withLabel(label: String): Unit = simulation.report.addReportEntry(data, ReportEntry(label, labelX, labelY))
