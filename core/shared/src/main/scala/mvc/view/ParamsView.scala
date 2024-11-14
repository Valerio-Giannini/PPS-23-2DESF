package mvc.view

import mvc.model.{ParameterID, ViewParameter}

import scala.concurrent.Future

trait ParamsView extends View:
  /** Inizializza i campi di questa view in base ai parametri richiesti
   * @param params
   * @return
   */
  def init(params: Iterable[ViewParameter]): Future[Iterable[(ParameterID, AnyVal)]]

