package mvc.model

case class StatisticEntry(label: String, value: AnyVal)

trait Statistics:
  def snapshot: List[StatisticEntry]

  def addStatisticEntry(label: String, value: => AnyVal): Unit

object Statistics:
  def apply(): Statistics = new StatisticsImpl
  private class StatisticsImpl  extends Statistics:
    private var statistics: List[(String, () => AnyVal)] = List.empty
  
    override def snapshot: List[StatisticEntry] =
      statistics.map((name, calculation) => StatisticEntry(name, calculation()))
  
    override def addStatisticEntry(label: String, value: => AnyVal): Unit =
      statistics = statistics :+ ((label, () => value))