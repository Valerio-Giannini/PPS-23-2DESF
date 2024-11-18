package core

case class C1(x: Int) extends Component
case class C2(x: Int) extends Component
case class C3(x: Int) extends Component

class IncrementC1System extends System:

  private def incrementC1(c1: C1): C1 =
    C1(c1.x + 1)

  def update(world: World): Unit =
    for
      entity <- world.entitiesWithAtLeastComponents[C1 ::: CNil]
      c1 <- entity.get[C1]
    do
      world.addComponent(entity, incrementC1(c1))
