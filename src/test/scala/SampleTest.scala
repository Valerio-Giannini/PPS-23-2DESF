import org.scalatest.flatspec.AnyFlatSpec

class SampleTest extends AnyFlatSpec {

  "A List" should "contains only 1 and 2! In this example at least" in {
    val stack = List(1, 2)
    assert(stack === List(1, 2))
    // assertResult(List(1, 4), "Wait am i wrong?") { stack }
  }

}
