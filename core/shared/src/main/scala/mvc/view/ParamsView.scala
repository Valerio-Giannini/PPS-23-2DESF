package mvc.view

import mvc.model.{Parameter, ViewParameter}

import scala.concurrent.Future

trait ParamsView extends View:
  /** Inizializza i campi di questa view in base ai parametri richiesti
   * @param params
   * @return
   */
  def init(params: Iterable[ViewParameter]): Future[Iterable[Parameter]]

