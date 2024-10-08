package bouncing_ball

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class SampleTest extends AnyFlatSpec with Matchers:

  "A List" should "return the first element when head is invoked" in {
    val list = List(1, 2, 3)
    list.head shouldEqual 1
  }
