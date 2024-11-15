package mvc.view

import mvc.model.ReportEntry

trait ReportView extends View:
  def init(infos: List[ReportEntry]): Unit
