package core

import org.openjdk.jmh.annotations.*

import java.util.concurrent.TimeUnit

case class C1(value: Double) extends Component
case class C2(value: Double) extends Component

@State(Scope.Thread)
@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Threads(1)
@Fork(1)
@Warmup(iterations = 100, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 100, time = 100, timeUnit = TimeUnit.MILLISECONDS)
class ComponentsUpdate:

  var world: World     = _
  val numEntities: Int = 10_000

  @Setup(Level.Iteration)
  def setup(): Unit =
    world = World()
    (1 to numEntities).foreach(_ => world.createEntity(C1(1) ::: C2(1)))

  @Benchmark
  def componentsUpdate(): Unit =
    for
      entity <- world.entitiesWithAtLeastComponents[C1 ::: C2 ::: CNil]
      c1 <- entity.get[C1]
      c2 <- entity.get[C2]
    do
      world.addComponent(entity, C1(c1.value + c2.value))

