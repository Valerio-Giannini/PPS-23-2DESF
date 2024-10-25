package core

import core._
import org.openjdk.jmh.annotations.*

import java.util.concurrent.TimeUnit



case class C1(value: Double) extends Component
case class C2(value: Double) extends Component

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
      .foreach(_ => world.createEntity(C1(1), C2(2)))

  @Benchmark
  def updateComponent(): Unit =
    for entity <- world.getEntities do
      (world.getComponent[C1](entity), world.getComponent[C2](entity)) match
        case (Some(c1), Some(c2)) =>
          world.addComponent(entity, c1.copy(value = c1.value + c2.value))
        case _ =>
