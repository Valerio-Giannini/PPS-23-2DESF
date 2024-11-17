package core

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ComponentChainTest extends AnyFlatSpec with Matchers:

  "A chain with CNil" should "allow adding a component" in:
    val componentChain = core.ComponentChain(C1(1))
    componentChain shouldEqual C1(1) :: CNil

  "A chain with two components" should "correctly build a chain" in:
    val componentChain = C1(1) :: C2(2)
    componentChain shouldEqual C1(1) :: C2(2) :: CNil

  "A ComponentChain" should "be iterable" in:
    val componentChain = C1(1) :: C2(2)
    componentChain.iterator.toList shouldEqual List(C1(1), C2(2))



