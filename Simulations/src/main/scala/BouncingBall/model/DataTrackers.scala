package BouncingBall.model

import mvc.model.DataTracker

private class TimeDataTracker extends DataTracker:
  def add(value: Double)(using tick: Int): Unit =
    add(tick, value)

object AvgSpeed extends TimeDataTracker

object MovingBalls extends TimeDataTracker
