package mvc.controller

import mvc.view.*

trait Controller:
  def start(): Unit
  def end(): Unit
  def setParamsView(paramsView: ParamsView): Unit
  def setSimulationView(simulationView: SimulationView): Unit

