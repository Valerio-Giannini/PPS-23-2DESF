package mvc.model

case class DataTrackerEntry(x: AnyVal, y: AnyVal)

trait DataTracker:
  private var data: List[DataTrackerEntry] = List.empty

  def add(x: AnyVal, y: AnyVal): Unit =
    data = data :+ DataTrackerEntry(x,y)

  def get: List[DataTrackerEntry] = data

case class ReportEntry(label: String, value: List[DataTrackerEntry])

trait Report:
  def data: List[ReportEntry]
  def addReportEntry(label: String, value: DataTracker): Unit

object Report extends Report:
  private var report: List[(String, DataTracker)] = List.empty

  override def data: List[ReportEntry] = report.map((label, dataTracker) => ReportEntry(label, dataTracker.get))
  override def addReportEntry(label: String, data: DataTracker): Unit  =
    report = report :+ (label, data)
