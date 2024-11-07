/*//package bouncing_ball
//
//
//import dsl.DSL.*
//
//object Simulation:
//  private val world = newWorld
//
//  def initializeWorld(): Unit =
//    into(world).spawnNewEntityWith(Position(0, 0), Speed(1, 1))
//    into(world).spawnNewEntityWith(Position(10, 10), Speed(-1, -1))
//
//    into(world).include(MovementSystem())
//    into(world).include(CollisionSystem())
//    into(world).include(PrintPositionAndSpeedOfEntitiesSystem())
//
//  def start(): Unit =
//    initializeWorld()
//
//    for tick <- 1 to 10 do
//      println(s"Tick $tick")
//      world.update()
//
//  @main def runSimulation(): Unit =
//    start()


package bouncing_ball

import bouncing_ball.{Position, Speed}
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
    Iterable.fill(entitiesNumber):
      val entity = world.createEntity()
      world.addComponent(entity, Position(posX, posY))
      world.addComponent(entity, Speed(randomSpeed()._1, randomSpeed()._2))
      entity

  def randomSpeed(): (Double, Double) =
    val speedX = 1
    val speedY = 1
    (speedX, speedY)

  // Funzione per avviare la simulazione
  private def start(): Unit =
  
    SimulationViewImpl.renderSim(world.entities)
    EventStream.periodic(50)
      .takeWhile(_ => running)
      .foreach { _ =>
        val newEntities = world.entities
        SimulationViewImpl.update(newEntities)
        world.update()
      }(unsafeWindowOwner)

  def stop(): Unit =
    running = false


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
      val entitiesConfigurations: Future[Iterable[(String, AnyVal)]] = ParamsViewImpl.init(entities:: posX:: posY:: Nil)
      // Gestiamo il risultato asincrono con `map`
      entitiesConfigurations.foreach { configurations =>
        val entityCount = configurations.collectFirst { case ("Numero di entità", n: Int) => n }.getOrElse(1)
        val initialPosX = configurations.collectFirst { case ("Posizione Asse X", x: Double) => x }.getOrElse(0.0)
        val initialPosY = configurations.collectFirst { case ("Posizione Asse Y", y: Double) => y }.getOrElse(0.0)

        // Inizializza il mondo con il numero di entità configurato e le posizioni specificate
        initializeWorld(entityCount, initialPosX, initialPosY)
      }

      start() // Avvia la simulazione
    }*/