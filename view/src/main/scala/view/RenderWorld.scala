//Si occupa di renderizzare un ambiente quadrato con bordi neri.
package view

import com.raquo.laminar.api.L.*

object RenderWorld:

  def renderWorld(): HtmlElement =
    div(
      cls("world"),
      width := "500px",  // Definisci la dimensione del mondo
      height := "500px",
      position := "relative", //Definisce la posizione del World relazione alla sua posizione normale nel flusso del documento
      border := "1px solid black"
    )
