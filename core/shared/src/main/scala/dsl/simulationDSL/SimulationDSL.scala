package dsl.simulationDSL

import mvc.model.Simulation

/** SimulationDSL provides a set of operations to interact with the simulation using an
 * english-like syntax.
 *
 * Operators:
 *
 * Requests a parameter
 * {{{
 *  simulation askParam (param)
 * }}}
 *
 * Retrieves the list of parameter asked by the user
 * {{{
 *  simulation.askedParameters
 * }}}
 *
 * Updates the simulation parameter asked with the one provided
 * {{{
 *  simulation updateParameters (viewParameters)
 * }}}
 *
 * Defines the condition to run the simulation
 * {{{
 *  simulation runTill {()=>condition}
 * }}}
 *
 * Checks the running condition
 * {{{
 *  simulation.shouldRun
 * }}}
 *
 * Updates the simulation for a specific tick
 * {{{
 *  simulation update (tick)
 * }}}
 *
 * Tracks a statistic in the simulation.
 * {{{
 *  simulation track (stat)
 * }}}
 *
 * Define a report chart
 * {{{
 *  simulation chart (chart)
 * }}}
 *
 * Get the statistic of the current tick
 * {{{
 *  simulation.stats
 * }}}
 *
 * Get the charts of the report
 * {{{
 *  simulation.report
 * }}}
 */
trait SimulationDSL:
  def simulation(using sim: Simulation): SimulationWord

object SimulationDSL extends SimulationDSL:
  override def simulation(using sim: Simulation): SimulationWord = SimulationWord(sim)