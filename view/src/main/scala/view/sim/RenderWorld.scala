package view.sim

import com.raquo.laminar.api.L.*
import core.Entity
import view.sim.RenderEntity.renderEntity

object RenderWorld:

  // Funzione che prende la lista di entità e le renderizza
  def renderWorld(entitiesSignal: Signal[List[(Int, (Double, Double))]]): HtmlElement =
      div(
      cls("world"),
      width := "510px",  // Dimensione del mondo
      height := "510px",
      position := "relative",
      backgroundColor := "grey",// Posizionamento relativo
      border := "5px solid black",
      // Effettua il rendering di tutte le entità presenti nel mondo
      children <-- (entitiesSignal.map { entities => entities.map { case (entityId, entityPos) =>
        renderEntity(entityId, entityPos)
      }})
    )

