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
  /**
   * Stores the parameters to be configured in this view.
   */
  var parameters: Iterable[ViewParameter] = _

  /**
   * Callback function to handle the configured parameters.
   */
  var results: Iterable[Parameter[_]] => Unit = _

  /**
   * Displays the parameter configuration view.
   *
   * Renders the configuration UI inside the "init-container" DOM element, allowing
   * users to input and validate simulation parameters interactively.
   */
  override def show(): Unit =
    val paramsConfig = _renderInit(parameters, results)
    val container = dom.document.getElementById("init-container")
    render(container, paramsConfig)

  /**
   * Closes the parameter configuration view.
   *
   * Clears the content of the "init-container" DOM element, resetting the UI state
   * and preparing it for potential reuse.
   */
  override def close(): Unit =
    val container = dom.document.getElementById("init-container")
    container.innerHTML = ""

  /**
   * Initializes the parameter view with the specified parameters.
   *
   * Sets up the view to display input fields for the given parameters and returns
   * a `Future` that resolves with the user-configured values upon completion.
   *
   * @param params an iterable collection of `ViewParameter` objects, each describing
   *               a parameter to be configured, including metadata like default values,
   *               minimum and maximum constraints, and labels.
   * @return a `Future` containing an iterable collection of configured `Parameter` objects.
   */
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

      /**
       * Attempts to validate all input fields.
       *
       * - Extracts raw values from the input fields.
       * - Converts the values to their expected types.
       * - Checks constraints (e.g., minimum, maximum, and type correctness).
       * - Provides real-time visual feedback by changing the input field's border color.
       *
       * @return a filtered collection of successfully validated parameters.
       */
      val maybeResults = inputFields.flatMap { (viewParam, inputBox) =>
        /**
         * Extracts the raw value from the input field.
         *
         * - Uses the placeholder value if the input field is empty.
         *
         * @return the raw value as a string.
         */
        val rawValue = if (inputBox.ref.value.isEmpty) inputBox.ref.placeholder else inputBox.ref.value
        /**
         * Parses the raw value to a `Double`, ensuring compatibility with numeric parameters.
         *
         * @return an optional `Double` value, or `None` if parsing fails.
         */
        val parsedValue = Try(rawValue.toDouble).toOption

        parsedValue match

          case Some(value) =>
            /**
             * Checks if the value meets the minimum constraint.
             *
             * @return `true` if the value is above or equal to the minimum, or if no minimum is defined.
             */
            val minCheck = viewParam.minValue.forall(min => value >= min.asInstanceOf[Double])
            /**
             * Checks if the value meets the maximum constraint.
             *
             * @return `true` if the value is below or equal to the maximum, or if no maximum is defined.
             */
            val maxCheck = viewParam.maxValue.forall(max => value <= max.asInstanceOf[Double])
            /**
             * Verifies if the value matches the expected type.
             *
             * - For `IntParameter`, ensures the parsed value is an integer.
             *
             * @return `true` if the type matches, `false` otherwise.
             */
            val correctType: Boolean = viewParam.parameter match
              case _: IntParameter => parsedValue.get == parsedValue.get.toInt
              case _ => true

            if minCheck && maxCheck && correctType then
              /**
               * Updates the input field's appearance to indicate success.
               */
              inputBox.amend(
                borderColor := "green",
                borderWidth := "1px"
              )

              /**
               * Returns the validated parameter encapsulated as a `Some` object.
               */
              Some(Parameter(value, viewParam.parameter.id).asInstanceOf[viewParam.parameter.type])
            else
              /**
               * Updates the input field's appearance to indicate an error.
               */
              inputBox.amend(
                borderColor := "red",
                borderWidth := "1px"
              )

              None

          case None =>
            /**
             * Updates the input field's appearance to indicate a parsing error.
             */
            inputBox.amend(
              borderColor := "red",
              borderWidth := "1px"
            )

            None

      }

      /**
       * Checks if all parameters are valid and triggers the `onSave` callback.
       *
       * - Ensures the number of validated parameters matches the total number of input fields.
       * - Passes the validated results to the `onSave` callback if all are valid.
       */
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

        /**
         * Placeholder for displaying error messages (currently unused).
         */
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
