package Main

import com.raquo.laminar.api.L.*
import controller.SimulationController
import org.scalajs.dom
import renderSim.SimulationViewImpl
import simulation.BounceSimulation
import view.utils.StartStopButton

object Main:

  def main(args: Array[String]): Unit =
//    val simView    = SimulationViewImpl
//    val controller = SimulationController(BounceSimulation, simView)

    val app = div(
      h1("Simulation App"),
      StartStopButton.startStopButton // Usa il pulsante Start/Stop da StartStopSimulation
    )

   /* // Rileva cambiamenti nello stato di `isRunning` e avvia/ferma la simulazione di conseguenza
    StartStopButton.isRunning.signal.foreach { running =>
      if running then controller.start() // Avvia la simulazione
    }(unsafeWindowOwner)
*/
    render(dom.document.body, app)
