package sprint_prototype

import core.World
import dsl.coreDSL.*
import sprint_prototype.Simulation.*

object Simulation:
  private val world = World()

  def initializeWorld(): Unit =
    into(world).spawnNewEntityWith(Position(0, 0), Speed(1, 1))
    into(world).spawnNewEntityWith(Position(10, 10), Speed(-1, -1))

    into(world).includeSystem(PrintPositionAndSpeedOfEntitiesSystem())
    into(world).includeSystem(CollisionSystem())
    into(world).includeSystem(MovementSystem())

  def start(): Unit =
    initializeWorld()

    for tick <- 1 to 10 do
      println(s"Tick $tick")
      update(world)

@main def runSimulation(): Unit =
  start()
