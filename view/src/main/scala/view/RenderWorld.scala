package view

import com.raquo.laminar.api.L.*
import RenderEntity.renderEntity

object RenderWorld:

  // Funzione che prende la lista di entità e le renderizza
  def renderWorld(
                   entitiesSignal: Signal[List[(Int, (Double, Double))]],
                   entityDesign: (Int, String),
                   worldSize: Int
                 ): HtmlElement =
    
    div(
      cls := "world",
      width := s"${worldSize}px",  
      height := s"${worldSize}px",
      position := "relative",
      backgroundColor := "grey", 
      border := "5px solid black",
      // Effettua il rendering di tutte le entità presenti nel mondo
      children <-- entitiesSignal.map(entities =>
        entities.map((entityId, entityPos) =>
          renderEntity((entityId, entityPos), entityDesign) // Passa il VisualParameter a renderEntity
        )
      )
    )


