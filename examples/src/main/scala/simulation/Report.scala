package simulation

class DataTracker[X, Y]:
  private var data: List[(X, Y)] = List.empty

  def updateData(x: X, y: Y): Unit =
    data = data :+ (x, y)

  def showData(label: String): Unit =
    println(s"Data: $label")
    for (x, y) <- data
    do println(s"x: $x, y: $y")

trait Report:
    def getReport: List[(String, DataTracker[AnyVal, AnyVal])]

object Report:
  val avgSpeed = new DataTracker[Int, Double]
  val movingBalls = new DataTracker[Int, Int]

  def updateAvgSpeed(value: Double)(using tick: Int = 0): Unit =
    avgSpeed.updateData(tick, value)

  def updateMovingBalls(value: Int)(using tick: Int = 0): Unit =
    movingBalls.updateData(tick, value)

  def getAvgSpeed(): Unit =
    avgSpeed.showData("Average speed")

  def getMovingBalls(): Unit =
    movingBalls.showData("Moving balls")

  def getReport[X, Y]: List[(String, DataTracker[AnyVal, AnyVal])] =
    List(
      
    )


