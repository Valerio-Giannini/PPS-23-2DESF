//Quuesto file mi renderizza la singola entity.
package view

import com.raquo.laminar.api.L.*
import bouncing_ball.Position // Usa la Position dal modulo examples
import scala.util.Random

case class Position(x: Double, y: Double)

object RenderEntity:

  /*def renderEntity(entityPos: Signal[Position], entityId: Int): HtmlElement =
    div(
      cls("entity"),
      left <-- entityPos.map(p => s"${p.x}px"),
      top <-- entityPos.map(p => s"${p.y}px"),
      width := "20px",  // Dimensione dell'entità
      height := "20px",
      backgroundColor := "blue",
      borderRadius := "50%",  // Trasforma il quadrato in un cerchio
      position := "absolute",
      display := "flex",
      justifyContent := "center",
      alignItems := "center",
      color := "white",
      fontSize := "20px",
      entityId.toString  // Mostra il numero al centro
    )*/

  def renderEntity(entityPos: Position, entityId: Int): HtmlElement =
      div(
        cls("entity"),
        left := s"${entityPos.x}px",
        top := s"${entityPos.y}px",
        width := "20px",  // Dimensione dell'entità
        height := "20px",
        backgroundColor := "blue",
        borderRadius := "50%",  // Trasforma il quadrato in un cerchio
        position := "absolute",
        display := "flex",
        justifyContent := "center",
        alignItems := "center",
        color := "white",
        fontSize := "20px",
        entityId.toString  // Mostra il numero al centro
      )

   
  // Genera una posizione casuale all'interno del mondo di 500x500 px
  def randomPosition(): Position =
    val random = new Random()
    Position(random.nextInt(480), random.nextInt(480)) // Sottraiamo 20px per evitare che le entità escano dal bordo



