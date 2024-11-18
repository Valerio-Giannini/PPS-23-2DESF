package model

import mvc.model.{Condition, DataTracker, Point, Report, ReportEntry}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ReportSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach:
  var report: Report = _

  val pointA = Point(0, 0)
  val pointB = Point(1, 1)

  override def beforeEach(): Unit =
    report = Report()

  "A report" should:
    "be empty when initialized" in:
      report.data shouldBe List.empty
    "contain data trackers" which:
      "are empty when initialized" in:
        object chart extends DataTracker
      "contains points expressed as x and y coordinates" in:
        object chart extends DataTracker

        chart.add(pointA.x,pointA.y)
        chart.add(pointB.x,pointB.y)

        chart.points shouldBe List(pointA, pointB)
    "contain entries with a label, a data tacker ad the labels of the axes" in:
      object chart extends DataTracker
      chart.add(pointA.x, pointA.y)
      val label = "Data"
      val labelX = "x axis"
      val labelY = "y axis"
      report.addReportEntry(chart, ReportEntry(label, Some(labelX), Some(labelY)))

      report.data shouldBe List(ReportEntry(label, Some(labelX), Some(labelY), chart.points))
    "contain entries with only a label and a data tacker" in:
      object chart extends DataTracker
      chart.add(pointA.x, pointA.y)
      val label = "Data"
      report.addReportEntry(chart, ReportEntry(label))

      report.data contains ReportEntry(label, points = chart.points)
//
//      report.data shouldBe List(ReportEntry(label, DataTrackerEntry(points = chart.get)))


//    "contain data trackers" which:
//      "are empty when initialized" in:
//        Data1.get shouldBe empty
//      "contains x and y pairs" in:
//        Data1.add(pointA.x,pointA.y)
//        Data1.add(pointB.x,pointB.y)
//
//        Data1.get shouldBe List(pointA, pointB)
//    "show data trackers with a label" which:
//      "are empty when initialized" in:
//        report.addReportEntry(reportEntryForData1.label, Data1)
//        report.addReportEntry(reportEntryForData2.label, Data2)
//
//        report.data shouldBe List(reportEntryForData1, reportEntryForData2)
//
//      "contains the value of the data trackers" in:
//        report.addReportEntry(reportEntryForData1.label, Data1)
//        report.addReportEntry(reportEntryForData2.label, Data2)
//
//        Data1.add(pointA.x,pointA.y)
//        Data1.add(pointB.x,pointB.y)
//
//        Data2.add(pointA.x, pointA.y)
//        Data2.add(pointB.x, pointB.y)
//
//
//
//        report.data shouldBe List(
//          ReportEntry(reportEntryForData1.label, List(pointA, pointB)),
//          ReportEntry(reportEntryForData2.label, List(pointA, pointB))
//        )
//    "correctly calculate the mean of C1 components over multiple iterations" in :
//      object AvgC1 extends DataTracker
//      report.addReportEntry("C1 avg", AvgC1)
//      into(world).include(IncrementC1System())
//      val valueOfC1 = 5
//      val e = into(world).spawnNewEntityWith(C1(valueOfC1))
//      var expectedList: List[(Int, Double)] = List.empty
//      val numIteration = 5
//      (1 to numIteration).foreach(tick =>
//        update(world)
//        AvgC1.add(tick, from(world).componentsOf(e).get[C1].get.x)
//        expectedList = expectedList :+ (tick, valueOfC1 + tick)
//      )
//
//      AvgC1.get.map(dt => (dt.x, dt.y)) shouldBe expectedList