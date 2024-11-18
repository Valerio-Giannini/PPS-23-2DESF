package mvc.view

import mvc.model.{Parameter, ViewParameter}
import scala.concurrent.Future

/**
 * A trait representing a view for configuring simulation parameters.
 *
 * The `ParamsView` extends the base `View` trait and provides functionality
 * for initializing the view with required parameters, allowing users to input
 * and validate their configurations.
 */
trait ParamsView extends View:

  /**
   * Initializes the view with the specified parameters.
   *
   * This method prepares the view by setting up fields based on the provided
   * parameters. It returns a `Future` that completes with the configured
   * parameter values once the user has finished their input and submitted
   * the data.
   *
   * @param params an iterable collection of `ViewParameter` objects describing
   *               the parameters to be configured, including metadata such as
   *               default values and constraints.
   * @return a `Future` that resolves to an iterable collection of `Parameter[_]`
   *         objects containing the user's configured values.
   */
  def init(params: Iterable[ViewParameter]): Future[Iterable[Parameter[_]]]

