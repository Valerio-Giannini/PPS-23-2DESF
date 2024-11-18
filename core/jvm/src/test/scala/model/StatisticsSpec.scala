package model

import mvc.model.{Condition, Statistic, StatisticEntry}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class StatisticsSpec  extends AnyWordSpec with Matchers with BeforeAndAfterEach:
  var statistic: Statistic = _

  override def beforeEach(): Unit =
    statistic = Statistic()

  "A statistic" should:
    "initially have an empty snapshot" in:
      statistic.snapshot shouldBe List.empty
    "allow adding entries and retrieve them in the snapshot" in:
      val Statistic1 = StatisticEntry("s1", 50)
      val Statistic2 = StatisticEntry("s2", 100)

      statistic.addStatisticEntry(Statistic1.label, Statistic1.value)
      statistic.addStatisticEntry(Statistic2.label, Statistic2.value)

      val snapshot = statistic.snapshot

      snapshot should have size 2
      snapshot shouldBe List(Statistic1, Statistic2)
    "evaluate lazy values correctly" in:
      var counter = 0
      val Statistic = StatisticEntry("s1", {
        counter
      })

      statistic.addStatisticEntry(Statistic.label, Statistic.value)

      val numIteration = 5

      (1 to numIteration).foreach(tick =>
        counter = counter + tick
      )

      statistic.snapshot contains StatisticEntry(Statistic.label, counter)



