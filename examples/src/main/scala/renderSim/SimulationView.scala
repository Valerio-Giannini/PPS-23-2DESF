package renderSim

import bouncing_ball.Position
import view.{ReportViewImpl, View}
import core.Entity

import scala.concurrent.{Future, Promise}
import com.raquo.laminar.api.L.*
import org.scalajs.dom
import view.sim.RenderEntity.renderEntity


trait SimulationView:
  def renderSim(entities: Iterable[Entity]): Unit  //, Iterable[Stat] da aggiungere quando verranno considerate
  def update(entities: Iterable[Entity]): Unit
  
object SimulationViewImpl extends SimulationView:

  override def renderSim(entities: Iterable[Entity]): Unit =  //, Iterable[Stat] da aggiungere quando verranno considerate
    val worldDiv = renderWorld(entities)
    val container = dom.document.getElementById("simulation-container")
    view.ViewImpl.show(container, worldDiv)

  override def update(entities: Iterable[Entity]): Unit =
    val container = dom.document.getElementById("simulation-container")
    if container != null then
      view.ViewImpl.close(container) // Svuota il contenuto esistente
      val updatedContent = renderWorld(entities)
      view.ViewImpl.show(container, updatedContent)

  private def renderWorld(entities: Iterable[Entity]): Div =
    div(
      cls("world"),
      width := "510px", // Dimensione del mondo
      height := "510px",
      position := "relative",
      backgroundColor := "grey", // Posizionamento relativo
      border := "5px solid black",
      // Effettua il rendering di tutte le entità presenti nel mondo
      children <-- Val(
        entities.flatMap { entity =>
          entity.get[Position] match
            case Some(position) =>
              Some(renderEntity(entity.id, (position.x, position.y)))
            case None =>
              None // Ignora l'entità se la posizione non è presente
        }.toSeq
      )
    )

//  private def renderStats(Iterable [Stat]): Unit // le cose che stanno in show
