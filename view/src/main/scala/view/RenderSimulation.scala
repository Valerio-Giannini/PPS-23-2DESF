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

  def renderSimulation[E](entities: List[E]) = // Il tipo E dovrà essere [entityId, Position]
    ???

