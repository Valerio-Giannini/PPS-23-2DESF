package view

import com.raquo.laminar.api.L.*
import org.scalajs.dom
import scala.concurrent.{Future, Promise}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object ConfigureSimulation:

  // Funzione che configura la simulazione e restituisce un Future con il risultato
  def configureSimulation[A](name: String)(parameters: List[(String, List[A])]): Future[List[(String, Int)]] =
    val promise = Promise[List[(String, Int)]]() // Promise che sarà completata quando il valore è salvato

    // Caso in cui non ci sono parametri: mostriamo solo l'interfaccia di configurazione entità
    if parameters.isEmpty then {
      val entityConfig = RenderEntityConfig.renderEntityConfig("Select the number of " + name) { value =>
        // Completa la promise con il risultato dopo che l'utente ha salvato il contatore
        promise.success(List(("Entities", value)))
      }
      render(dom.document.getElementById("simulation-container"), entityConfig)
    } else {
      // Gestione combinata per configurare sia le entità sia i parametri
      var entitiesConfigured: Option[Int] = None
      var parametersConfigured: Option[Map[String, List[A]]] = None

      // Funzione che controlla se entrambi i componenti sono stati configurati e completa la promise
      def checkAndComplete(): Unit =
        (entitiesConfigured, parametersConfigured) match
          case (Some(entityCount), Some(paramValues)) =>
            // Completa la promise quando sia le entità sia i parametri sono configurati
            promise.success(List(("Entities", entityCount)) ++ paramValues.toList.map { case (key, values) => (key, values.length) })
          case _ => () // Non fa nulla finché non sono configurati entrambi

      // Interfaccia per configurare le entità
      val entityConfig = RenderEntityConfig.renderEntityConfig("Select the number of " + name) { value =>
        entitiesConfigured = Some(value) // Salva la configurazione delle entità
        checkAndComplete()               // Verifica se completare la promise
      }
      render(dom.document.getElementById("simulation-container"), entityConfig)

      // Interfaccia per configurare i parametri (richiede il parametro `onSave`)
      val parameterConfig = RenderParametersConfig.renderParametersConfig(parameters, onSave = { values =>
        parametersConfigured = Some(values) // Salva la configurazione dei parametri
        checkAndComplete()                  // Verifica se completare la promise
      })
      render(dom.document.getElementById("simulation-container"), parameterConfig)
    }

    // Restituisce il Future che verrà completato quando il valore sarà salvato
    promise.future
