package BouncingBall

import BouncingBall.model.*
import core.World
import dsl.DSL.*
import mvc.model.Simulation

class BouncingBallSim extends Simulation:
  override def tick(currentTick: Int): Unit =
    given tick: Int = currentTick
    given World = world

    update(world)

    AvgSpeed add Stats.calcAvgSpeed
    MovingBalls add Stats.numberOfMovingBalls



