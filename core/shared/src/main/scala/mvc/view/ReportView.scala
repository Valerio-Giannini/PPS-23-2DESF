package mvc.view

trait ReportView extends View:
  def report(infos: List[(String, List[(AnyVal, AnyVal)])]): Unit
