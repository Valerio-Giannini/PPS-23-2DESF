package mvc.model

trait ParameterID

case class ViewParameter(id: ParameterID, label: String, value: AnyVal, minValue: Option[AnyVal] = None, maxValue: Option[AnyVal] = None)
case class Parameter(id: ParameterID, value: AnyVal)


trait ParameterResolver[A]:
  def resolve(varName: ParameterID): A

trait Parameters:
  private var requestedParameters: List[ViewParameter] = List.empty
  private var params: List[Parameter] = List.empty

  def askParam(param: ViewParameter): Unit =
    requestedParameters :+= param

  def getRequestedParams: List[ViewParameter] = requestedParameters

  def parsedParam(parsedParams: List[Parameter]): Unit =
    params = parsedParams

  private def searchForParameter[T](param: ParameterID): T = params.find(_.id == param) match
    case Some(parameter) => parameter.value.asInstanceOf[T]
    case None => throw new NoSuchElementException(s"Parameter $param not found")


  def retrieveParamValue[T](varName: ParameterID)(using parameterResolver: ParameterResolver[T]): T
  = parameterResolver.resolve(varName)

  given ParameterResolver[Double] with
    override def resolve(varName: ParameterID): Double = searchForParameter[Double](varName)

  given ParameterResolver[Int] with
    override def resolve(varName: ParameterID): Int = searchForParameter[Int](varName)
