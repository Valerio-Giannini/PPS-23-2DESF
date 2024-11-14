package simulation

import controller.SimulationController
import demo.mvc.view.SimulationViewImpl
import dsl.coreDSL.CoreDSL.into
import simulation.SimulationParameters.{ballRadius, borderSize, deceleration}

object BounceSimulation extends Simulation:
  override def condition: Boolean = Stats.calcAvgSpeed != 0

  override def setParams: Unit =
    deceleration = 0.05
    borderSize = 290
    ballRadius = 5

  override def initWorld: Unit =
    // maybe we should add a function where to put this block ?
    into(world).include(MovementSystem())
    into(world).include(BoundaryBounceSystem())
    into(world).include(CollisionSystem())
    into(world).include(PrintPositionAndSpeedOfEntitiesSystem())

    val positions = List(
      (-200, 100), (-150, -150), (0, 0), (100, 150), (150, -100),
      (-100, -50), (200, 0), (-50, 200), (50, -200), (0, 100)
    )

    val velocities = List(
      (10, 15), (-15, -10), (20, -20), (-10, 10), (15, 5),
      (5, -15), (-20, 20), (10, 10), (15, -5), (-5, -20)
    )
    // end block

    for ((pos, vel) <- positions.zip(velocities)) do
      into(world).spawnNewEntityWith(Position(pos._1, pos._2), Speed(vel._1, vel._2))
      println("Entity with Pos and Speed Created")

    Report.updateAvgSpeed(Stats.calcAvgSpeed)
    Report.updateMovingBalls(Stats.numberOfMovingBalls)

  override def showStats: Unit =
    println(s"Velocità media ${Stats.calcAvgSpeed}")
    println(s"Numero di entità in movimento ${Stats.numberOfMovingBalls}")
    println(s"Numero di entità ferme ${Stats.numberOfStoppedBalls}")

  override def updateReport(using current_tick: Int): Unit =
    Report.updateAvgSpeed(Stats.calcAvgSpeed)
    Report.updateMovingBalls(Stats.numberOfMovingBalls)

  override def showReport: Unit =
    Report.getReport
    println(Report.getReport)
//    Report.showAvgSpeed()
//    Report.showMovingBalls()
