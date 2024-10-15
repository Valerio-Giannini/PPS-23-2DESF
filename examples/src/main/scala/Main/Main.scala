package Main

import bouncing_ball.*
import com.raquo.laminar.api.L._
import org.scalajs.dom

object Main:

  def main(args: Array[String]): Unit =
    val app = div(
      h1("Simulation App"),
      button("Start Simulation", onClick --> (_ => Simulation.runSimulation()))
    )
    render(dom.document.body, app)


