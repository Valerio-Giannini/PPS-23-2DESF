package BouncingBall.view


import BouncingBall.model.GlobalParameters.{ballRadius, borderSize}
import BouncingBall.model.Position
import com.raquo.airstream.core.Signal
import com.raquo.airstream.state.Var
import com.raquo.laminar.api.L.*
import core.Entity
import mvc.model.StatisticEntry
import mvc.view.SimulationView
import org.scalajs.dom

import scala.reflect.ClassTag

/**
 * Implementation of the `SimulationView` trait for rendering the simulation state.
 *
 * This class provides a graphical representation of the simulation, including
 * dynamic updates to entity positions and simulation statistics. It uses Laminar
 * for reactivity and efficient DOM updates.
 */
class SimulationViewImpl extends SimulationView:

  /**
   * Reactive variable for tracking the positions of entities.
   */
  private val entitiesVar = Var[Iterable[(Int, (Double, Double))]](List.empty)

  /**
   * Signal that emits updates to entity positions, ensuring distinct values to
   * avoid redundant updates.
   *
   * @return a `Signal` of the current entity positions.
   */
  def entitiesSignal: Signal[Iterable[(Int, (Double, Double))]] = entitiesVar.signal.distinct

  /**
   * Reactive variable for tracking the simulation statistics.
   */
  private val statsVar = Var[List[StatisticEntry]](List.empty)

  /**
   * Signal that emits updates to the simulation statistics, ensuring distinct values.
   *
   * @return a `Signal` of the current statistics.
   */
  def statsSignal: Signal[List[StatisticEntry]] = statsVar.signal.distinct

  /**
   * Displays the simulation view.
   *
   * Renders the simulation world and statistics inside the "simulation-container" DOM element.
   */
  override def show(): Unit =
    val container = dom.document.getElementById("simulation-container")
    val worldDiv = renderWorld(entitiesSignal, statsSignal)
    render(container, worldDiv)

  /**
   * Closes the simulation view.
   *
   * Clears the content of the "simulation-container" DOM element, resetting the view state.
   */
  override def close(): Unit =
    val container = dom.document.getElementById("simulation-container")
    container.innerHTML = ""

  /**
   * Updates the simulation view with the latest entity positions and statistics.
   *
   * This method updates the reactive variables to trigger re-rendering of the
   * simulation world and statistics. It only applies updates if changes are detected.
   *
   * @param entities      an iterable collection of entities to update their positions.
   * @param newStatsInfos a list of updated statistics to display in the view.
   */
  override def update(entities: Iterable[Entity], newStatsInfos: List[StatisticEntry]): Unit =

    /**
     * Extracts the positions of entities that define the `Position` component.
     *
     * This collection maps each entity ID to its current (x, y) coordinates.
     *
     * @return a collection of tuples, where each tuple contains an entity ID and its position as `(Double, Double)`.
     */
    val updatedPositions = entities.collect:
      case entity if entity.get[Position].isDefined =>
        val pos = entity.get[Position].get
        (entity.id, (pos.x, pos.y))

    /**
     * Updates the `entitiesVar` reactive variable if the new positions differ from the current state.
     *
     * - Compares `updatedPositions` to the current state of `entitiesVar` using `.now()`.
     * - Logs a message indicating the update when a change is detected.
     * - Updates the `entitiesVar` variable with the new positions.
     */
    if (updatedPositions != entitiesVar.now()) then
      println(s"Updating positions: $updatedPositions")
      entitiesVar.set(updatedPositions)

    /**
     * Updates the `statsVar` reactive variable if the new statistics differ from the current state.
     *
     * - Compares `newStatsInfos` to the current state of `statsVar` using `.now()`.
     * - Logs a message indicating the update when a change is detected.
     * - Updates the `statsVar` variable with the new statistics.
     */
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
                           entitySignals: Signal[Iterable[(Int, (Double, Double))]],
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
        entities.toSeq.map { case (entityId, position) =>
          println(s"Rendering entità ID: $entityId, Posizione: $position")
          renderEntity(entityId, position)
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
                            entityColor: String = "blue"
                          ): Div =
    val (x, y) = pos
    div(
      cls("entity"),
      position := "absolute",
      left := s"${x + borderSize()}px",
      bottom := s"${y + borderSize()}px",
      width := s"${ballRadius()}px",
      height := s"${ballRadius()}px",
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
