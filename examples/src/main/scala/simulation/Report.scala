package simulation

class DataTracker[X, Y]:
  private var data: List[(X, Y)] = List.empty

  def updateData(x: X, y: Y): Unit =
    data = data :+ (x, y)

  def showData(label: String): Unit =
    println(s"Data: $label")
    for (x, y) <- data
    do println(s"x: $x, y: $y")

object Report:
  val avgSpeed = new DataTracker[Int, Double]
  val movingBalls = new DataTracker[Int, Int]

  def updateAvgSpeed(value: Double)(using tick: Int = 0): Unit =
    avgSpeed.updateData(tick, value)

  def updateMovingBalls(value: Int)(using tick: Int = 0): Unit =
    movingBalls.updateData(tick, value)

  def showAvgSpeed(): Unit =
    avgSpeed.showData("Average speed")

  def showMovingBalls(): Unit =
    movingBalls.showData("Moving balls")

