package BouncingBall.view


import BouncingBall.model.GlobalParameters.{ballRadius, borderSize}
import BouncingBall.model.{Position, Dimension}
import com.raquo.airstream.core.Signal
import com.raquo.airstream.state.Var
import com.raquo.laminar.api.L.*
import core.Entity
import mvc.model.StatisticEntry
import mvc.view.SimulationView
import org.scalajs.dom


import core.given

/**
 * Implementation of the `SimulationView` trait for rendering the simulation state.
 *
 * This class provides a graphical representation of the simulation, including
 * dynamic updates to entity positions and simulation statistics. It uses Laminar
 * for reactivity and efficient DOM updates.
 */
class SimulationViewImpl extends SimulationView:

  private val entitiesVar = Var[Iterable[(Int, (Double, Double), Int)]](List.empty)
  /**
   * Signal that emits updates to entity positions, ensuring distinct values to
   * avoid redundant updates.
   *
   * @return a `Signal` of the current entity positions.
   */
  private def entitiesSignal: Signal[Iterable[(Int, (Double, Double), Int)]] = entitiesVar.signal.distinct

  private val statsVar = Var[List[StatisticEntry]](List.empty)
  /**
   * Signal that emits updates to the simulation statistics, ensuring distinct values.
   *
   * @return a `Signal` of the current statistics.
   */
  private def statsSignal: Signal[List[StatisticEntry]] = statsVar.signal.distinct

  override def show(): Unit =
    val container = dom.document.getElementById("simulation-container")
    val worldDiv = renderWorld(entitiesSignal, statsSignal)
    render(container, worldDiv)

  override def close(): Unit =
    val container = dom.document.getElementById("simulation-container")
    container.innerHTML = ""

  override def update(entities: Iterable[Entity], newStatsInfos: List[StatisticEntry]): Unit =

    val updatedComponents = entities.collect:
      case entity if (entity.get[Position].isDefined && entity.get[Dimension].isDefined) =>
        val pos = entity.get[Position].get
        val dim = entity.get[Dimension].get
        (entity.id, (pos.x, pos.y), dim.x)

    if (updatedComponents != entitiesVar.now()) then
      println(s"Updating positions: $updatedComponents")
      entitiesVar.set(updatedComponents)

    if (newStatsInfos != statsVar.now()) then
      println(s"Updating stats: $newStatsInfos")
      statsVar.set(newStatsInfos)

  /**
   * Renders the simulation world, including all entities and statistics.
   *
   * This method dynamically generates the world layout based on the current entity
   * positions and statistics, ensuring an up-to-date graphical representation.
   *
   * @param entitySignals a signal representing the positions of entities.
   * @param statsSignal   a signal representing the simulation statistics.
   * @return a Laminar `Div` element containing the rendered world and statistics.
   */
  private def renderWorld(
                           entitySignals: Signal[Iterable[(Int, (Double, Double),Int)]],
                           statsSignal: Signal[List[StatisticEntry]]
                         ): Div =
    println("RenderWorld chiamato")

    div(
      cls("world"),
      width := s"${borderSize() * 2}px",
      height := s"${borderSize() * 2}px",
      position := "relative",
      backgroundColor := "#ccc",
      border := "5px solid black",
      children <-- entitySignals.map { entities =>
        println(s"Entità da renderizzare: ${entities.mkString(", ")}")
        entities.toSeq.map { case (entityId, position, dimension) =>
          println(s"Rendering entità ID: $entityId, Posizione: $position, Dimensione: $dimension")
          renderEntity(entityId, position, dimension)
        }
      },

      child <-- statsSignal.map { statsList =>
        println(s"Rendering statistiche: ${statsList.mkString(", ")}")
        stats(statsList)
      }
    )

  /**
   * Renders a single entity in the simulation world.
   *
   * @param id          the unique identifier of the entity.
   * @param pos         the position of the entity as a tuple (x, y).
   * @param entityColor the color of the entity, defaulting to blue.
   * @return a Laminar `Div` element representing the rendered entity.
   */
  private def renderEntity(
                            id: Int,
                            pos: (Double, Double),
                            dim: Int,
                            entityColor: String = "blue"
                          ): Div =
    val (x, y) = pos
    div(
      cls("entity"),
      position := "absolute",
      left := s"${x + borderSize()}px",
      bottom := s"${y + borderSize()}px",
      width := s"${dim}px", //Possible to change dim with ballRadius() if You want to remove Dimension Component
      height := s"${dim}px",
      backgroundColor := entityColor,
      borderRadius := "50%",
      display := "flex",
      justifyContent := "center",
      alignItems := "center",
      color := "white",
      fontSize := "10px"
    )

  /**
   * Renders the simulation statistics in a dedicated section of the view.
   *
   * @param s a list of `StatisticEntry` objects representing the current statistics.
   * @return a Laminar `Div` element containing the rendered statistics.
   */
  private def stats(s: List[StatisticEntry]): Div =
    div(
      position := "absolute",
      top := "0px",
      right := "0px",
      width := "200px",
      padding := "10px",
      backgroundColor := "rgba(0, 0, 0, 0.4)",
      color := "white",
      borderRadius := "8px",
      border := "1px solid #ccc",
      fontSize := "12px",
      overflowY := "auto",
      children <-- Val(
        s.map(stat =>
          div(
            s"${stat.label} = ${stat.value}",
            marginBottom := "5px"
          )
        )
      )
    )
