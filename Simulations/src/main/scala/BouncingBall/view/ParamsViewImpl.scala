package BouncingBall.view

import mvc.model.{DoubleParameter, IntParameter, Parameter, Parameters, ViewParameter}
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

    val inputFields = paramsList.map { param =>

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

              viewParam.parameter match
                case _: IntParameter => Some(IntParameter(value.toInt, viewParam.parameter.id))
                case _: DoubleParameter => Some(DoubleParameter(value, viewParam.parameter.id))

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
