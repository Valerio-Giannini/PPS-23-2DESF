package bouncing_ball

import bouncing_ball.{Position, Speed, MovementSystem}
import core.{ComponentTag, Entity, World}
import view.*
import renderSim.*
import com.raquo.laminar.api.L.*
import org.scalajs.dom
import view.init.ViewParameter

import scala.util.Random
import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue // Import per l'ExecutionContext globale

object Simulation:

  val world: World = World.apply()
  private var running = false

  // Funzione per inizializzare il mondo dopo aver ricevuto il numero di entità e le loro posizioni
  private def initializeWorld(entitiesNumber: Int, posX: Double, posY: Double): Unit =
    createEntities(entitiesNumber, posX, posY)
    world.addSystem(MovementSystem())
    world.addSystem(CollisionSystem())

  // Funzione per creare le entità con le posizioni specificate
  private def createEntities(entitiesNumber: Int, posX: Double, posY: Double): Iterable[Entity] =
    Iterable.fill(entitiesNumber) {
      val entity = world.createEntity(Position(posX, posY), Speed(1, 1))
//      world.addComponent(entity, )
//      world.addComponent(entity2)
      entity
    }

  // Funzione per avviare la simulazione
  private def start(): Unit =
    SimulationViewImpl.renderSim(world.entities.toList, List.empty) // Rendering iniziale
    EventStream.periodic(50)
      .takeWhile(_ => running)
      .foreach { _ =>
        world.update() // Aggiorna lo stato del mondo
        val newEntities = world.entities.toList // Recupera le entità aggiornate
        SimulationViewImpl.renderNext(newEntities, List.empty) // Rendering aggiornato
        println(s"New positions: ${newEntities.map(_.get[Position])}")
      }(unsafeWindowOwner)

  def stop(): Unit =
    running = false

  // Funzione per avviare la simulazione e attendere la configurazione delle entità
  def runSimulation(): Unit =
    if !running then
      running = true
      // Creo la lista di parametri da settare
      val entitiesParam = ViewParameter("Numero di entità", 1, minValue = Some(1))
      val posX = ViewParameter("Posizione Asse X", 0.0, minValue = Some(0), maxValue = Some(500))
      val posY = ViewParameter("Posizione Asse Y", 0.0, minValue = Some(0), maxValue = Some(500))

      // Chiamata alla configurazione (restituisce un Future)
      val entitiesConfigurations: Future[Iterable[(String, AnyVal)]] = ParamsViewImpl.init(List(entitiesParam, posX, posY))

      entitiesConfigurations
        .map { configurations =>
          val entityCount = configurations.collectFirst { case ("Numero di entità", n: Int) => n }.getOrElse(1)
          val initialPosX = configurations.collectFirst { case ("Posizione Asse X", x: Double) => x }.getOrElse(0.0)
          val initialPosY = configurations.collectFirst { case ("Posizione Asse Y", y: Double) => y }.getOrElse(0.0)

          // Inizializza il mondo con il numero di entità configurato e le posizioni specificate
          initializeWorld(entityCount, initialPosX, initialPosY)

          start() // Avvia la simulazione
        }
        .recover { case e =>
          println(s"Errore nella configurazione della simulazione: ${e.getMessage}")
        }

