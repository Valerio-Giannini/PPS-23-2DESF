package simulation

import core.*
import dsl.DSL.*

object Stats:
  def movingBalls(using world: World): Iterable[Entity] =
    for
      entity <- from(world).entitiesHaving(SPEED)
      speed  <- from(world).componentsOf(entity).get[Speed]
      if speed.vx != 0 || speed.vy != 0
    yield entity

  def numberOfMovingBalls(using world: World): Int = movingBalls.size
  def numberOfStoppedBalls(using world: World): Int = from(world).entitiesHaving(SPEED).size - movingBalls.size

  def calcAvgSpeed(using world: World): Double =
    val speeds = for
      entity <- movingBalls
      speed  <- from(world).componentsOf(entity).get[Speed]
    yield math.sqrt(speed.vx * speed.vx + speed.vy * speed.vy)
    if speeds.nonEmpty then speeds.sum / speeds.size else 0
