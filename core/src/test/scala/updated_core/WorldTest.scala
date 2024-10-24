package updated_core

import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class WorldTest extends AnyWordSpec with Matchers with BeforeAndAfterEach:

  var world: World = _

  override def beforeEach(): Unit =
    world = SimpleWorld()

  "A World" when:
    "initialized" should:
      "be empty" in:
        world.entities shouldBe empty