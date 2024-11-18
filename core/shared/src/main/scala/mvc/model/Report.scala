package mvc.model

case class Point(x: AnyVal, y: AnyVal)

trait DataTracker:
  private var data: List[Point] = List.empty

  def add(x: AnyVal, y: AnyVal): Unit =
    data = data :+ Point(x, y)

  def points: List[Point] = data

case class ReportEntry(label: String, labelX: Option[String] = None, labelY: Option[String] = None, var points: List[Point] = List.empty)


/**
 * Trait for managing simulation reports.
 * Provides methods to store and retrieve report entries associated with simulation data.
 */
trait Report:
  /**
   * Retrieves the list of all report entries.
   * Each entry contains data points that are updated dynamically based on their associated data tracker.
   *
   * @return a list of `ReportEntry` objects representing the simulation report data.
   */
  def data: List[ReportEntry]

  /**
   * Adds a new entry to the report.
   * Each entry is linked to a specific data tracker, which provides the points associated with the entry.
   *
   * @param data the `DataTracker` object supplying the data points for the report entry.
   * @param entry the `ReportEntry` object to be added to the report.
   */
  def addReportEntry(data: DataTracker, entry: ReportEntry): Unit

object Report:
  def apply(): Report = new ReportImpl

  private class ReportImpl extends Report:
    private var report: List[(DataTracker, ReportEntry)] = List.empty

    override def addReportEntry(data: DataTracker, entry: ReportEntry): Unit =
      report = report :+ (data, entry)


    override def data: List[ReportEntry] =
      report.map((data, entry) => 
        entry.points = data.points
        entry
      )
