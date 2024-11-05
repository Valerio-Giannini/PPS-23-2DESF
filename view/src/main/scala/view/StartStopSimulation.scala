package view

import com.raquo.laminar.api.L._

object StartStopSimulation:

  // Variabile reattiva per tracciare se la simulazione deve essere avviata o fermata
  val isRunning = Var(false)

  // Funzione che fornisce il pulsante Start/Stop
  def startStopButton: Button = {
    button(
      child.text <-- isRunning.signal.map {
        case true => "Stop Simulation"
        case false => "Start Simulation"
      },
      onClick --> { _ =>
        // Inverti lo stato della simulazione al clic del pulsante
        isRunning.update(!_)
      }
    )
  }
