//Quuesto file mi renderizza la singola entity.
package view

import com.raquo.laminar.api.L.*
import bouncing_ball.Position // Usa la Position dal modulo examples

object RenderEntity:

  def renderEntity(entityPos: Signal[Position], entityId: Int): HtmlElement =
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
    )




