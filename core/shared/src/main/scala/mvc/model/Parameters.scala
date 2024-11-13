package mvc.model

import mvc.view.ViewParameter

case class Parameter(label: String, value: AnyVal)

trait SimulationParameters:
  private var requestedParameters : List[ViewParameter] = List.empty
  private var params: List[Parameter] = List.empty

  def askParam(param: ViewParameter): Unit =
    requestedParameters  :+= param

  def parseParam(params: List[ViewParameter]): Unit =
    requestedParameters.map(p => Parameter(p.label, p.value))

