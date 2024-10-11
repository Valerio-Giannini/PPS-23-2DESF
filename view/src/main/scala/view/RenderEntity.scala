package view

import core.Entity
import com.raquo.laminar.api.L._

object RenderEntity:

  def renderEntity(entityPos: (Double, Double), entityId: Entity.ID): HtmlElement =
    val (x, y) = entityPos
    div(
      cls("entity"),
      left := s"${x}px",
      top := s"${y}px",
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




