package renderSim

import bouncing_ball.Position
import view.{ReportViewImpl, View}
import core.Entity

import scala.concurrent.{Future, Promise}
import com.raquo.laminar.api.L.*
import org.scalajs.dom
import view.sim.RenderEntity.renderEntity


trait SimulationView:
  def renderSim(entities: Iterable[Entity], statsInfos: List[(String, AnyVal)]): Unit  //, Iterable[Stat] da aggiungere quando verranno considerate

object SimulationViewImpl extends SimulationView:

  override def renderSim(entities: Iterable[Entity], statsInfos: List[(String, AnyVal)]): Unit =  //, Iterable[Stat] da aggiungere quando verranno considerate

    val container = dom.document.getElementById("simulation-container")
    if container != null then
     // view.ViewImpl.close(container) // Svuota il contenuto esistente
      val worldDiv = renderWorld(entities, statsInfos)
      view.ViewImpl.show(container, worldDiv)
    else
      val worldDiv = renderWorld(entities, statsInfos)
      view.ViewImpl.show(container, worldDiv)

  private def renderWorld(entities: Iterable[Entity], statsInfos: List[(String, AnyVal)]): Div =
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
      ),
      // Richiama la funzione `stats` per visualizzare le statistiche
      stats(statsInfos)
    )
    
  private def stats(infos: List[(String, AnyVal)]): Div =
    div(
      position := "absolute",
      bottom := "0px", // Posiziona il riquadro in basso
      right := "0px", // Posiziona il riquadro a destra
      width := "200px",
      padding := "10px",
      backgroundColor := "rgba(0, 0, 0, 0.7)", // Sfondo semi-trasparente per leggibilità
      color := "white", // Colore del testo per contrastare lo sfondo
      borderRadius := "8px",
      border := "1px solid #ccc",
      fontSize := "12px",
      overflowY := "auto", // Permette lo scrolling verticale se necessario
      children <-- Val(
        infos.map { case (key, value) =>
          div(
            s"$key = $value",
            marginBottom := "5px" // Spaziatura tra le righe
          )
        }
      )
    )

