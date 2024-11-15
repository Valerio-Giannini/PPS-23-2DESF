package mvc.model

case class Point(x: AnyVal, y: AnyVal)
case class DataTrackerEntry(labelX: String, labelY: String, points: List[Point])

trait DataTracker:
  private var data: List[Point] = List.empty

  def add(x: AnyVal, y: AnyVal): Unit =
    data = data :+ Point(x, y)

  def get: List[Point] = data

case class ReportEntry(label: String, data: DataTrackerEntry)

trait Report:
  def data: List[ReportEntry]

  def addReportEntry(label: String, labelX: String, labelY: String, data: DataTracker): Unit

object Report:
  def apply(): Report = new ReportImpl

  private class ReportImpl extends Report:
    private var report: List[(String, DataTracker)] = List.empty
    private var axisLabels: (String, String) = _
    override def data: List[ReportEntry] = report.map((label, dataTracker) => ReportEntry(label, DataTrackerEntry(axisLabels._1, axisLabels._2, dataTracker.get)))

    override def addReportEntry(label: String, labelX: String, labelY: String, data: DataTracker): Unit =
      report = report :+ (label, data)
      axisLabels= (labelX, labelY)


