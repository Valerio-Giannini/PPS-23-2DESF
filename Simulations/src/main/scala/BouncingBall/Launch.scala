package BouncingBall

import BouncingBall.controller.SimulationController
import BouncingBall.view.SimulationViewImpl
import mvc.model.{ParameterID, Parameters, ViewParameter}

object Launch:
  def main(args: Array[String]): Unit =
    val simulation = BouncingBallSim()
    simulation.setParameters(model.Parameters())
    val controller = SimulationController(simulation)
    controller.setSimulationView(SimulationViewImpl())
    controller.start()