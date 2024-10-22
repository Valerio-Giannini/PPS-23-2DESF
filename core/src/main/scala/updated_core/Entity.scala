package updated_core

import scala.collection.immutable.HashMap
import scala.reflect.ClassTag

sealed trait Entity:
  def id: Int

object Entity:

  def apply(): Entity =
    SimpleEntity(IdGenerator.nextId())



  private final case class SimpleEntity(ID: Int)
      extends Entity:

    def id: Int = this.ID

  private object IdGenerator:
    private var currentId: Int = 0

    def nextId(): Int =
      currentId += 1
      currentId
