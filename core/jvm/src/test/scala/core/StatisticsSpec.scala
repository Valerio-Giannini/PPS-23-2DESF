package core

import dsl.DSL.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

//object Stats extends Statistics{
//
//}


class StatisticsSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach:

  var world: World = _

  override def beforeEach(): Unit =
    world = World()

  def meanC1: Int =
    val c1s = for
      entity <- from(world).entitiesHaving(ComponentTag[C1])
      c1 <- from(world).componentsOf(entity).get[C1]
    yield c1.x


    c1s.sum / c1s.size

  //    println(c1s.sum / c1s.size)
  def meanOfTwoNumbers(x: Int, y: Int): Int = (x + y) / 2

  "Statistics" should :
    "correctly calculate the mean of C1 components over multiple iterations" in :
      val valueOfC1FirstEntity = 5
      val valueOfC1SecondEntity = 10

      into(world).spawnNewEntityWith(C1(valueOfC1FirstEntity))
      into(world).spawnNewEntityWith(C1(valueOfC1SecondEntity))

      Statistics.addStatisticEntry("c1 mean", meanC1)

      into(world).include(IncrementC1System())

      var expectedMean: List[Int] = List.empty
      var actualMean: List[Int] = List.empty

      val numIteration = 5

      (1 to numIteration).foreach(tick =>
        update(world)
        expectedMean = expectedMean :+ meanOfTwoNumbers(valueOfC1FirstEntity + tick, valueOfC1SecondEntity + tick)
        actualMean = actualMean :+ Statistics.statsSnapshot.find(_.label == "c1 mean").get.value.asInstanceOf[Int]

      )

      actualMean shouldBe expectedMean

