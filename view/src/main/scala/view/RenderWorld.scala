//Si occupa di renderizzare un ambiente quadrato con bordi neri.
package view

import com.raquo.laminar.api.L.*
import RenderEntity.*
import bouncing_ball.Position

object RenderWorld:

  def renderWorld(entities: List[(Int, Signal[Position])]): HtmlElement =
    div(
      cls("world"),
      width := "500px",  // Definisci la dimensione del mondo
      height := "500px",
      position := "relative", //Definisce la posizione del World relazione alla sua posizione normale nel flusso del documento
      border := "5px solid black",
      //da qui renderizzo le entity che devono stare all'interno del world.
      RenderSimulation.renderSimulation(entities)
    )
