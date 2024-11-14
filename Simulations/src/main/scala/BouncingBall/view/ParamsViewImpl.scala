package BouncingBall.view

import mvc.model.{ParameterID, ViewParameter}
import mvc.view.ParamsView
import org.scalajs.dom
import com.raquo.laminar.api.L.*

import scala.util.Try
import scala.concurrent.{Future, Promise}

class ParamsViewImpl extends ParamsView:
  var parameters: Iterable[ViewParameter]  = _
  var results: Map[ParameterID, AnyVal] => Unit = _

  override def show(): Unit =
    val paramsConfig = _renderInit(parameters, results)
    // Setta Il container dove visualizzare il render
    val container = dom.document.getElementById("init-container")
    // Renderizza l'elemento di configurazione nel contenitore della simulazione
    render(container, paramsConfig)

  override def close(): Unit =
    val container = dom.document.getElementById("init-container")
    container.innerHTML = ""

  override def init(params: Iterable[ViewParameter]): Future[Iterable[(ParameterID, AnyVal)]] =
    parameters = params
    val promise = Promise[Iterable[(ParameterID, AnyVal)]]()
    // Funzione `onSave` per completare la promise con i risultati configurati
    val onSave: Iterable[(ParameterID, AnyVal)] => Unit = resultsParams => promise.success(resultsParams)
    results = onSave

    // Restituisce il Future che verrà completato quando tutti i parametri saranno configurati
    promise.future

  def _renderInit(
                  paramsList: Iterable[ViewParameter],
                  onSave: Map[ParameterID, AnyVal] => Unit
                ): Div =

    // Iterable di coppie (parametro, campo di input) per riferimento diretto

    val inputFields = paramsList.map { param =>

      val inputBox = input(
        placeholder := param.value.toString
      )

      (param, inputBox)

    }.toList // Convertiamo a List per mantenere l'ordine nell'iterazione

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

              Some(param.id -> value.asInstanceOf[param.value.type])
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
          label(
            param.label,
            width := "100px", // Imposta una larghezza fissa per la label

            display := "inline-block", // Assicura che la label occupi lo spazio specificato

            textAlign := "right" // Allinea il testo a destra

          ),
          span(" | "), // Separatore tra label e il primo elemento

          param.minValue.map(min =>
            span(
              s"Min: $min",
              width := "80px",
              display := "inline-block",
              textAlign := "center"
            )
          ),
          span(" | "), // Separatore tra min e inputBox

          inputBox.amend(
            width := "150px",
            display := "inline-block"
          ),
          span(" | "), // Separatore tra inputBox e max

          param.maxValue.map(max =>
            span(
              s"Max: $max",
              width := "80px",
              display := "inline-block",
              textAlign := "center"
            )
          ),
          span(" | "), // Separatore tra max e errorHandler

          errorHandler
        )

      }.toSeq :+ button(
        "OK",
        onClick --> (_ => validateAll())
      )
    )