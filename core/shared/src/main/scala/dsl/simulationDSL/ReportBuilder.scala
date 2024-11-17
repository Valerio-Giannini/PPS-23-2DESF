package dsl.simulationDSL

import mvc.model.{DataTracker, ReportEntry, Simulation}

trait ReportBuilder:
  def withXLabel(axisX: String): ReportBuilder

  def withYLabel(axisY: String): ReportBuilder

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
