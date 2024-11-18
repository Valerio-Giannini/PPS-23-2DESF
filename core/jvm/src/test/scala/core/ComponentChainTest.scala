package core

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ComponentChainTest extends AnyFlatSpec with Matchers:

  "A ComponentChain" should "be created with a single component" in:
    val componentChain = core.ComponentChain(C1(1))
    componentChain shouldEqual C1(1) :: CNil

  "A ComponentChain" should "be created with more than on component" in:
    val componentChain = C1(1) :: C2(2)
    componentChain shouldEqual C1(1) :: C2(2) :: CNil
  
  "A ComponentChain" should "only be created with subtype of Component" in :
    assertDoesNotCompile("C1(1) :: Component")

  "A ComponentChain" should "be iterable" in:
    val componentChain = C1(1) :: C2(2)
    componentChain.iterator.toList shouldEqual List(C1(1), C2(2))
