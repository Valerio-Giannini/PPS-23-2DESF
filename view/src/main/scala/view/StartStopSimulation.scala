package view

import com.raquo.laminar.api.L.*

object StartStopSimulation:
  
  val isRunning = Var(false)
  def startStopButton: Button =
    button(
      child.text <-- isRunning.signal.map:
        case true  => "Stop Simulation"
        case false => "Start Simulation",
      onClick --> (_ =>
        isRunning.update(!_)
        )
    )
