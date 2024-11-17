package core

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ComponentChainTagTest extends AnyFlatSpec with Matchers:

  "Deriving ComponentTag for Component" should "fail at compile-time" in:
    assertDoesNotCompile("summon[ComponentTag[Component]]")

  "Deriving ComponentChainTag for a chain with a single component" should "compile successfully" in:
    assertCompiles("summon[ComponentChainTag[C1 :: CNil]]")

  "Deriving ComponentChainTag for a chain with multiple components" should "compile successfully" in:
    assertCompiles("summon[ComponentChainTag[C1 :: C2 :: CNil]]")

  "Deriving ComponentChainTag for CNil" should "compile successfully" in:
    assertCompiles("summon[ComponentChainTag[CNil]]")
  


