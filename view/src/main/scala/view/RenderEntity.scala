package view

import com.raquo.laminar.api.L.*

object RenderEntity:

  def renderEntity(entityPos: (Int, (Double, Double)),
                   entityDesign: (Int, String)): HtmlElement =
    
    val (id, (x, y)) = entityPos
    val (dimension, entityColor) = entityDesign 

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
      fontSize := "20px",
      s"$id"                       
    )





