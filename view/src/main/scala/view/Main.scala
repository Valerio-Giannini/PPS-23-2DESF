package view

import com.raquo.laminar.api.L._
import org.scalajs.dom

object Main:

  def main(args: Array[String]): Unit =
    val container = dom.document.getElementById("app")

    if container != null then
      // Renderizza solo il mondo
      val view = div(
        h1("Render del Mondo"),
        RenderWorld.renderWorld(),  // Richiama il metodo per visualizzare solo il mondo
      )
      // Monta il contenuto nel container (l'elemento con id "app")
      render(container, view)
    else
      println("Elemento con id 'app' non trovato nel DOM.")
