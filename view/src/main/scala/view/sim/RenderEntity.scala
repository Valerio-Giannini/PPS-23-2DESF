package view.sim

import com.raquo.laminar.api.L.*
import core.Entity

object RenderEntity:

  def renderEntity(entityPos: (Int, (Double, Double))): HtmlElement =
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
      fontSize := "20px"
    )




