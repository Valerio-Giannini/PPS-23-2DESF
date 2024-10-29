package bouncing_ball


import dsl.DSL.*

object Simulation:
  private val world = newWorld

  def initializeWorld(): Unit =
    val entity1 = world.createEntity(Position(0, 0), Speed(1, 1))
    val entity2 = world.createEntity(Position(10, 10), Speed(-1, -1))

    world.addSystem(MovementSystem())
    world.addSystem(CollisionSystem())

  def start(): Unit =
    initializeWorld()

    for tick <- 1 to 10 do
      println(s"Tick $tick")
      world.update()

  @main def runSimulation(): Unit =
    start()
