package view

import com.raquo.laminar.api.L.*
import org.scalajs.dom
import scala.util.Try

object RenderParameterConfig:

  // Funzione che renderizza la lista dei parametri
  def renderParametersConfig(
                              paramsList: List[ViewParameter],
                              onSave: Map[String, AnyVal] => Unit
                            ): Div = {

    // Variabile per salvare i risultati
    val results = scala.collection.mutable.Map[String, AnyVal]()

    // Funzione che verifica se il valore è idoneo
    def validateAndSave(param: ViewParameter, inputBox: Input, errorHandler: Span): Unit = {
      val rawValue = inputBox.ref.value

      // Controlla se il valore è un numero
      val parsedValue = Try(rawValue.toDouble).toOption

      parsedValue match {
        case Some(value) =>
          // Verifica i limiti, se presenti
          val minCheck = param.minValue.forall(min => value >= min.asInstanceOf[Double])
          val maxCheck = param.maxValue.forall(max => value <= max.asInstanceOf[Double])

          if (minCheck && maxCheck) {
            inputBox.amend(
              borderColor := "green",
              borderWidth := "1px"
            ) // Bordo verde per indicare successo
            results(param.id) = value.asInstanceOf[AnyVal]
            
          } else {
            inputBox.amend(
              borderColor := "red",
              borderWidth := "1px"
            )
            
          }

        case None =>
          inputBox.amend(
            borderColor := "red",
            borderWidth := "1px"
          )
          
      }

      // Verifica se tutti i parametri sono validi e completi
      if (results.size == paramsList.size) onSave(results.toMap)
    }

    // Elemento Div che contiene la lista di parametri renderizzati
    div(
      children <-- Var(paramsList.map { param =>
        val inputBox = input().amend(
          placeholder := param.value.toString
        )
        val errorHandler = span()

        div(
          cls := "parameter-row",
          label(param.label.getOrElse(param.id)),
          param.minValue.map(min => span(s"Min: $min")),
          inputBox,
          param.maxValue.map(max => span(s"Max: $max")),
          button(
            "OK",
            onClick --> { _ => validateAndSave(param, inputBox, errorHandler) }
          ),
          errorHandler
        )
      })
    )
  }
