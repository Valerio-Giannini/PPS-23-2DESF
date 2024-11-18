package core

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ComponentChainTagTest extends AnyFlatSpec with Matchers:

  "ComponentTag for Component" should "fail at compile-time" in:
    assertDoesNotCompile("summon[ComponentTag[Component]]")

  "ComponentChainTag for a chain with a single component" should "compile successfully" in:
    assertCompiles("summon[ComponentChainTag[C1 :: CNil]]")

  "ComponentChainTag for a chain with multiple components" should "compile successfully" in:
    assertCompiles("summon[ComponentChainTag[C1 :: C2 :: CNil]]")

  "ComponentChainTag for CNil" should "compile successfully" in:
    assertCompiles("summon[ComponentChainTag[CNil]]")
  


