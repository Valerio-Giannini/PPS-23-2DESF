package view.init

import com.raquo.laminar.api.L.*
import org.scalajs.dom

import scala.concurrent.{Future, Promise}

object ConfigureParam:

  // Funzione che configura i parametri e restituisce un Future con il risultato
  def configureParameters(paramsList: List[ViewParameter]): Future[List[(String, AnyVal)]] =
    val promise = Promise[List[(String, AnyVal)]]()

    // Funzione `onSave` per completare la promise con i risultati configurati
    val onSave: Map[String, AnyVal] => Unit = results => {
      promise.success(results.toList)
    }

    // Chiama `renderParametersConfig` con `paramsList` e `onSave`
    val parameterConfig = RenderParameterConfig.renderParametersConfig(paramsList, onSave)

    // Renderizza l'elemento di configurazione nel contenitore della simulazione
    render(dom.document.getElementById("simulation-container"), parameterConfig)

    // Restituisce il Future che verrà completato quando tutti i parametri saranno configurati
    promise.future
