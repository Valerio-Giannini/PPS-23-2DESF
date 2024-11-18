package model

import mvc.model.{DoubleParameter, IntParameter, Parameters, ViewParameter}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ParameterSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach:
  var parameters: Parameters = _

  override def beforeEach(): Unit =
    parameters = Parameters()

  "A Param" should:
      "store and retrieve an integer number" in:
        val intParam = IntParameter(10)
        intParam() shouldBe 10
      "store and retrieve a floating number" in:
        val doubleParam = DoubleParameter(5.5)
        doubleParam() shouldBe 5.5
      "allow updating its value" in:
        val param = IntParameter(10)
        val newValue = 20
        param.value = newValue

        param() shouldBe newValue
      "optionally have an id" in:
        val paramWithId = IntParameter(10, Some(1))

        val paramWithoutId = IntParameter(20)

        paramWithId.id shouldBe Some(1)
        paramWithoutId.id shouldBe None

  "Parameters" should:
    "be empty when initialized" in:
      parameters.getRequestedParams shouldBe empty
    "contain asked parameters" in:
      val param = IntParameter(10)
      val viewParam = ViewParameter(param, "Parameter Label")
      parameters.askParam(viewParam)

      parameters.getRequestedParams contains viewParam
    "can be asked" which:
      "requires a label" in:
        val param = IntParameter(10)
        val viewParam = ViewParameter(param, "Parameter Label")

        viewParam.parameter shouldBe param
        viewParam.label shouldBe "Parameter Label"
      "can have a range" in:
        val param = IntParameter(10)
        val viewParam = ViewParameter(param, "Parameter Label", Some(0), Some(100))

        viewParam.parameter shouldBe param
        viewParam.label shouldBe "Parameter Label"
        viewParam.minValue shouldBe Some(0)
        viewParam.maxValue shouldBe Some(100)

