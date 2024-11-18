package mvc.model

class Parameter[T](var value: T, var id: Option[Int] = None):
  def apply(): T = value

class IntParameter(value: Int, id: Option[Int] = None) extends Parameter[Int](value, id)

class DoubleParameter(value: Double, id: Option[Int] = None) extends Parameter[Double](value, id)

case class ViewParameter(parameter: Parameter[_], label: String, minValue: Option[AnyVal] = None, maxValue: Option[AnyVal] = None)

trait Parameters:
  private var requestedParameters: List[ViewParameter] = List.empty

  def askParam(param: ViewParameter): Unit =
    param.parameter.id = Some(requestedParameters.size)
    requestedParameters :+= param
  
  def getRequestedParams: List[ViewParameter] =
    requestedParameters

  def parseParams(viewParameters: List[Parameter[_]]): Unit =
    requestedParameters.foreach(
      rp => rp.parameter.value = viewParameters.find(vp => vp.id == rp.parameter.id).get.value.asInstanceOf
    )

object Parameters:
  def apply(): Parameters = new ParametersImpl
  private class ParametersImpl extends Parameters



