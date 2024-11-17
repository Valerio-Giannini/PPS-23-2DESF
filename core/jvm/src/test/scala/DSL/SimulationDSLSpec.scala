package DSL

import core.World
import dsl.DSL.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SimulationDSLSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach:
  var world: World = _

  object AvgC1 extends DataTracker
  
  override def beforeEach(): Unit =
    world = newWorld

  "The SimulationDSL with simulation operator" should:
    "provide a report operator " which:
      "allow to retrieve the report" in:
        val x = 0
        
      