package model

import mvc.model.Condition
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ConditionSpec extends AnyWordSpec with Matchers:
  "A Condition" should:
    "evaluate to true by default" in:
      val condition = Condition()
      condition.evaluate shouldBe true
    "evaluate a custom predicate function correctly" in:
      val condition = Condition()
      condition.setPredicate(() => 5 > 3)
      val trueCondition = condition.evaluate
      condition.setPredicate(() => 5 < 3)

      condition.evaluate shouldBe false
      trueCondition shouldBe true

