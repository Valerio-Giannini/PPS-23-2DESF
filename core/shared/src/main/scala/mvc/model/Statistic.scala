package mvc.model

case class StatisticEntry(label: String, value: AnyVal)

trait Statistic:
  def snapshot: List[StatisticEntry]

  def addStatisticEntry(label: String, value: => AnyVal): Unit

object Statistic:
  def apply(): Statistic = new StatisticImpl
  private class StatisticImpl extends Statistic:
    private var statistic: List[(String, () => AnyVal)] = List.empty
  
    override def snapshot: List[StatisticEntry] =
      statistic.map((name, calculation) => StatisticEntry(name, calculation()))
  
    override def addStatisticEntry(label: String, value: => AnyVal): Unit =
      statistic = statistic :+ ((label, () => value))