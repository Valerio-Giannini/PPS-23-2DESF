package dsl.simulationDSL

import mvc.model.*

trait SimulationWord:

  /**
   * Requests a parameter to be used in the simulation.
   *
   * @param param the parameter to be requested.
   * @return a `ParamBuilder` to further configure the requested parameter.
   */
  infix def askParam(param: Parameter[_]): ParamBuilder

  /**
   * Retrieves the list of parameters that have been requested for the simulation.
   *
   * @return a list of `ViewParameter` objects representing the requested parameters.
   */
  def askedParameters: List[ViewParameter]

  /**
   * Updates the simulation with a collection of parameters provided by the user.
   *
   * @param viewParameters a collection of `Parameter` objects to update the simulation.
   */
  infix def updateParameters(viewParameters: Iterable[Parameter[_]]): Unit

  /**
   * Defines a condition for the simulation to continue running.
   * The simulation will stop when the condition evaluates to `false`.
   *
   * @param condition a function that returns a boolean, representing the condition for the simulation to run.
   */
  infix def runTill(condition: () => Boolean): Unit

  /**
   * Checks whether the simulation should continue running.
   *
   * @return `true` if the simulation should continue, `false` otherwise.
   */
  def shouldRun: Boolean

  /**
   * Updates the simulation for a specific tick.
   *
   * @param tick the current tick of the simulation.
   */
  infix def update(tick: Int): Unit

  /**
   * Tracks a value as a statistic in the simulation.
   *
   * @param value the value to be tracked.
   * @return a `StatBuilder` to configure and label the tracked statistic.
   */
  infix def track(value: => AnyVal): StatBuilder

  /**
   * Creates a chart using the given data tracker and adds it to the simulation report.
   *
   * @param data the data tracker that provides the data for the chart.
   * @return a `ReportBuilder` to configure the chart.
   */
  infix def chart(data: DataTracker): ReportBuilder

  /**
   * Retrieves the current snapshot of all tracked statistics in the simulation.
   *
   * @return a list of `StatisticEntry` objects representing the statistics.
   */
  def stats: List[StatisticEntry]

  /**
   * Retrieves the current report of the simulation.
   *
   * @return a list of `ReportEntry` objects representing the simulation report.
   */
  def report: List[ReportEntry]


object SimulationWord:
  def apply(sim: Simulation): SimulationWord = new SimulationWorldImpl(sim)

  private class SimulationWorldImpl(sim: Simulation) extends SimulationWord:
    given Simulation = sim

    override def askParam(param: Parameter[_]): ParamBuilder = ParamBuilder(param)

    override def askedParameters: List[ViewParameter] = sim.parameters.getRequestedParams

    override def updateParameters(viewParams: Iterable[Parameter[_]]): Unit = sim.parameters.parseParams(viewParams.toList)

    override def runTill(condition: () => Boolean): Unit = sim.runCondition.setPredicate(condition)

    override def shouldRun: Boolean = sim.runCondition.evaluate

    override def update(tick: Int): Unit = sim.tick(tick)

    override def track(value: => AnyVal): StatBuilder = StatBuilder(value)

    override def chart(data: DataTracker): ReportBuilder = ReportBuilder(data)

    override def stats: List[StatisticEntry] = sim.statistics.snapshot

    override def report: List[ReportEntry] = sim.report.data
