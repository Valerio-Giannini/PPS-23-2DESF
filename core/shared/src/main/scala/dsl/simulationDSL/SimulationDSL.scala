package dsl.simulationDSL

import mvc.model.{DataTracker, Report, Simulation, Statistic}

trait SimulationDSL:
  def simulation(using sim: Simulation): SimulationWord

object SimulationDSL extends SimulationDSL:
  override def simulation(using sim: Simulation): SimulationWord = SimulationWord(sim)