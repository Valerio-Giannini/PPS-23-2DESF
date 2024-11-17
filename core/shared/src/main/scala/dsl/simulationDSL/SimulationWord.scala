package dsl.simulationDSL

import mvc.model.{DataTracker, Parameter, ReportEntry, Simulation, StatisticEntry, ViewParameter}


trait SimulationWord:
  def askParam(param: Parameter[_]): ParamBuilder
  def askedParameters: List[ViewParameter]
  def updateParameters(viewParameters: Iterable[Parameter[_]]): Unit
  def runTill(condition: () => Boolean): Unit
  def shouldRun: Boolean
  def update(tick: Int): Unit
  def show(value: => AnyVal): StatBuilder
  def show(data: DataTracker): ReportBuilder
  def stats: List[StatisticEntry]
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
    override def show(value: => AnyVal): StatBuilder = StatBuilder(value)
    override def show(data: DataTracker): ReportBuilder = ReportBuilder(data)

    override def stats: List[StatisticEntry] = sim.statistics.snapshot
    override def report: List[ReportEntry] = sim.report.data
