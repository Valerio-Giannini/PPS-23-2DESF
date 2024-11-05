package bouncing_ball

import bouncing_ball.{Position, Speed}
import core.{Entity, World}
import core.World
import view.*
import com.raquo.laminar.api.L.*
import org.scalajs.dom
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
  private def createEntities(entitiesNumber: Int, posX: Double, posY: Double): List[Entity] =
    List.fill(entitiesNumber) {
      val entity = world.createEntity()
      world.addComponent(entity, Position(posX, posY))
      world.addComponent(entity, Speed(randomSpeed()._1, randomSpeed()._2))
      entity
    }

  def randomSpeed(): (Double, Double) = {
    val speedX = -1 + (2) * Random.nextDouble()
    val speedY = -1 + (2) * Random.nextDouble()
    (speedX, speedY)
  }

  // Funzione per avviare la simulazione
  private def start(): Unit =
    val worldDiv = RenderWorld.renderWorld(entitiesSignal)
    render(dom.document.getElementById("simulation-container"), worldDiv)

    EventStream.periodic(50)
      .takeWhile(_ => running) // Continua solo finché `running` è `true`
      .foreach { _ =>
        world.update()
        val newEntities = updateEntities()
        entitiesVar.set(newEntities)
      }(unsafeWindowOwner)

  def stop(): Unit =
    running = false


  private val entitiesVar = Var[List[(Entity.ID, (Double, Double))]](List.empty)

  private def updateEntities(): List[(Entity.ID, (Double, Double))] =
    world.getEntities.map { entity =>
      val position = world.getComponent[Position](entity).getOrElse(Position(0, 0))
      (entity.id, (position.x, position.y))
    }

  def entitiesSignal: Signal[List[(Entity.ID, (Double, Double))]] = entitiesVar.signal

  // Funzione per avviare la simulazione e attendere la configurazione delle entità
  def runSimulation(): Unit =
    if (!running) {
      running = true
      // Creo la lista di parametri da settare
      val entities = ViewParameter(
        label = Some("Numero di entità"),
        value = 1, // Imposta qui il valore iniziale, ad esempio 0 o un altro valore di default
        minValue = Some(1)
      )
      val posX = ViewParameter(
        label = Some("Posizione Asse X"),
        value = 0, // Imposta qui il valore iniziale, ad esempio 0 o un altro valore di default
        minValue = Some(0),
        maxValue = Some(500)
      )
      val posY = ViewParameter(
        label = Some("Posizione Asse Y"),
        value = 0, // Imposta qui il valore iniziale, ad esempio 0 o un altro valore di default
        minValue = Some(0),
        maxValue = Some(500)
      )

      // Chiamata alla configurazione (restituisce un Future)
      val entitiesConfigurations: Future[List[(String, AnyVal)]] = ConfigureParam.configureParameters(entities :: posX :: posY :: Nil)

      // Gestiamo il risultato asincrono con `map`
      entitiesConfigurations.foreach { configurations =>
        val entityCount = configurations.collectFirst { case ("Numero di entità", n: Int) => n }.getOrElse(1)
        val initialPosX = configurations.collectFirst { case ("Posizione Asse X", x: Double) => x }.getOrElse(0.0)
        val initialPosY = configurations.collectFirst { case ("Posizione Asse Y", y: Double) => y }.getOrElse(0.0)

        // Inizializza il mondo con il numero di entità configurato e le posizioni specificate
        initializeWorld(entityCount, initialPosX, initialPosY)
      }

      start() // Avvia la simulazione
    }






