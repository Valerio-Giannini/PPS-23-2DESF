package Main

import bouncing_ball.Simulation
import com.raquo.laminar.api.L._
import org.scalajs.dom
import view.StartStopSimulation

object Main:

  def main(args: Array[String]): Unit =
    val app = div(
      h1("Simulation App"),
      StartStopSimulation.startStopButton // Usa il pulsante Start/Stop da StartStopSimulation
    )

    // Rileva cambiamenti nello stato di `isRunning` e avvia/ferma la simulazione di conseguenza
    StartStopSimulation.isRunning.signal.foreach { running =>
      if (running) {
        Simulation.runSimulation() // Avvia la simulazione
      } else {
        Simulation.stop() // Ferma la simulazione
      }
    }(unsafeWindowOwner)

    render(dom.document.body, app)

