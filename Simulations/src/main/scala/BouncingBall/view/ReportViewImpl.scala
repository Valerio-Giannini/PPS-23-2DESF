package BouncingBall.view

import mvc.view.ReportView
import mvc.model.{Point, ReportEntry}

import com.raquo.laminar.api.L._
import com.raquo.laminar.api.L.svg.{fontSize => svgFontSize}
import com.raquo.laminar.api.L.{Div, backgroundColor, border, borderRadius, children, div, padding, position as htmlPosition, render, right, span, top, display as htmlDisplay, height as htmlHeight, width as htmlWidth}
import com.raquo.laminar.api.L.svg.{fill, points, polyline, stroke, strokeWidth, svg, viewBox, height as svgHeight, width as svgWidth, x, y, text, textAnchor, x1, y1, x2, y2, line, transform}
import org.scalajs.dom

/**
 * Implementation of the `ReportView` trait for rendering graphical reports.
 *
 * This class provides a detailed graphical representation of simulation results using
 * SVG elements. It supports rendering multiple reports dynamically, including axes,
 * labels, and data points. The class uses Laminar to manage rendering and state updates.
 */
class ReportViewImpl extends ReportView:

  private var reportEntries: List[ReportEntry] = _

  override def init(infos: List[ReportEntry]): Unit =
    reportEntries = infos

  override def show(): Unit =
    val container = dom.document.getElementById("report-container")
    container.innerHTML = ""
    val graphs = reportEntries.map(entry => _renderSingleReport(entry))
    val allGraphsDiv = div(graphs)
    render(container, allGraphsDiv)

  override def close(): Unit =
    val container = dom.document.getElementById("report-container")
    container.innerHTML = ""

  /**
   * Renders a single report graph based on the provided data entry.
   *
   * Constructs an SVG-based graphical representation of the report, including:
   * - Data points connected by a polyline.
   * - Scaled axes with labels.
   * - Optional axis names.
   *
   * @param entry the `ReportEntry` containing data points and optional axis labels.
   * @return a Laminar `Div` element containing the rendered SVG graph.
   */
  private def _renderSingleReport(entry: ReportEntry): Div =
    val colors = List("red", "blue", "green", "purple", "orange", "brown", "pink", "cyan", "magenta", "yellow")

    val pointsList = entry.points
    val minX = pointsList.map(_.x.toString.toDouble).min
    val maxX = pointsList.map(_.x.toString.toDouble).max
    val minY = pointsList.map(_.y.toString.toDouble).min
    val maxY = pointsList.map(_.y.toString.toDouble).max

    /**
     * Scales the X-coordinate of a data point for rendering within the graph dimensions.
     *
     * @param x the X-coordinate of the data point.
     * @return the scaled X-coordinate.
     */
    def scaleX(x: Double): Double = ((x - minX) / (maxX - minX)) * 500

    /**
     * Scales the Y-coordinate of a data point for rendering within the graph dimensions.
     *
     * @param y the Y-coordinate of the data point.
     * @return the scaled Y-coordinate.
     */
    def scaleY(y: Double): Double = 380 - ((y - minY) / (maxY - minY)) * 380

    val color = colors(reportEntries.indexOf(entry) % colors.length)

    val graphLine = polyline(
      points := pointsList.map(point =>
        s"${scaleX(point.x.toString.toDouble)},${scaleY(point.y.toString.toDouble)}"
      ).mkString(" "),
      stroke := color,
      fill := "none",
      strokeWidth := "2"
    )

    val axisLines = List(
      line(
        x1 := "0", y1 := "380", x2 := "500", y2 := "380",
        stroke := "black", strokeWidth := "1"
      ),
      line(
        x1 := "0", y1 := "0", x2 := "0", y2 := "380",
        stroke := "black", strokeWidth := "1"
      )
    )

    val xAxisLabels = (0 to 10).map: i =>
      val xPos = i * (500.0 / 10)
      val labelValue = minX + i * ((maxX - minX) / 10)
      text(
        x := xPos.toString, y := "400",
        f"$labelValue%.2f",
        svgFontSize := "10px",
        textAnchor := "middle"
      )

    val yAxisLabels = (0 to 10).map: i =>
      val yPos = 380 - i * (380.0 / 10)
      val labelValue = minY + i * ((maxY - minY) / 10)
      text(
        x := "-10", y := yPos.toString,
        f"$labelValue%.2f",
        svgFontSize := "10px",
        textAnchor := "end"
      )

    val xAxisName = entry.labelX.map: label =>
      text(
        x := "250", y := "430",
        label,
        svgFontSize := "12px",
        textAnchor := "middle"
      )

    val yAxisName = entry.labelY.map: label =>
      text(
        x := "-40", y := "190",
        label,
        svgFontSize := "12px",
        textAnchor := "middle",
        transform := "rotate(-90 -40 190)"
      )

    val svgGraph = svg(
      svgWidth := "500px",
      svgHeight := "400px",
      viewBox := "-50 -20 600 450",
      axisLines,
      graphLine,
      xAxisLabels,
      yAxisLabels,
      xAxisName.toSeq,
      yAxisName.toSeq
    )

    div(
      htmlPosition := "relative",
      htmlWidth := "500px",
      htmlHeight := "450px",
      border := "1px solid #ccc",
      svgGraph
    )
