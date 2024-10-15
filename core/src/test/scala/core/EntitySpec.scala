package core

import core.Entity.ID
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class EntitySpec extends AnyWordSpec with Matchers:

  def extractIDFromEntity(id: ID): Int = id.asInstanceOf[Int]

  "An Entity" should:
    "assign ID starting from 1" in:
      val entity = Entity()
      extractIDFromEntity(entity.id) shouldEqual 1
    "have incrementing ID" in:
      val entities = List.fill(100)(Entity())
      val ids      = entities.map(e => extractIDFromEntity(e.id))
      ids shouldEqual ids.sorted
    "have a unique ID" in:
      val entities = List.fill(100)(Entity())
      val ids      = entities.map(e => extractIDFromEntity(e.id))
      ids.distinct.length shouldEqual ids.length
