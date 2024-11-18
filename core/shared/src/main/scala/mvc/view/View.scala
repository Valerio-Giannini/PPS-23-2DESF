package mvc.view

/**
 * A base trait for representing a generic view in the simulation.
 *
 * The `View` is composed of modular blocks corresponding to different stages
 * or components of the simulation:
 * - `app`: A general container for launching the simulation.
 * - `init`: A container dedicated to configuring simulation parameters.
 * - `simulation`: A container for rendering the ongoing simulation.
 * - `report`: A container for displaying report graphics and statistics.
 *
 * The specific functionality and content of the view are determined by its
 * implementation.
 */
trait View: 

  /**
   * Displays the current view along with its relevant data.
   *
   * This method should be implemented to render the appropriate visual
   * components for the view. The specific data and layout depend on the
   * implementation.
   */
  def show(): Unit

  /**
   * Closes the current view and clears its contents.
   *
   * This method is responsible for cleaning up any rendered components
   * associated with the view. It resets the visual state, preparing the
   * container for a potential new view.
   */
  def close(): Unit

