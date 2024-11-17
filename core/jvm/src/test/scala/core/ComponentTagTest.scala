package core

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


import scala.quoted.{Quotes, Type}

class ComponentTagTest extends AnyWordSpec with Matchers:

  "ComponentTag" should:
    "be derived for valid Component subtypes" in:
      val c1Tag = summon[ComponentTag[C1]]
      val c2Tag = summon[ComponentTag[C2]]

      c1Tag should not be null
      c2Tag should not be null
    "throw an error if derived for the base Component type" in:
      """summon[ComponentTag[Component]]""" shouldNot compile
    "return correct toString representation" in:
      val c1Tag = summon[ComponentTag[C1]]
      val c2Tag = summon[ComponentTag[C2]]

      c1Tag.toString shouldEqual "core.C1"
      c2Tag.toString shouldEqual "core.C2"
    "return correct hashCode" in:
      val c1Tag = summon[ComponentTag[C1]]
      val c2Tag = summon[ComponentTag[C2]]

      c1Tag.hashCode shouldEqual "core.C1".hashCode
      c2Tag.hashCode shouldEqual "core.C2".hashCode
    "compare equality correctly" in:
      val c1Tag1 = summon[ComponentTag[C1]]
      val c1Tag2 = summon[ComponentTag[C1]]
      val c2Tag  = summon[ComponentTag[C2]]

      c1Tag1 shouldEqual c1Tag2
      c1Tag1 should not equal c2Tag


