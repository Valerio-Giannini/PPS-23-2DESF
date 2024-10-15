package bouncing_ball

import view.{WorldJS, RenderWorld}
import view.model.Entity
import com.raquo.laminar.api.L.*
import org.scalajs.dom

object Simulation:

  // Inizializziamo WorldJS invece di World
  val world: WorldJS = WorldJS()

  // Funzione per inizializzare il mondo
  private def initializeWorld(): Unit =
    val entity1 = world.createEntity(Position(0, 0), Speed(1, 1))
    val entity2 = world.createEntity(Position(500, 500), Speed(-1, -1))
    // Aggiungiamo i sistemi come prima
    world.addSystem(MovementSystem())
    world.addSystem(CollisionSystem())

  // Funzione per avviare la simulazione
  private def start(): Unit =
    initializeWorld()

    EventStream.periodic(50).take(1000).foreach { _ =>
      world.update()
      val newEntities = updateEntities()
      entitiesVar.set(newEntities)
    }(unsafeWindowOwner)

  // Observable che contiene la lista delle posizioni delle entità
  private val entitiesVar = Var[List[(Entity.ID, (Double, Double))]](List.empty)

  // Funzione per aggiornare le entità con le loro nuove posizioni
  private def updateEntities(): List[(Entity.ID, (Double, Double))] =
    val updatedEntities = world.getEntities.map { entity =>
      val position = world.getComponent[Position](entity).getOrElse(Position(0, 0))
      (entity.id, (position.x, position.y))
    }
    println(s"Updated entities: $updatedEntities")
    updatedEntities


    // Funzione per ottenere l'observable delle entità (pubblica)
  def entitiesSignal: Signal[List[(Entity.ID, (Double, Double))]] = entitiesVar.signal

  // Funzione per avviare la simulazione
  def runSimulation(): Unit =
    // Inizializziamo il mondo una sola volta
    val worldDiv = RenderWorld.renderWorld(entitiesSignal)
    render(dom.document.getElementById("simulation-container"), worldDiv)

    start()



