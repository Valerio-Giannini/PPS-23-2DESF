package mvc

import bouncing_ball.Simulation
import com.raquo.laminar.api.L.*
import org.scalajs.dom
import view.init.ViewParameter
import view.utils.StartStopButton

object Main:

  def main(args: Array[String]): Unit = 
    InitSimulationView.start()


object InitSimulationView:
  val app = div(
    h1("Simulation App"),
    StartStopButton.startStopButton // Usa il pulsante Start/Stop da StartStopSimulation
  )

  def start(): RootNode =
    StartStopButton.isRunning.signal.foreach { running =>
      if (running) {
        SimulationController().startSimulation()// Avvia la simulazione
      } else {
        //SimulationController().stopSimulation()
      }
    }(unsafeWindowOwner)
    render(dom.document.body, app)