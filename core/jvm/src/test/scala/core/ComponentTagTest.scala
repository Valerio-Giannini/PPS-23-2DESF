package core

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


import scala.quoted.{Quotes, Type}

class ComponentTagTest extends AnyWordSpec with Matchers:
  val c1Tag: ComponentTag[C1] = summon[ComponentTag[C1]]
  
  "ComponentTag" should:
    "be derived for valid Component subtypes" in:
      c1Tag should not be null
      
    "throw an error if derived for the base Component type" in:
      assertDoesNotCompile("summon[ComponentTag[Component]]")
      
    "return correct toString representation" in:
      c1Tag.toString shouldEqual "core.C1"
      
    "return correct hashCode" in:
      c1Tag.hashCode shouldEqual "core.C1".hashCode
      
    "compare equality correctly" in:
      val c1Tag2 = summon[ComponentTag[C1]]
      val c2Tag  = summon[ComponentTag[C2]]

      c1Tag shouldEqual c1Tag2
      c1Tag should not equal c2Tag


