package mvc.model

case class Point(x: AnyVal, y: AnyVal)

case class DataTrackerEntry(labelX: Option[String], labelY: Option[String], points: List[Point])

trait DataTracker:
  private var data: List[Point] = List.empty

  def add(x: AnyVal, y: AnyVal): Unit =
    data = data :+ Point(x, y)

  def get: List[Point] = data

case class ReportEntry(label: String, data: DataTrackerEntry)

trait Report:
  def data: List[ReportEntry]

  def addReportEntry(label: String, data: DataTracker, labelX: Option[String] = None, labelY: Option[String] = None): Unit

object Report:
  def apply(): Report = new ReportImpl

  private class ReportImpl extends Report:
    private var report: List[(String, DataTracker)] = List.empty
    private var axisLabels: (Option[String], Option[String]) = _
    override def data: List[ReportEntry] = report.map((label, dataTracker) => ReportEntry(label, DataTrackerEntry(axisLabels._1, axisLabels._2, dataTracker.get)))

    override def addReportEntry(label: String, data: DataTracker, labelX: Option[String] = None, labelY: Option[String] = None): Unit =
      report = report :+ (label, data)
      axisLabels= (labelX, labelY)


