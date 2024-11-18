package mvc.model

case class StatisticEntry(label: String, value: AnyVal)

/**
 * Trait for managing statistics in a simulation.
 * Allows the addition of dynamically calculated statistical entries and provides snapshots of their current values.
 */
trait Statistic:

  /**
   * Retrieves a snapshot of the current statistics.
   * Each statistic is evaluated to produce a `StatisticEntry` containing the label and current value.
   *
   * @return a list of `StatisticEntry` objects representing the current state of all tracked statistics.
   */
  def snapshot: List[StatisticEntry]

  /**
   * Adds a new statistic entry to the simulation.
   * The value of the statistic is calculated dynamically using the provided function.
   *
   * @param label a descriptive label for the statistic.
   * @param value a lazily evaluated function that provides the statistic's current value.
   */
  def addStatisticEntry(label: String, value: => AnyVal): Unit

object Statistic:
  def apply(): Statistic = new StatisticImpl
  private class StatisticImpl extends Statistic:
    private var statistic: List[(String, () => AnyVal)] = List.empty
  
    override def snapshot: List[StatisticEntry] =
      statistic.map((name, calculation) => StatisticEntry(name, calculation()))
  
    override def addStatisticEntry(label: String, value: => AnyVal): Unit =
      statistic = statistic :+ ((label, () => value))