package mvc

trait Observer:
  def update(event: String): Unit // maybe we need to consider an enum of event, idk

trait Observable:
  private var observers: List[Observer] = List()

  def addObserver(observer: Observer): Unit =
    observers = observer :: observers

  def removeObserver(observer: Observer): Unit =
    observers = observers.filterNot(_ == observer)

  protected def notifyObservers(event: String): Unit =
    observers.foreach(_.update(event))
