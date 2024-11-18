package mvc.view

import mvc.model.ReportEntry

/**
 * A trait representing a view for displaying simulation reports.
 *
 * The `ReportView` extends the base `View` trait, providing functionality
 * for initializing the view with report data. This data includes points
 * to be visualized and optional labels for axes.
 */
trait ReportView extends View:

  /**
   * Initializes the report view with a list of report entries.
   *
   * This method prepares the view for rendering by setting up the provided
   * report data. 
   *
   * @param infos a list of `ReportEntry` objects containing data points and
   *              optional metadata for the report.
   */
  def init(infos: List[ReportEntry]): Unit

