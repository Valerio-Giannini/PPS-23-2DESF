package bouncing_ball

import core.World

object Simulation:
  private val world = World()

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
      for entity <- world.getEntities do
        val position =
          world.getComponent[Position](entity).map(pos => s"Position(${pos.x}, ${pos.y})").getOrElse("No Position")
        val speed =
          world.getComponent[Speed](entity).map(speed => s"Speed(${speed.vx}, ${speed.vy})").getOrElse("No Speed")
        println(s"Entity ${entity.id}: $position, $speed")
      println("-------------------")

  @main def runSimulation(): Unit =
    start()
