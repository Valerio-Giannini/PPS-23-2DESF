//Questo file viene fatto partire con il lancio della simulazione
//
// --Simulation chiama initializeWorld --> creazione entities
// --Simulation dovrà inviare a RenderSimulation le entity con le loro position
//Viene chiamato RenderWorld per inserire il mondo
//Viene ciclato RenderEntity per inserire le entities

// --Simulation chiama world.update
// --Simulation calcola le nuove position delle entities
// --Simulation dovrà inviare nuovamente le entity con le loro position
//Viene aggiornato il render spostando le entities nella loro nuova position

// E così via fino al termine della simulation

package view

import bouncing_ball.*
import com.raquo.laminar.api.L.*


object RenderSimulation:

  def renderSimulation(entities: List[(Int, Signal[Position])]): HtmlElement =
    @annotation.tailrec
    def loop(remaining: List[(Int, Signal[Position])], acc: List[HtmlElement]): List[HtmlElement] =
      remaining match
        case Nil => acc
        case (entityId, positionSignal) :: tail =>
          val entityHtml = RenderEntity.renderEntity(positionSignal, entityId)
          loop(tail, entityHtml :: acc) // Continua la ricorsione accumulando gli elementi

    // Chiamo la funzione loop partendo da una lista vuota come accumulatore
    val entityElements = loop(entities, List()).reverse // Invertiamo la lista alla fine per mantenere l'ordine corretto
    div(entityElements)


    