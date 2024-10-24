package view

import com.raquo.laminar.api.L.*
import org.scalajs.dom

import scala.concurrent.{Future, Promise}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object ConfigureSimulation:

  // Funzione che configura la simulazione e restituisce un Future con il risultato
  def configureSimulation(name: String)(parameters: List[String]): Future[List[(String, Int)]] =
    val promise = Promise[List[(String, Int)]]() // Promise che sarà completata quando il valore è salvato

    parameters match
      case Nil =>
        val entityConfig = RenderEntityConfig.renderEntityConfig("Select the number of " + name) { value =>
          // Completa la promise con il risultato dopo che l'utente ha salvato il contatore
          promise.success(List(("Entities", value)))
        }
        render(dom.document.getElementById("simulation-container"), entityConfig)

      case _ => promise.success(List.empty) // Gestisci altri casi se necessario

    // Restituisce il Future che verrà completato quando il valore sarà salvato
    promise.future
