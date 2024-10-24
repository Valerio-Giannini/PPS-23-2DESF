package bouncing_ball

import bouncing_ball.{Position, Speed}
import coreJS.{Entity, WorldTrait}
import coreJS.WorldJS
import view.*
import com.raquo.laminar.api.L.*
import org.scalajs.dom
import scala.util.Random
import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue // Import per l'ExecutionContext globale

object Simulation:

  val world: WorldTrait = WorldJS.apply()

  // Funzione per inizializzare il mondo dopo aver ricevuto il numero di entità
  private def initializeWorld(entitiesNumber: List[(String, Int)]): Unit =
    createEntities(entitiesNumber)

    world.addSystem(MovementSystem())
    world.addSystem(CollisionSystem())

  // Funzione per creare le entità
  private def createEntities(entitiesNumber: List[(String, Int)]): List[Entity] =
    entitiesNumber.flatMap {
      case ("Entities", n) =>
        List.fill(n)(world.createEntity(Position(randomPos()._1, randomPos()._2), Speed(randomVel()._1, randomVel()._2)))
      case _ =>
        List.empty // Ignora altre coppie che non sono "Entità"
    }

  // Funzione per generare una posizione casuale
  private def randomPos(): (Double, Double) =
    val randomX: Double = Random.between(0, 500)
    val randomY: Double = Random.between(0, 500)
    (randomX, randomY)

  // Funzione per generare una posizione casuale
  private def randomVel(): (Double, Double) =
    val randomX: Double = Random.between(-1, 1)
    val randomY: Double = Random.between(-1, 1)
    (randomX, randomY)

  // Funzione per avviare la simulazione
  private def start(): Unit =
    val worldDiv = RenderWorld.renderWorld(entitiesSignal)
    render(dom.document.getElementById("simulation-container"), worldDiv)

    // Rallento la simulazione
    EventStream.periodic(50).take(1000).foreach { _ =>
      world.update()
      val newEntities = updateEntities()
      entitiesVar.set(newEntities)
    }(unsafeWindowOwner)

  private val entitiesVar = Var[List[(Entity.ID, (Double, Double))]](List.empty)

  private def updateEntities(): List[(Entity.ID, (Double, Double))] =
    world.getEntities.map { entity =>
      val position = world.getComponent[Position](entity).getOrElse(Position(0, 0))
      (entity.id, (position.x, position.y))
    }

  def entitiesSignal: Signal[List[(Entity.ID, (Double, Double))]] = entitiesVar.signal

  // Funzione per avviare la simulazione e attendere la configurazione delle entità
  def runSimulation(): Unit =
    // Chiamata alla configurazione (restituisce un Future)
    val entitiesFuture: Future[List[(String, Int)]] = ConfigureSimulation.configureSimulation("BouncingBalls")(Nil)

    // Gestiamo il risultato asincrono con `map`
    entitiesFuture.foreach: entitiesNumber =>
      initializeWorld(entitiesNumber) // Inizializza il mondo con le entità configurate
      start()                         // Avvia la simulazione






