package fixtures

import core.Component

//case class Position(x: Double, y: Double) extends Component
//
//case class Speed(vx: Double, vy: Double) extends Component

trait ComponentsFixture:
  val position = Position(0, 0)
  val speed    = Speed(1, 1)
