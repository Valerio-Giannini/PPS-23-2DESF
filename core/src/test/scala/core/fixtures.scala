package core

case class C1(x: Int) extends Component
case class C2(x: Int) extends Component
case class C3(x: Int) extends Component

class IncrementC1System extends System:

  private def incrementC1(entity: Entity): Entity =
    entity.get[C1] match
      case Some(c1) => entity.add(C1(c1.x + 1))
      case None => entity

  def update(world: World): Unit =
    world.entitiesWithAtLeastComponents(ComponentTag[C1])
      .foreach(e => world.addComponent(e, incrementC1(e).get[C1].get))
