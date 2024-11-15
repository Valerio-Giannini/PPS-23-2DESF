package BouncingBall

import BouncingBall.controller.SimulationController
import BouncingBall.model.*
import BouncingBall.view.SimulationViewImpl
import mvc.model.Simulation
import dsl.DSL.*

class BouncingBallSim extends Simulation:
  override def init(): Unit =
    into(world).include(MovementSystem())
    into(world).include(BoundaryBounceSystem())
    into(world).include(CollisionSystem())

    val positions = List(
      (-200, 100), (-150, -150), (0, 0), (100, 150), (150, -100),
      (-100, -50), (200, 0), (-50, 200), (50, -200), (0, 100)
    )

    val velocities = List(
      (4, 15), (-3, -0), (0, -4), (-0, 10), (8, 5),
      (5, -3), (-3, 5), (2, 2), (4, -5), (-5, -20)
    )
    // end block

    for ((pos, vel) <- positions.zip(velocities)) do
      into(world).spawnNewEntityWith(Position(pos._1, pos._2), Speed(vel._1, vel._2))
      println("Entity with Pos and Speed Created")

  override def tick(currentTick: Int): Unit =
    world.update()

  override def isRunning: Boolean =
    true
  //currentTick < 100 // esempio di limite

  override def endSimulation(): Unit =
    println("Simulation Ended")

  def showStats(): Unit =
    println(s"Statistiche attuali: ${world.entities.size} entità attive.")

  def showReport(): Unit =
    println(s"Report finale: ${world.entities.size} entità rimanenti.")



