package bouncing_ball

import core.{World, Entity}

import view.RenderWorld
import com.raquo.laminar.api.L.*

object Simulation:
  
  val world: World = World()
  
  private def initializeWorld(): Unit =
    val entity1 = world.createEntity(Position(0, 0), Speed(1, 1))
    val entity2 = world.createEntity(Position(10, 10), Speed(-1, -1))
    //RenderWorld.renderWorld()
    world.addSystem(MovementSystem())
    world.addSystem(CollisionSystem())

  private def start(): Unit =
    initializeWorld()

    for tick <- 1 to 10 do
      world.update()
      // Crea la lista delle entità con la loro posizione aggiornata
      val newEntities = updateEntities()
      // Aggiorna la variabile osservabile con le nuove posizioni
      entitiesVar.set(newEntities)
      RenderWorld.renderWorld(entitiesSignal)

  // Observable che contiene la lista delle posizioni delle entità
  private val entitiesVar = Var[List[(Entity.ID, (Double, Double))]](List.empty)
  private def updateEntities(): List[(Entity.ID, (Double, Double))] =
    world.getEntities.map:
      entity => val position = world.getComponent[Position](entity).getOrElse(Position(0, 0))
      (entity.id, (position.x, position.y))
  
  // Funzione per ottenere l'observable delle entità (pubblica)
  private def entitiesSignal: Signal[List[(Entity.ID, (Double, Double))]] = entitiesVar.signal

  // Funzione per iniziare la simulazione
  def runSimulation(): Unit =
    start()

