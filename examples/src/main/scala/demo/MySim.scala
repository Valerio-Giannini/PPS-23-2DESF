package demo

import mvc.model.Simulation

class MySim extends Simulation:
  override def condition: Boolean = true
  override def init(): Unit =
    /*
    Configurare i parametri e le entità qui?
     */
    notifyObservers("Simulation Initialized")

  override def tick(currentTick: Int): Unit =
    world.update()
    notifyObservers(s"Tick $currentTick completed")

  override def isRunning: Boolean =
    true
    //currentTick < 100 // esempio di limite

  override def endSimulation(): Unit =
    notifyObservers("Simulation Ended")

  override def showStats(): Unit =
    notifyObservers(s"Statistiche attuali: ${world.entities.size} entità attive.")

  override def showReport(): Unit =
    notifyObservers(s"Report finale: ${world.entities.size} entità rimanenti.")
