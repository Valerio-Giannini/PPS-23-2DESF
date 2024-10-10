package view

import com.raquo.laminar.api.L._
import org.scalajs.dom
import bouncing_ball.*

object Main:

  def main(args: Array[String]): Unit =
    // Montiamo l'interfaccia utente usando Laminar
    render(dom.document.getElementById("app"), mainView)

  // Definiamo un nodo root per Laminar che avvia la simulazione
  def mainView: HtmlElement =
    // Bottone per avviare la simulazione
    div(
      button(
        "Start Simulation",
        onClick --> { _ =>
          Simulation.start() // Avvia la simulazione (mostra i risultati nel terminale)
          println("Simulazione avviata. Controlla il terminale per i risultati.")
        }
      ),
      div("Simulazione in esecuzione nel terminale...")
    )

      

