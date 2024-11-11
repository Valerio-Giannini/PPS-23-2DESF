package renderSim

import bouncing_ball.Position
import view.{ReportViewImpl, View}
import core.Entity
import com.raquo.laminar.api.L.*
import org.scalajs.dom
import view.sim.RenderEntity.renderEntity
import view.SimulationView

import scala.collection.mutable

object SimulationViewImpl extends SimulationView:

  // Mappa per tenere traccia delle posizioni di ciascuna entità tramite Var
  private val entityPositions: mutable.Map[Int, Var[(Double, Double)]] = mutable.Map()

  override def renderSim(entities: Iterable[Entity], statsInfos: List[(String, AnyVal)]): Unit =
    // Aggiorna o crea Var per le posizioni delle entità
    entities.foreach { entity =>
      entity.get[Position] match
        case Some(position) =>
          val positionVar = entityPositions.getOrElseUpdate(entity.id, Var((position.x, position.y)))
          positionVar.set((position.x, position.y)) // Aggiorna la posizione
        case None =>
      // Ignora l'entità se la posizione non è presente
    }

    val container = dom.document.getElementById("simulation-container")
    if container == null then
      val worldDiv = renderWorld(entities.map(e => e.id -> entityPositions(e.id).signal), statsInfos)
      view.ViewImpl.show(container, worldDiv)
    else
      renderWorld(entities.map(e => e.id -> entityPositions(e.id).signal), statsInfos)


  private def renderWorld(entitySignals: Iterable[(Int, Signal[(Double, Double)])], statsInfos: List[(String, AnyVal)]): Div =
    div(
      cls("world"),
      width := "510px", // Dimensione del mondo
      height := "510px",
      position := "relative",
      backgroundColor := "grey", // Posizionamento relativo
      border := "5px solid black",
      // Effettua il rendering dinamico di tutte le entità presenti nel mondo
      children <-- Signal.combineSeq(entitySignals.map { case (entityId, positionSignal) =>
        positionSignal.map { position =>
          renderEntity(entityId, position)
        }
      }.toSeq),
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
