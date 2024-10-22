package view

import com.raquo.laminar.api.L.*
import org.scalajs.dom

import scala.collection.mutable
import scala.concurrent.{Future, Promise}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object ConfigureSimulation:

  // Funzione per configurare la simulazione, restituisce un Future con il risultato dopo il click su "Next"
  def configureSimulation(archetype: Int)(nameList: List[String]): Future[List[(String, Int)]] =
    val counters = mutable.Map[String, Int]() // Mappa mutabile per mantenere il valore dei contatori
    val promise = Promise[List[(String, Int)]]() // Promessa per restituire il risultato al click su "Next"

    // Renderizza i contatori per ogni nome
    val pageContent = renderConfiguration(nameList, counters, promise)
    render(dom.document.getElementById("simulation-container"), pageContent)

    // Restituisce un Future che sarà completato dopo il click su "Next"
    promise.future


  // Funzione che renderizza la configurazione dei contatori e imposta il completamento della promessa
  private def renderConfiguration(
                                   nameList: List[String],
                                   counters: mutable.Map[String, Int],
                                   promise: Promise[List[(String, Int)]]
                                 ): HtmlElement =
    // Inizializza i contatori per ogni nome
    nameList.foreach(name => counters(name) = 0)

    // Crea una colonna con il nome e il contatore associato
    div(
      nameList.map { name =>
        div(
          h3(s"Configura $name"),
          button("-", onClick.map(_ => decrementCounter(counters, name)) --> Observer(_ => ())),
          span(child.text <-- Signal.fromValue(counters(name).toString)), // Mostra il contatore
          button("+", onClick.map(_ => incrementCounter(counters, name)) --> Observer(_ => ()))
        )
      },
      button("Next", onClick.map(_ => {
        // Completa la promessa con il risultato dei contatori
        val result = nameList.map(name => (name, counters(name)))
        promise.success(result) // Completa la promessa con la lista di coppie

        // Pulisce tutto il contenuto del body
        dom.document.body.innerHTML = ""
      }) --> Observer(_ => ()))
    )

  // Funzione per incrementare il contatore
  private def incrementCounter(counters: mutable.Map[String, Int], name: String): Unit =
    counters(name) += 1

  // Funzione per decrementare il contatore, assicurandosi che rimanga >= 0
  private def decrementCounter(counters: mutable.Map[String, Int], name: String): Unit =
    if counters(name) > 0 then
      counters(name) -= 1
