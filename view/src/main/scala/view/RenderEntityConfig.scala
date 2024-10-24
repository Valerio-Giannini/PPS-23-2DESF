package view

import com.raquo.laminar.api.L.*
import org.scalajs.dom

object RenderEntityConfig:

  // Funzione che renderizza il contatore e usa una callback per restituire il valore finale
  def renderEntityConfig(label: String)(onSave: Int => Unit): HtmlElement =
    val initialStep: Int = 1
    val allowedSteps = List(1, 2, 3, 5, 10)
    val stepVar = Var(initialStep)
    val countVar = Var(0) // Var per tenere traccia del valore corrente del contatore

    // Div che contiene l'interfaccia del contatore
    div(
      cls(label),
      p(
        "Step: ",
        select(
          value <-- stepVar.signal.map(_.toString),
          onChange.mapToValue.map(_.toInt) --> stepVar,
          allowedSteps.map { step => option(value := step.toString, step) }
        )
      ),
      p(
        label + ": ",
        b(child.text <-- countVar.signal.map(_.toString)), // Mostra il contatore
        " ",
        button("–", onClick.map(_ => -stepVar.now()) --> { _ => countVar.update(_ - stepVar.now()) }), // Decrementa il contatore
        button("+", onClick.map(_ => stepVar.now()) --> { _ => countVar.update(_ + stepVar.now()) })   // Incrementa il contatore
      ),
      p(
        button("Salva", onClick.map(_ => {
          val finalValue = countVar.now() // Ottiene il valore finale del contatore dalla Var
          onSave(finalValue) // Chiama la callback passando il valore
        }) --> Observer(_ => ()))
      )
    )
