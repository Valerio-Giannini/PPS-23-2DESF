package bouncing_ball

import coreJS.{System, WorldTrait}

// Movement system: updates position using speed
class MovementSystem extends System:

  override def update(world: WorldTrait): Unit =
    for entity <- world.getEntities do
      (world.getComponent[Position](entity), world.getComponent[Speed](entity)) match
      case (Some(pos), Some(speed)) =>
        world.addComponent(entity, pos.copy(pos.x + speed.vx, pos.y + speed.vy))
      case _ =>

// Collision system: if entities collide, invert the speed
class CollisionSystem extends System:

  private val collisionThreshold = 10.0 // Soglia per la collisione tra entità
  private val worldWidth = 500.0 // Larghezza del mondo
  private val worldHeight = 500.0 // Altezza del mondo

  override def update(world: WorldTrait): Unit =
    val entities = world.getEntities

    // Verifica le collisioni tra entità
    for i <- entities.indices do
      for j <- (i + 1) until entities.size do
        val entityA = entities(i)
        val entityB = entities(j)

        // Ottieni le posizioni delle entità
        (world.getComponent[Position](entityA), world.getComponent[Position](entityB)) match
          case (Some(posA), Some(posB)) =>
            // Calcola la distanza tra le due entità
            val distance = math.sqrt(math.pow(posA.x - posB.x, 2) + math.pow(posA.y - posB.y, 2))

            // Se la distanza è inferiore o uguale alla soglia di collisione
            if distance <= collisionThreshold then
              println(s"Collision detected between Entity ${entityA.id} and Entity ${entityB.id}")

              // Inverti la velocità delle entità
              (world.getComponent[Speed](entityA), world.getComponent[Speed](entityB)) match
                case (Some(speedA), Some(speedB)) =>
                  // Inverte la velocità di entrambe le entità
                  world.addComponent(entityA, Speed(-speedA.vx, -speedA.vy))
                  world.addComponent(entityB, Speed(-speedB.vx, -speedB.vy))
                case _ =>
                  println(s"One or both entities lack a Speed component: ${entityA.id}, ${entityB.id}")
          case _ =>
            ()

    // Verifica la collisione con i bordi per ogni entità
    entities.foreach: entity =>
      world.getComponent[Position](entity) match
        case Some(pos) =>
          world.getComponent[Speed](entity) match
            case Some(speed) =>
              var newSpeedX = speed.vx
              var newSpeedY = speed.vy

              // Verifica la collisione con il bordo orizzontale (asse X)
              if pos.x <= 0 || pos.x >= worldWidth then
                newSpeedX = -speed.vx // Inverti la velocità lungo l'asse X

              // Verifica la collisione con il bordo verticale (asse Y)
              if pos.y <= 0 || pos.y >= worldHeight then
                newSpeedY = -speed.vy // Inverti la velocità lungo l'asse Y

              // Aggiorna la velocità se necessario
              if newSpeedX != speed.vx || newSpeedY != speed.vy then
                println(s"Entity ${entity.id} collided with the border")
                world.addComponent(entity, Speed(newSpeedX, newSpeedY))

            case None =>
              println(s"Entity ${entity.id} lacks a Speed component")
        case None =>
          println(s"Entity ${entity.id} lacks a Position component")
