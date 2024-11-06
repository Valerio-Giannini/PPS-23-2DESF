package mvc

import bouncing_ball.Position
import bouncing_ball.Simulation.{entitiesSignal, entitiesVar, updateEntities, world}
import com.raquo.airstream.core.EventStream
import com.raquo.laminar.api.L.*
import core.ComponentTag
import org.scalajs.dom
import view.init.ViewParameter

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

trait Controller:
  def startSimulation(): Unit
  def stopSimulation(): Unit

class SimulationController extends Controller:

  override def startSimulation(): Unit =
    val entities = ViewParameter(
      label = Some("Numero di entità"),
      value = 1, // Imposta qui il valore iniziale
      minValue = Some(1)
    )
    val posX = ViewParameter(
      label = Some("Posizione Asse X"),
      value = 0, // Imposta qui il valore iniziale
      minValue = Some(0),
      maxValue = Some(500)
    )
    val posY = ViewParameter(
      label = Some("Posizione Asse Y"),
      value = 0, // Imposta qui il valore iniziale
      minValue = Some(0),
      maxValue = Some(500)
    )

    val entitiesConfigurations: Future[List[(String, AnyVal)]] =
      ConfigureParam.configureParameters(entities :: posX :: posY :: Nil)

    entitiesConfigurations.foreach { configurations =>
      val entityCount = configurations.collectFirst { case ("Numero di entità", n: Int) => n }.getOrElse(1)
      val posX        = configurations.collectFirst { case ("Posizione Asse X", x: Double) => x }.getOrElse(0.0)
      val posY        = configurations.collectFirst { case ("Posizione Asse Y", y: Double) => y }.getOrElse(0.0)

      Simulation.init(entityCount, posX, posY)
    }
    Simulation.isRunning = true
    renderAndScheduleUpdates()
    

  private def renderAndScheduleUpdates(): Unit =
    val worldDiv = RenderWorld.renderWorld(entitiesSignal)
    render(dom.document.getElementById("simulation-container"), worldDiv)
    EventStream
      .periodic(50)
      .takeWhile(_ => Simulation.isRunning) // Continua solo finché `Simulation.running` è `true`
      .foreach { _ =>
        world.update()
        renderEntities()
      }(unsafeWindowOwner)

  //  This should be on the view trait!
  private def renderEntities() =
    world
      .entitiesWithAtLeastComponents(ComponentTag[Position])
      .toList
      .map: entity =>
        val pos = entity.get[Position].getOrElse(Position(0, 0))
        (entity.id, (pos.x, pos.y))

  override def stopSimulation(): Unit = Simulation.isRunning = false
