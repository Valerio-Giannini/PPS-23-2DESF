package BouncingBall

import BouncingBall.controller.SimulationController
import BouncingBall.model.{BoundaryBounceSystem, CollisionSystem, MovementSystem, Position, Speed}
import BouncingBall.model.GlobalParameters.{ballRadius, borderSize, deceleration}
import mvc.model.{DoubleParameter, IntParameter, Parameter}
//import BouncingBall.model.{AvgSpeed, BallRadius, BorderSize, Deceleration, MovingBalls, Stats}
import BouncingBall.model.{AvgSpeed, MovingBalls, Stats}
import BouncingBall.view.SimulationViewImpl
import core.World
import mvc.model.{Parameters, Simulation, ViewParameter}


import dsl.DSL.*

object Launch:
  def main(args: Array[String]): Unit =
    given sim: Simulation = BouncingBallSim()
    given World = sim.world
    
    val controller = SimulationController(sim)
    controller.setSimulationView(SimulationViewImpl())

    into(sim.world).include(MovementSystem())
    into(sim.world).include(BoundaryBounceSystem())
    into(sim.world).include(CollisionSystem())

    simulation.askParam(deceleration).withLabel("Deceleration")
    simulation.askParam(ballRadius).withMin(4).withMax(20).withLabel("ball radius")

    simulation.runTill(() => Stats.numberOfMovingBalls > 0)

    simulation.show(Stats.calcAvgSpeed).withLabel("Average speed")
    simulation.show(Stats.numberOfMovingBalls).withLabel("Number of moving balls")
    simulation.show(Stats.numberOfStoppedBalls).withLabel("Number of stopped balls")
    
    simulation.show((()=>deceleration())()).withLabel("Deceleration")
    simulation.show((()=>borderSize())()).withLabel("Border size")
    simulation.show((()=>ballRadius())()).withLabel("Ball radius")
    
    
    simulation.show(AvgSpeed).withXLabel("time").withYLabel("speed").withLabel("Average speed")
    simulation.show(MovingBalls).withXLabel("time").withYLabel("number of balls").withLabel("Moving balls")


    val positions = List(
      (-200, 100), (-150, -150), (0, 0), (100, 150), (150, -100),
      (-100, -50), (200, 0), (-50, 200), (50, -200), (0, 100)
    )

    val velocities = List(
      (4, 15), (-3, -0), (0, -4), (-0, 10), (8, 5),
      (5, -3), (-3, 5), (2, 2), (4, -5), (-5, -20)
    )

    for ((pos, vel) <- positions.zip(velocities)) do
      into(sim.world).spawnNewEntityWith(Position(pos._1, pos._2), Speed(vel._1, vel._2))

    AvgSpeed.add(0, Stats.calcAvgSpeed)
    MovingBalls.add(0, Stats.numberOfMovingBalls)

    
    controller.start()

