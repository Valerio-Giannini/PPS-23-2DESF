package view

import com.raquo.laminar.api.L.*
import scala.concurrent.{Future, Promise}
import org.scalajs.dom

object RenderParametersConfig:

  def renderParametersConfig[A](parameters: List[(String, List[A])], onSave: Map[String, List[A]] => Unit): HtmlElement =
    // Inizializza una mappa mutabile per tenere traccia dei valori selezionati
    val selectedValues: Var[Map[String, List[A]]] = Var(Map.empty)

    div(
      parameters.map { (title, options) =>
        div(
          h3(title), // Titolo del parametro
          ul(
            // Crea una lista di checkbox per ogni opzione
            options.map { option =>
              val isChecked = Var(false) // Variabile che traccia lo stato della checkbox

              li(
                label(
                  input(
                    typ := "checkbox",
                    // Imposta il valore della checkbox
                    checked <-- isChecked.signal,
                    onClick.mapToChecked --> { isCheckedValue =>
                      // Aggiorna lo stato della checkbox
                      isChecked.set(isCheckedValue)

                      // Aggiorna la mappa delle selezioni
                      val updatedSelections = selectedValues.now().updatedWith(title) {
                        case Some(selectedOptions) =>
                          // Aggiungi o rimuovi l'opzione selezionata
                          if isCheckedValue then Some(option :: selectedOptions)
                          else Some(selectedOptions.filterNot(_ == option))
                        case None =>
                          // Se nessuna opzione è ancora selezionata per questo titolo, aggiungi l'opzione
                          if isCheckedValue then Some(List(option)) else Some(Nil)
                      }

                      // Aggiorna il Var con la nuova mappa
                      selectedValues.set(updatedSelections)
                    }
                  ),
                  span(option.toString) // Visualizza il nome dell'opzione accanto alla checkbox
                )
              )
            }
          )
        )
      },
      p(
        button("Salva", onClick.map(_ => {
          val finalSelections = selectedValues.now() // Ottieni i valori finali delle selezioni
          onSave(finalSelections) // Chiama la callback passando i valori selezionati
        }) --> Observer(_ => ()))
      )
    )
