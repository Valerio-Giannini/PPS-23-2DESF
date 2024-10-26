package bouncing_ball

import core.{ComponentTag, World}

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
      for entity <- world.entitiesWithAtLeastComponents(ComponentTag[Position], ComponentTag[Speed]) do
        (entity.get[Position], entity.get[Speed]) match
          case (Some(pos), Some(speed)) =>
            val positionInfo = s"Position(${pos.x}, ${pos.y})"
            val speedInfo = s"Speed(${speed.vx}, ${speed.vy})"
            println(s"Entity ${entity.id}: $positionInfo, $speedInfo")
            println("-------------------")
          case _ => println("Position Or Speed not found")

  @main def runSimulation(): Unit =
    start()
