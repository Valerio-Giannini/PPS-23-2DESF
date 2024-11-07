package view.sim

import com.raquo.laminar.api.L.*

object RenderEntity:

  // Funzione che renderizza un'entità con ID, posizione, dimensione e colore
  def renderEntity(
                    id: Int,
                    pos: (Double, Double),
                    dimension: Int = 20,         // Dimensione predefinita
                    entityColor: String = "blue" // Colore predefinito
                  ): Div =
    val (x, y) = pos
    div(
      cls("entity"),
      left := s"${x}px",
      top := s"${y}px",
      width := s"${dimension}px",
      height := s"${dimension}px",
      backgroundColor := entityColor,
      borderRadius := "50%",
      position := "absolute",
      display := "flex",
      justifyContent := "center",
      alignItems := "center",
      color := "white",
      fontSize := "10px"
    )

