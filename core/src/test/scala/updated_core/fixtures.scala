package updated_core

case class C1(x: Int) extends Component
case class C2(x: Int) extends Component
case class C3(x: Int) extends Component

class IncrementC1System extends System:

  def update(world: World): Unit =
    world
      .entitiesWithAtLeastComponents(ComponentTag[C1])
      .foreach(e => world.addComponent(e, C1(e.get[C1].get.x + 1)))
