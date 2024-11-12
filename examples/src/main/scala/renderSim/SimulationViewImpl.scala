package renderSim

import simulation.Position
import view.{ReportViewImpl, View}
import core.Entity
import com.raquo.laminar.api.L.*
import org.scalajs.dom
import view.SimulationView

import scala.collection.mutable
import scala.reflect.ClassTag

object SimulationViewImpl extends SimulationView:

  // Var per le posizioni delle entità
  private val entitiesVar = Var[Iterable[(Int, (Double, Double))]](List.empty)
  // Applica distinct per evitare aggiornamenti ridondanti
  def entitiesSignal: Signal[Iterable[(Int, (Double, Double))]] = entitiesVar.signal.distinct

  // Var per le statistiche
  private val statsVar = Var[List[(String, AnyVal)]](List.empty)
  // Applica distinct per evitare aggiornamenti ridondanti
  def statsSignal: Signal[List[(String, AnyVal)]] = statsVar.signal.distinct

  // Chiamato una sola volta per renderizzare il container
  override def renderSim(entities: Iterable[Entity], initialStatsInfos: List[(String, AnyVal)]): Unit =
    // Imposta le posizioni iniziali delle entità
    val initialPositions = entities.flatMap { entity =>
      entity.get[Position].map { position =>
        (entity.id, (position.x, position.y))
      }
    }
    entitiesVar.set(initialPositions)
    statsVar.set(initialStatsInfos) // Imposta le statistiche iniziali

    val container = dom.document.getElementById("simulation-container")
    val worldDiv = renderWorld(entitiesSignal, statsSignal)
    view.ViewImpl.show(container, worldDiv)

  // Chiamato per aggiornare i signal con le nuove posizioni e statistiche
  override def renderNext(entities: Iterable[Entity], newStatsInfos: List[(String, AnyVal)]): Unit =
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


  private def renderWorld(
                           entitySignals: Signal[Iterable[(Int, (Double, Double))]],
                           statsSignal: Signal[List[(String, AnyVal)]]
                         ): Div = {
    println("RenderWorld chiamato")

    div(
      cls("world"),
      width := "500px", // Dimensione del mondo
      height := "500px",
      position := "relative",
      backgroundColor := "grey", // Posizionamento relativo
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

  private def renderEntity(
                            id: Int,
                            pos: (Double, Double),
                            dimension: Int = 20,         // Dimensione predefinita
                            entityColor: String = "blue" // Colore predefinito
                          ): Div =
    val (x, y) = pos
    println("New renderEntity!")
    div(
      cls("entity"),
      left := s"${x}px",
      top := s"${y}px",
      width := s"${dimension}px",
      height := s"${dimension}px",
      backgroundColor := entityColor,
      borderRadius := "50%",
      position := "absolute",
      display := "flex",
      justifyContent := "center",
      alignItems := "center",
      color := "white",
      fontSize := "10px"
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
        }.toSeq
      )
    )


