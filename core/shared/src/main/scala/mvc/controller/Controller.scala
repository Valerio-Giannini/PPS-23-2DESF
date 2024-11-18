package mvc.controller

/**
 * Trait that represents a controller responsible for managing the lifecycle of the simulation application
 */
trait Controller:

  /**
   * Starts the simulation application
   */
  def start(): Unit

  /**
   * Ends the process or simulation.
   * This method is called to stop the ongoing operation, such as releasing resources,
   * halting the workflow, or hiding a view.
   */
  def end(): Unit