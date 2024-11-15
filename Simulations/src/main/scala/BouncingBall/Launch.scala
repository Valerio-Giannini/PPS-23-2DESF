package BouncingBall

import BouncingBall.controller.SimulationController
import BouncingBall.model.Stats
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

    controller.setSimulationView(SimulationViewImpl())
    controller.start()
