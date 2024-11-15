package mvc.model


case class ViewParameter(id: String, label: String, value: AnyVal, minValue: Option[AnyVal] = None, maxValue: Option[AnyVal] = None)
case class Parameter(id: String, value: AnyVal)

trait ParameterResolver[A]:
  def resolve(varName: String): A

trait Parameters:
  private var requestedParameters: List[ViewParameter] = List.empty
  private var params: List[Parameter] = List.empty

  def askParam(param: ViewParameter): Unit =
    requestedParameters :+= param
  
  def getRequestedParams: List[ViewParameter] = requestedParameters

  def parsedParam(parsedParams: List[Parameter]): Unit =
    params = parsedParams

  def searchForParameter[T](param: String): T = params.find(_.id == param) match
    case Some(parameter) => parameter.value.asInstanceOf[T]
    case None => throw new NoSuchElementException(s"Parameter $param not found")

  def retrieveParamValue[T](varName: String)(using parameterResolver: ParameterResolver[T]): T
  = parameterResolver.resolve(varName)

  given ParameterResolver[Double] with
    override def resolve(varName: String): Double = searchForParameter[Double](varName)

  given ParameterResolver[Int] with
    override def resolve(varName: String): Int = searchForParameter[Int](varName)

object Parameters:
  def apply(): Parameters = new ParametersImpl
  private class ParametersImpl extends Parameters
    