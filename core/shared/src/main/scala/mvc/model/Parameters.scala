package mvc.model

abstract class Parameter[T<: Int| Double](var value: T, var id: Option[Int] = None):
  def apply(): T = value

class DoubleParameter(value: Double, id: Option[Int] = None) extends Parameter[Double](value, id)
class IntParameter(value: Int, id: Option[Int] = None) extends Parameter[Int](value, id)

case class ViewParameter(parameter: Parameter[_], label: String, minValue: Option[AnyVal] = None, maxValue: Option[AnyVal] = None)

trait Parameters:
  /**
   * Requests a new parameter to be included in the simulation.
   * The parameter is assigned a unique ID based on the current number of requested parameters.
   *
   * @param param the `ViewParameter` object representing the parameter to be requested.
   */
  def askParam(param: ViewParameter): Unit

  /**
   * Retrieves the list of parameters that have been requested for the simulation.
   *
   * @return a list of `ViewParameter` objects representing the requested parameters.
   */
  def getRequestedParams: List[ViewParameter]

  /**
   * Updates the internal parameter values based on a list of provided parameters.
   * Matches parameters by their unique IDs and updates their values accordingly.
   *
   * @param viewParameters a list of `Parameter` objects containing the updated parameter values.
   */
  def parseParams(viewParameters: List[Parameter[_]]): Unit

object Parameters:
  def apply(): Parameters = new ParametersImpl()

  private class ParametersImpl extends Parameters:
    private var requestedParameters: List[ViewParameter] = List.empty

    override def askParam(param: ViewParameter): Unit =
      param.parameter.id = Some(requestedParameters.size)
      requestedParameters :+= param

    override def getRequestedParams: List[ViewParameter] =
      requestedParameters

    override def parseParams(viewParameters: List[Parameter[_]]): Unit =
      requestedParameters.foreach(
        rp => rp.parameter.value = viewParameters.find(vp => vp.id == rp.parameter.id).get.value.asInstanceOf
      )




