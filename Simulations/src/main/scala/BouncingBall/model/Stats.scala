package BouncingBall.model

import BouncingBall.model.GlobalParameters.{ballRadius, borderSize, deceleration}
import core.{Entity, World}
import dsl.DSL.*

object Stats:
  private def _movingBalls(world: World): Iterable[Entity] =
    for
      entity <- from(world).entitiesHaving(SPEED)
      speed  <- from(world).componentsOf(entity).get[Speed]
      if speed.vx != 0 || speed.vy != 0
    yield entity

  def numberOfMovingBalls(using world: World): Int = _movingBalls(world).size

  def numberOfStoppedBalls(using world: World): Int = from(world).entitiesHaving(SPEED).size - _movingBalls(world).size

  def calcAvgSpeed(using world: World): Double =
    val speeds = for
      entity <- _movingBalls(world)
      speed <- from(world).componentsOf(entity).get[Speed]
    yield math.sqrt(speed.vx * speed.vx + speed.vy * speed.vy)
    if speeds.nonEmpty then speeds.sum / speeds.size else 0