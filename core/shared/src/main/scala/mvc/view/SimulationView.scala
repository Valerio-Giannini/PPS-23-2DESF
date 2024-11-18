package mvc.view

import core.Entity
import mvc.model.StatisticEntry

/**
 * A trait representing a view for displaying the state of an ongoing simulation.
 *
 * The `SimulationView` extends the base `View` trait, adding functionality for
 * updating the view with the current state of the simulation. 
 * Rendering entities and displaying simulation statistics in real time.
 */
trait SimulationView extends View:

  /**
   * Updates the simulation view with the latest entity components values and statistics.
   *
   * This method is called during the simulation to reflect changes in the
   * entities' states and to update statistical information. 
   *
   * @param entities an iterable collection of `Entity` objects representing
   *                 the current state of entities in the simulation.
   * @param statsInfos a list of `StatisticEntry` objects containing updated
   *                   simulation statistics to be displayed in the view.
   */
  def update(entities: Iterable[Entity], statsInfos: List[StatisticEntry]): Unit

