package view

import coreJS.Entity
import com.raquo.laminar.api.L.*

object RenderEntity:

  def renderEntity(
                    entityId: Entity.ID,
                    entityPos: (Double, Double),
                    dimension: Int = 20, // Parametro opzionale con valore di default 20
                    entityColor: String = "blue" // Parametro opzionale con valore di default "blue"
                  ): HtmlElement =
    val (x, y) = entityPos
    div(
      cls("entity"),
      left := s"${x}px",
      top := s"${y}px",
      width := s"${dimension}px", // Utilizza il parametro dimension, se specificato
      height := s"${dimension}px",
      backgroundColor := entityColor, // Usa entityColor come colore, se specificato
      borderRadius := "50%", // Trasforma il quadrato in un cerchio
      position := "absolute",
      display := "flex",
      justifyContent := "center",
      alignItems := "center",
      color := "white",
      fontSize := s"${dimension / 2}px", // Ridimensiona il testo in base alla dimensione
      entityId.toString // Mostra il numero al centro
    )




