package BouncingBall

import BouncingBall.controller.SimulationController
import BouncingBall.model.{AvgSpeed, BallRadius, BorderSize, Deceleration, MovingBalls, Stats}
import BouncingBall.view.SimulationViewImpl
import core.World
import mvc.model.{Parameters, Simulation, ViewParameter}

object Launch:
  def main(args: Array[String]): Unit =
    val simulation = BouncingBallSim()
    val controller = SimulationController(simulation)

    simulation.parameters.askParam(ViewParameter(Deceleration.id, "Deceleration", Deceleration()))
    simulation.parameters.askParam(ViewParameter(BallRadius.id, "BallRadius", BallRadius()))
    simulation.parameters.askParam(ViewParameter(BorderSize.id, "BorderSize", BorderSize()))

    simulation.statistics.addStatisticEntry("Average speed", Stats.calcAvgSpeed(simulation.world))
    simulation.statistics.addStatisticEntry("Number of moving balls", Stats.numberOfMovingBalls(simulation.world))
    simulation.statistics.addStatisticEntry("Number of stopped balls", Stats.numberOfStoppedBalls(simulation.world))
    simulation.statistics.addStatisticEntry("dec",  Stats.dec)

    simulation.statistics.addStatisticEntry("BallRadius",  Stats.dec3)
    simulation.statistics.addStatisticEntry("BorderSize",  Stats.dec2)
    
    simulation.report.addReportEntry("Average speed", "time", "speed", AvgSpeed)
    simulation.report.addReportEntry("Moving balls","time", "number of balls", MovingBalls)

    controller.setSimulationView(SimulationViewImpl())
    controller.start()
