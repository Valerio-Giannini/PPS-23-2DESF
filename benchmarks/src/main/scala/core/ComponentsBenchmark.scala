package core

import core.*
import core.fixtures.SampleComponent
import org.openjdk.jmh.annotations.*

import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Threads(1)
@Fork(1)
@Warmup(iterations = 100, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 100, time = 200, timeUnit = TimeUnit.MILLISECONDS)

/** Benchmarks the performance of updating components.
  */
class ComponentsBenchmark:

  var world: World     = _
  val numEntities: Int = 10_000

  @Setup(Level.Iteration)
  def setup(): Unit =
    world = World()

    (1 to numEntities)
      .foreach(_ => world.createEntity(SampleComponent(1)))

  @Benchmark
  def updateComponent(): Unit =
    for entity <- world.getEntities do
      world.getComponent[SampleComponent](entity) match
      case Some(sampleComponent) =>
        world.addComponent(entity, sampleComponent.copy(sampleComponent.value + 1))
      case _ =>
