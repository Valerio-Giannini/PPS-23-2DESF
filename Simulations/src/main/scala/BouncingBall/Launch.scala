package BouncingBall

import BouncingBall.controller.SimulationController
import BouncingBall.model.{BoundaryBounceSystem, CollisionSystem, MovementSystem, Position, Speed, Dimension}
import BouncingBall.model.GlobalParameters.{ballRadius, deceleration}

import BouncingBall.model.{AvgSpeed, MovingBalls, Stats}
import core.World
import mvc.model.Simulation


import dsl.DSL.*

object Launch:
  def main(args: Array[String]): Unit =
    given sim: Simulation = BouncingBallSim()
    given World = sim.world
    
    val controller = SimulationController(sim)

    into(sim.world).include(MovementSystem())
    into(sim.world).include(BoundaryBounceSystem())
    into(sim.world).include(CollisionSystem())

    simulation.askParam(deceleration).withMin(0).withMax(0.1).withLabel("Deceleration")
    simulation.askParam(ballRadius).withMin(4).withMax(20).withLabel("Ball radius")

    simulation.runTill(() => Stats.numberOfMovingBalls > 0)

    simulation.track(Stats.calcAvgSpeed).withLabel("Average speed")
    simulation.track(Stats.numberOfMovingBalls).withLabel("Number of moving balls")
    simulation.track(Stats.numberOfStoppedBalls).withLabel("Number of stopped balls")
    
    simulation.track({deceleration()}).withLabel("Deceleration")
    
    
    simulation.chart(AvgSpeed).withXLabel("time").withYLabel("speed").withLabel("Average speed")
    simulation.chart(MovingBalls).withXLabel("time").withYLabel("number of balls").withLabel("Moving balls")

    val positions = List(
      (-200, 100), (-150, -150), (0, 0), (100, 150), (150, -100),
      (-100, -50), (200, 0), (-50, 200), (50, -200), (0, 100)
    )

    val velocities = List(
      (4, -3), (-3, -0), (0, -4), (-0, -2), (8, 5),
      (5, -3), (-3, 5), (2, 2), (4, -5), (-5, 4)
    )

    for ((pos, vel) <- positions.zip(velocities)) do
      into(sim.world).spawnNewEntityWith(Position(pos._1, pos._2), Speed(vel._1, vel._2), Dimension(ballRadius()))

    AvgSpeed.add(0, Stats.calcAvgSpeed)
    MovingBalls.add(0, Stats.numberOfMovingBalls)

    controller.start()

