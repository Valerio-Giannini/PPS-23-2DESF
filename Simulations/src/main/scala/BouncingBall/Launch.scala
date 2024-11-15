package BouncingBall

import BouncingBall.controller.SimulationController
import BouncingBall.model.{AvgSpeed, MovingBalls, Stats}
import BouncingBall.view.SimulationViewImpl
import core.World
import mvc.model.{ParameterID, Parameters, Simulation, ViewParameter}

object Launch:
  def main(args: Array[String]): Unit =
    val simulation = BouncingBallSim()
    val controller = SimulationController(simulation)

    simulation.statistics.addStatisticEntry("Average speed", Stats.calcAvgSpeed(simulation.world))
    simulation.statistics.addStatisticEntry("Number of moving balls", Stats.numberOfMovingBalls(simulation.world))
    simulation.statistics.addStatisticEntry("Number of stopped balls", Stats.numberOfStoppedBalls(simulation.world))
    simulation.report.addReportEntry("Average speed","x", "y", AvgSpeed)
    simulation.report.addReportEntry("Moving balls","x", "y", MovingBalls)

    controller.setSimulationView(SimulationViewImpl())
    controller.start()
