package BouncingBall

import BouncingBall.controller.SimulationController
import BouncingBall.model.{BoundaryBounceSystem, CollisionSystem, MovementSystem, Position, Speed}
import BouncingBall.model.GlobalParameters.{ballRadius, deceleration}

import BouncingBall.model.{AvgSpeed, MovingBalls, Stats}
import BouncingBall.view.SimulationViewImpl
import core.World
import mvc.model.{Simulation}


import dsl.DSL.*

object Launch:
  def main(args: Array[String]): Unit =
    given sim: Simulation = BouncingBallSim()
    given World = sim.world
    
    val controller = SimulationController(sim)

    into(sim.world) include MovementSystem()

    into(sim.world) include MovementSystem()

    into(sim.world) include BoundaryBounceSystem()
    into(sim.world) include CollisionSystem()

    simulation askParam deceleration withLabel "Deceleration"
    simulation askParam ballRadius withMin 4 withMax 20  withLabel "ball radius"

    simulation runTill {() => Stats.numberOfMovingBalls > 0}

    simulation track Stats.calcAvgSpeed withLabel "Average speed"
    simulation track Stats.numberOfMovingBalls withLabel "Number of moving balls"
    simulation track Stats.numberOfStoppedBalls withLabel "Number of stopped balls"

    simulation track {deceleration()} withLabel "Deceleration"
    
    
    simulation chart AvgSpeed withXLabel "time" withYLabel "speed" withLabel "Average speed"
    simulation chart MovingBalls withXLabel "time" withYLabel "number of balls" withLabel("Moving balls")


    val positions = List(
      (-200, 100), (200, 100)
    )

    val velocities = List(
      (1, 0), (-1, 0)
    )

    for ((pos, vel) <- positions.zip(velocities)) do
      into(sim.world) spawnNewEntityWith(Position(pos._1, pos._2), Speed(vel._1, vel._2))

    AvgSpeed add (0, Stats.calcAvgSpeed)
    MovingBalls add (0, Stats.numberOfMovingBalls)

    
    controller.start()

