package BouncingBall.view

import mvc.model.{IntParameter, Parameter, Parameters, ViewParameter}
import mvc.view.ParamsView
import org.scalajs.dom
import com.raquo.laminar.api.L.*

import scala.util.Try
import scala.concurrent.{Future, Promise}

/**
 * Implementation of the `ParamsView` trait for configuring simulation parameters.
 *
 * This class provides a user interface for inputting and validating parameters
 * dynamically with real-time feedback. It leverages Laminar for reactive updates
 * and supports asynchronous interactions through Futures and Promises.
 */
class ParamsViewImpl extends ParamsView:

  var parameters: Iterable[ViewParameter] = _

  var results: Iterable[Parameter[_]] => Unit = _

  override def show(): Unit =
    val paramsConfig = _renderInit(parameters, results)
    val container = dom.document.getElementById("init-container")
    render(container, paramsConfig)

  override def close(): Unit =
    val container = dom.document.getElementById("init-container")
    container.innerHTML = ""

  override def init(params: Iterable[ViewParameter]): Future[Iterable[Parameter[_]]] =
    parameters = params
    val promise = Promise[Iterable[Parameter[_]]]()
    val onSave: Iterable[Parameter[_]] => Unit = resultsParams => promise.success(resultsParams)
    results = onSave
    promise.future

  /**
   * Renders the input fields for parameter configuration.
   *
   * This method dynamically generates input fields for the specified parameters,
   * including real-time validation and visual feedback. It provides an "OK" button
   * to trigger the validation and save the results if all inputs are valid.
   *
   * @param paramsList the list of parameters to render.
   * @param onSave     a callback function to handle the validated parameter values.
   * @return a Laminar `Div` element containing the rendered input fields and a submission button.
   */
  private def _renderInit(
                   paramsList: Iterable[ViewParameter],
                   onSave: Iterable[Parameter[_]] => Unit
                 ): Div =

    /**
     * Generates input fields for each parameter in `paramsList`.
     *
     * Each input field is initialized with the parameter's default value as its placeholder.
     * The `value` attribute is set to the parameter's default value to prefill the input field.
     *
     * @note This collection pairs each `ViewParameter` with its corresponding Laminar `input` element,
     *       allowing the input fields to be referenced later for validation or state updates.
     * @example For a parameter with value `42`, an input field is created with
     *          `placeholder = "42"` and `value = "42"`.
     */
    val inputFields = paramsList.map { param =>

      /**
       * Creates a Laminar `input` element for the given parameter.
       *
       * - **`placeholder`**: Displays the parameter's default value when the input field is empty.
       * - **`value`**: Sets the input field's initial value to the parameter's default value.
       *
       * @return a Laminar `input` element pre-configured with the parameter's metadata.
       */
      val inputBox = input(
        placeholder := param.parameter.value.toString,
        value := param.parameter.value.toString
      )

      (param, inputBox)

    }.toList

    /**
     * Validates all user inputs and triggers the onSave callback if all values are valid.
     *
     * For each input field:
     * - Ensures the value satisfies the type, minimum, and maximum constraints.
     * - Provides real-time visual feedback by updating the border color (green for valid, red for invalid).
     * - Collects and returns the validated results through the callback.
     */
    def validateAll(): Unit =

      val maybeResults = inputFields.flatMap { (viewParam, inputBox) =>

        val rawValue = if (inputBox.ref.value.isEmpty) inputBox.ref.placeholder else inputBox.ref.value

        val parsedValue = Try(rawValue.toDouble).toOption

        parsedValue match

          case Some(value) =>

            val minCheck = viewParam.minValue.forall(min => value >= min.asInstanceOf[Double])

            val maxCheck = viewParam.maxValue.forall(max => value <= max.asInstanceOf[Double])

            val correctType: Boolean = viewParam.parameter match
              case _: IntParameter => parsedValue.get == parsedValue.get.toInt
              case _ => true

            if minCheck && maxCheck && correctType then
              inputBox.amend(
                borderColor := "green",
                borderWidth := "1px"
              )
              Some(Parameter(value, viewParam.parameter.id).asInstanceOf[viewParam.parameter.type])
            else
              inputBox.amend(
                borderColor := "red",
                borderWidth := "1px"
              )

              None

          case None =>
            inputBox.amend(
              borderColor := "red",
              borderWidth := "1px"
            )

            None

      }

      if maybeResults.size == paramsList.size then
        onSave(maybeResults)

    /**
     * Renders the UI for the parameter configuration, including input fields, constraints,
     * and a submission button.
     *
     * - Displays validation constraints (e.g., minimum and maximum values) next to input fields.
     * - Provides an "OK" button to trigger validation and submission.
     *
     * @return a Laminar `Div` containing all input fields and the "OK" button.
     */
    div(
      inputFields.map { (param, inputBox) =>

        val errorHandler = span()

        div(
          cls := "parameter-row",
          label(
            param.label,
            width := "100px",
            display := "inline-block",
            textAlign := "right"
          ),
          span(" | "),
          param.minValue.map(min =>
            span(
              s"Min: $min",
              width := "80px",
              display := "inline-block",
              textAlign := "center"
            )
          ),
          span(" | "),
          inputBox.amend(
            width := "150px",
            display := "inline-block"
          ),
          span(" | "),
          param.maxValue.map(max =>
            span(
              s"Max: $max",
              width := "80px",
              display := "inline-block",
              textAlign := "center"
            )
          ),
          span(" | "),
          errorHandler
        )

      }.toSeq :+ button(
        "OK",
        onClick --> (_ => validateAll())
      )
    )
