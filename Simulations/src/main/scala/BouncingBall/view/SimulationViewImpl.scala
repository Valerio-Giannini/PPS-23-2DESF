package BouncingBall.view

//import BouncingBall.model.Position

import BouncingBall.model.{BallRadius, Position}
import com.raquo.airstream.core.Signal
import com.raquo.airstream.state.Var
import com.raquo.laminar.api.L.*
import core.Entity
import mvc.model.StatisticEntry
import mvc.view.SimulationView
import org.scalajs.dom

import scala.reflect.ClassTag

class SimulationViewImpl extends SimulationView:
  // Var per le posizioni delle entità
  private val entitiesVar = Var[Iterable[(Int, (Double, Double))]](List.empty)

  // Applica distinct per evitare aggiornamenti ridondanti
  def entitiesSignal: Signal[Iterable[(Int, (Double, Double))]] = entitiesVar.signal.distinct

  // Var per le statistiche
  private val statsVar = Var[List[StatisticEntry]](List.empty)

  // Applica distinct per evitare aggiornamenti ridondanti
  def statsSignal: Signal[List[StatisticEntry]] = statsVar.signal.distinct

  override def show(): Unit =
    val container = dom.document.getElementById("simulation-container")
    val worldDiv = renderWorld(entitiesSignal, statsSignal)
    render(container, worldDiv)

  override def close(): Unit =
    val container = dom.document.getElementById("simulation-container")
    container.innerHTML = ""

  //  // Chiamato una sola volta per renderizzare il container
  //  override def viewSim(entities: Iterable[Entity], initialStatsInfos: List[(String, AnyVal)]): Unit =
  //    // Imposta le posizioni iniziali delle entità
  //    val initialPositions = entities.flatMap { entity =>
  //      entity.get[Position].map { position =>
  //        (entity.id, (position.x, position.y))
  //      }
  //    }
  //    entitiesVar.set(initialPositions)
  //    statsVar.set(initialStatsInfos) // Imposta le statistiche iniziali

  // Chiamato per aggiornare i signal con le nuove posizioni e statistiche
  override def update(entities: Iterable[Entity], newStatsInfos: List[StatisticEntry]): Unit =
    val updatedPositions = entities.collect {
      case entity if entity.get[Position].isDefined =>
        val pos = entity.get[Position].get
        (entity.id, (pos.x, pos.y))
    }

    // Aggiorna solo se ci sono cambiamenti nelle posizioni
    if (updatedPositions != entitiesVar.now()) then
      println(s"Updating positions: $updatedPositions")
      entitiesVar.set(updatedPositions)

    // Aggiorna solo se ci sono cambiamenti nelle statistiche
    if (newStatsInfos != statsVar.now()) then
      println(s"Updating stats: $newStatsInfos")
      statsVar.set(newStatsInfos)


  // TODO : val borderSize: Int = 290

  private def renderWorld(
                           entitySignals: Signal[Iterable[(Int, (Double, Double))]],
                           statsSignal: Signal[List[StatisticEntry]]
                         ): Div = {
    val borderSize: Int = 290
    println("RenderWorld chiamato")

    div(
      cls("world"),
      width := s"${borderSize * 2}px", // Dimensione del mondo
      height := s"${borderSize * 2}px",
      position := "relative",
      backgroundColor := "#ccc", // Posizionamento relativo
      border := "5px solid black",
      // Effettua il rendering dinamico di tutte le entità presenti nel mondo
      children <-- entitySignals.map { entities =>
        println(s"Entità da renderizzare: ${entities.mkString(", ")}") // Debug per vedere le entità ricevute
        entities.toSeq.map { case (entityId, position) =>
          println(s"Rendering entità ID: $entityId, Posizione: $position") // Debug per ogni entità
          renderEntity(entityId, position)
        }
      },
      // Richiama la funzione `stats` per visualizzare le statistiche come Signal
      child <-- statsSignal.map { statsList =>
        println(s"Rendering statistiche: ${statsList.mkString(", ")}") // Debug per tracciare le statistiche
        stats(statsList)
      }
    )
  }

  // TODO: Anche qui

  private def renderEntity(
                            id: Int,
                            pos: (Double, Double),
                            entityColor: String = "blue" // Colore predefinito
                          ): Div =
    val (x, y) = pos
    println("New renderEntity!")
    val borderSize: Int = 290
    div(
      cls("entity"),
      position := "absolute",
      left := s"${x + borderSize}px",
      bottom := s"${y + borderSize}px",
      width := s"${BallRadius()}px",
      height := s"${BallRadius()}px",
      backgroundColor := entityColor,
      borderRadius := "50%",
      display := "flex",
      justifyContent := "center",
      alignItems := "center",
      color := "white",
      fontSize := "10px"
    )


  private def stats(s: List[StatisticEntry]): Div =
    div(
      position := "absolute",
      top := "0px", // Posiziona il riquadro in basso
      right := "0px", // Posiziona il riquadro a destra
      width := "200px",
      padding := "10px",
      backgroundColor := "rgba(0, 0, 0, 0.4)", // Sfondo semi-trasparente per leggibilità
      color := "white", // Colore del testo per contrastare lo sfondo
      borderRadius := "8px",
      border := "1px solid #ccc",
      fontSize := "12px",
      overflowY := "auto", // Permette lo scrolling verticale se necessario
      children <-- Val(
        s.map(stat =>
          div(
            s"${stat.label} = ${stat.value}",
            marginBottom := "5px" // Spaziatura tra le righe
          )
        )
      )
    )
