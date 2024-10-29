package dsl.coreDSL

import core.*

trait CoreDSL:
  def newWorld: World
  def from(world: World): From
  def into(world: World): Into
  def reset(world: World): World
  def update(world: World): Unit
  def outerWorld: OuterWorld

object CoreDSL extends CoreDSL:
  override def newWorld: World = World()
  override def from(world: World): From = From(world)

  override def into(world: World): Into = Into(world)

  override def outerWorld: OuterWorld = OuterWorld()

  override def reset(world: World): World = world.clearEntities()

  override def update(world: World): Unit = world.update()
