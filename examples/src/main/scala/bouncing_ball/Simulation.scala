package bouncing_ball

import bouncing_ball.{Position, Speed}
import coreJS.{Entity, WorldTrait}
import coreJS.WorldJS
import view.*
import com.raquo.laminar.api.L.*
import org.scalajs.dom

object Simulation:

  val world: WorldTrait = WorldJS.apply()

  private def initializeWorld(): Unit =
    //Chiama la view per inserire il numero di entities
    val entitiesNumber = ConfigureSimulation.configureSimulation(1)("Entità":: Nil)
    //Con il ritorno dalla configuration (viene ritornata una lista di coppie: String, Int)
    //Fare ciclo per creare n entities la funzione è val entityN = createEntity(Position(0,0),Speed(1,1))
    //Come valore di position e Speed lasciare 0,0 e 1,1


    world.addSystem(MovementSystem())
    world.addSystem(CollisionSystem())

  private def start(): Unit =
    initializeWorld()
    
    val worldDiv = RenderWorld.renderWorld(entitiesSignal)
    render(dom.document.getElementById("simulation-container"), worldDiv)
    
    //Rallento la simulazione
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

  def runSimulation(): Unit =
    start()





