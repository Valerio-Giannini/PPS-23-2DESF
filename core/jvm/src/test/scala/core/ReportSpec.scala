//package core
//
//import dsl.DSL.*
//import org.scalatest.BeforeAndAfterEach
//import org.scalatest.matchers.should.Matchers
//import org.scalatest.wordspec.AnyWordSpec
//
//class ReportSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach:
//
//  var world: World = _
//  var report: Report = _
//
//  object AvgC1 extends DataTracker
//
//
//  override def beforeEach(): Unit =
//    world = World()
//    report = Report
//
//  "A report" should :
//    "contain entries with a label and a set of value" in:
//      val label: String = "Value of speed"
//      val pointA = (0,0)
//      val pointB = (1,1)
//      val entry = ReportEntry(label, List(DataTrackerEntry(pointA._1,pointA._2), DataTrackerEntry(pointB._1,pointB._2)))
//
//      entry.label shouldBe label
//      entry.value.map(dt => (dt.x,dt.y)) shouldBe List(pointA, pointB)
//
//    "empty when initialized" in:
//      report.data shouldBe List.empty
//
//    "contain the set of value in a data trackers" in:
//      val label: String = "Value of speed"
//      object entry extends DataTracker {}
//      report.addReportEntry(label, entry)
//      report.data shouldBe List(ReportEntry(label, List()))
//
//    "contain the set of value in a data trackers with some value" in :
//      val label: String = "Value of speed"
//      object entry extends DataTracker {}
//      report.addReportEntry(label, entry)
//      entry.add(0, 1)
//      entry.add(0, 1)
//      report.data shouldBe List(ReportEntry(label, List()))
//
//    "correctly calculate the mean of C1 components over multiple iterations" in :
//      val valueOfC1 = 5
//
//      into(world).include(IncrementC1System())
//      val e = into(world).spawnNewEntityWith(C1(valueOfC1))
//      report.addReportEntry("c1", AvgC1)
//
////      println(from(world).componentsOf(e).get[C1].get.x)
//
//      val numIteration = 5
//      var expectedList: List[(Int, Double)] = List.empty
//
//      (1 to numIteration).foreach(tick =>
//        update(world)
//        AvgC1.add(tick, from(world).componentsOf(e).get[C1].get.x)
//        expectedList = expectedList :+ (tick, valueOfC1+tick)
//      )
////
////        println(AvgC1.get.map(dt => (dt.x,dt.y)))
//      AvgC1.get.map(dt => (dt.x,dt.y)) shouldBe expectedList
//
//
//
