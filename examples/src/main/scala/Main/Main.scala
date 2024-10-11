package Main

import bouncing_ball.*
import com.raquo.laminar.api.L.*
import org.scalajs.dom

object Main:
  
  def main(args: Array[String]): Unit =
    println("Simulazione del Bouncing Ball")
    Simulation.runSimulation()


