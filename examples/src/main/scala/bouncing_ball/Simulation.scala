package bouncing_ball


import dsl.DSL.*

object Simulation:
  private val world = newWorld

  def initializeWorld(): Unit =
    into(world).spawnNewEntityWith(Position(0, 0), Speed(1, 1))
    into(world).spawnNewEntityWith(Position(10, 10), Speed(-1, -1))

    into(world).include(MovementSystem())
    into(world).include(CollisionSystem())
    into(world).include(PrintPositionAndSpeedOfEntitiesSystem())

  def start(): Unit =
    initializeWorld()

    for tick <- 1 to 10 do
      println(s"Tick $tick")
      world.update()

  @main def runSimulation(): Unit =
    start()
