package dsl

import dsl.Stats.meanC1
import core.{C1, ComponentTag, IncrementC1System, World}
import dsl.coreDSL.CoreDSL.*
import dsl.simulationDSL.SimulationDSL.*
import mvc.model.{DataTracker, DoubleParameter, IntParameter, Point, Simulation, StatisticEntry, ViewParameter}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import core.given 

object Stats:
  def meanC1(using world: World): Int =
    val c1s = for
      entity <- from(world).entitiesHaving[C1]
      c1 <- from(world).componentsOf(entity).get[C1]
    yield c1.x

    c1s.sum / c1s.size

class TestSim extends Simulation:
  override def tick(currentTick: Int): Unit =
    given World = world
    update(world)


class SimulationDSLSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach:
  var sim: Simulation = _

  val valueOfC1 = 5

  given Simulation = sim
  given World = sim.world

  object AvgC1 extends DataTracker

  override def beforeEach(): Unit =
    sim = TestSim()
    into(sim.world).include(IncrementC1System())
    into(sim.world).spawnNewEntityWith(C1(valueOfC1))

  "The SimulationDSL with simulation operator" should:
    "provide operators to ask parameters" in:
      val param = IntParameter(5, Some(1))
      val param2 = IntParameter(4, Some(1))
      val param3 = DoubleParameter(2, Some(1))
      val label = "param"
      val min = 4
      val max = 20

      simulation askParam param withMin min withMax max withLabel label

      simulation.askedParameters contains ViewParameter(param, label, Some(min), Some(max))

      simulation.updateParameters(List(IntParameter(15, Some(0))))

      param() shouldBe(15)

    "provide operators to control the simulation execution" in:
      simulation.runTill(()=> meanC1 < 10)

      val numIteration = 5
      var expectedValueOfC1 = valueOfC1

      (1 to numIteration).foreach(tick =>
        if simulation.shouldRun then
          sim.tick(tick)
      )

      meanC1 shouldBe 10
      simulation.shouldRun shouldBe false


    "provide operators to track and get statistics" in:
      simulation.track(meanC1).withLabel("Mean C1")
      val numIteration = 5
      var expectedValueOfC1 = valueOfC1

      (1 to numIteration).foreach(tick =>
        sim.tick(tick)
        expectedValueOfC1 = expectedValueOfC1 + tick
      )

      simulation.stats contains StatisticEntry("Mean C1", expectedValueOfC1)

    "provide operators to define and show a chart " in:
      simulation.chart(AvgC1).withLabel("AvgC1")
      val numIteration = 5

      var expectedList: List[Point] = List.empty

      (1 to numIteration).foreach(tick =>
        sim.tick(tick)
        AvgC1.add(tick, meanC1)
        expectedList = expectedList :+ Point(tick, valueOfC1 + tick)
      )

      simulation.report.map(e => e.points) contains expectedList



