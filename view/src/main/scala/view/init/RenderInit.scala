package view.init

import com.raquo.laminar.api.L.*
import org.scalajs.dom
import scala.util.Try

object RenderInit:

  // Funzione che renderizza l'iterable dei parametri
  def renderInit(
                  paramsList: Iterable[ViewParameter],  
                  onSave: Map[String, AnyVal] => Unit
                ): Div =

    // Iterable di coppie (parametro, campo di input) per riferimento diretto
    val inputFields = paramsList.map { param =>
      val inputBox = input(
        placeholder := param.value.toString
      )
      (param, inputBox)
    }.toList  // Convertiamo a List per mantenere l'ordine nell'iterazione

    // Funzione per validare tutti i parametri e, se tutti sono validi, salvare i risultati
    def validateAll(): Unit =
      // Prova a costruire la mappa dei risultati solo se tutti i parametri sono validi
      val maybeResults = inputFields.flatMap { (param, inputBox) =>
        val rawValue = inputBox.ref.value

        // Controlla se il valore è un numero
        val parsedValue = Try(rawValue.toDouble).toOption

        parsedValue match
          case Some(value) =>
            // Verifica i limiti, se presenti
            val minCheck = param.minValue.forall(min => value >= min.asInstanceOf[Double])
            val maxCheck = param.maxValue.forall(max => value <= max.asInstanceOf[Double])

            if minCheck && maxCheck then
              // Imposta il bordo verde per indicare successo
              inputBox.amend(
                borderColor := "green",
                borderWidth := "1px"
              )
              // Restituisci una Some con la coppia chiave-valore se valido
              Some(param.label.getOrElse("Unnamed") -> value.asInstanceOf[AnyVal])
            else
              // Imposta il bordo rosso in caso di errore
              inputBox.amend(
                borderColor := "red",
                borderWidth := "1px"
              )
              None // Indica che questo parametro non è valido

          case None =>
            // Imposta il bordo rosso in caso di errore
            inputBox.amend(
              borderColor := "red",
              borderWidth := "1px"
            )
            None // Indica che questo parametro non è valido
      }

      // Verifica se tutti i parametri sono validi (ossia, `maybeResults` ha lo stesso numero di elementi di `paramsList`)
      if maybeResults.size == paramsList.size then
        // Completa `onSave` con la mappa dei risultati solo se tutti sono validi
        onSave(maybeResults.toMap)

    // Elemento Div che contiene la lista di parametri renderizzati e il pulsante OK
    div(
      // Inseriamo ogni elemento come parte della lista `Seq`
      inputFields.map { (param, inputBox) =>
        val errorHandler = span()

        div(
          cls := "parameter-row",
          label(param.label.getOrElse("Unnamed")),
          paddingRight := "20px",
          param.minValue.map(min => span(s"Min: $min")),
          paddingRight := "20px",
          inputBox,
          paddingRight := "20px",
          param.maxValue.map(max => span(s"Max: $max")),
          errorHandler
        )
      }.toSeq :+ button(
        "OK",
        onClick --> (_ => validateAll())
      )
    )

