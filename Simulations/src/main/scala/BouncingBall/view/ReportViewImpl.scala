package BouncingBall.view

import mvc.view.ReportView
import mvc.model.{Point, ReportEntry}

import com.raquo.laminar.api.L._
import com.raquo.laminar.api.L.svg.{fontSize => svgFontSize}
import com.raquo.laminar.api.L.{Div, backgroundColor, border, borderRadius, children, div, padding, position as htmlPosition, render, right, span, top, display as htmlDisplay, height as htmlHeight, width as htmlWidth}
import com.raquo.laminar.api.L.svg.{fill, points, polyline, stroke, strokeWidth, svg, viewBox, height as svgHeight, width as svgWidth, x, y, text, textAnchor, x1, y1, x2, y2, line}
import org.scalajs.dom

class ReportViewImpl extends ReportView:
  private var reportEntries: List[ReportEntry] = _

  override def init(infos: List[ReportEntry]): Unit =
    reportEntries = infos

  override def show(): Unit =
    val container = dom.document.getElementById("report-container")
    container.innerHTML = "" // Cancella il contenuto precedente
    val graphs = reportEntries.map(entry => _renderSingleReport(entry))
    val allGraphsDiv = div(graphs) // Passa la lista di grafici direttamente come bambini
    render(container, allGraphsDiv)

  override def close(): Unit =
    val container = dom.document.getElementById("report-container")
    container.innerHTML = ""

  private def _renderSingleReport(entry: ReportEntry): Div =
    val colors = List("red", "blue", "green", "purple", "orange", "brown", "pink", "cyan", "magenta", "yellow")

    // Estrai i punti e calcola min e max per scalare i valori
    val pointsList = entry.data.points
    val minX = pointsList.map(_.x.toString.toDouble).min
    val maxX = pointsList.map(_.x.toString.toDouble).max
    val minY = pointsList.map(_.y.toString.toDouble).min
    val maxY = pointsList.map(_.y.toString.toDouble).max

    // Funzioni per scalare i valori x e y
    def scaleX(x: Double): Double = ((x - minX) / (maxX - minX)) * 500
    def scaleY(y: Double): Double = 400 - ((y - minY) / (maxY - minY)) * 400 // Inverti Y per posizionare l'asse in basso

    // Crea una linea SVG per i punti di questo entry
    val color = colors(reportEntries.indexOf(entry) % colors.length)
    val graphLine = polyline(
      points := pointsList.map(point =>
        s"${scaleX(point.x.toString.toDouble)},${scaleY(point.y.toString.toDouble)}"
      ).mkString(" "),
      stroke := color,
      fill := "none",
      strokeWidth := "2"
    )

    // Creazione degli assi con valori
    val axisLines = List(
      line(
        x1 := "0", y1 := "400", x2 := "500", y2 := "400",
        stroke := "black", strokeWidth := "1" // Asse X
      ),
      line(
        x1 := "0", y1 := "0", x2 := "0", y2 := "400",
        stroke := "black", strokeWidth := "1" // Asse Y
      )
    )

    // Aggiungi valori agli assi X
    val adjustedMaxX = if ( (maxX - minX) % 5 != 0 )
      maxX + (5 - (maxX - minX) % 5) // Arrotonda maxX al prossimo multiplo di 5
    else
      maxX

    val step = ((adjustedMaxX - minX) / 5).toInt

    val xAxisLabels = (0 to 5).map { i =>
      val xPos = i * (500.0 / 5)
      val labelValue = minX.toInt + i * step
      text(
        x := xPos.toString, y := "420",
        labelValue.toString,
        svgFontSize := "10px",
        textAnchor := "middle"
      )
    }

    // Aggiungi valori agli assi Y
    val yAxisLabels = (0 to 5).map { i =>
      val yPos = 400 - i * (400.0 / 5)
      val labelValue = minY + i * ((maxY - minY) / 5)
      val formattedLabel = f"$labelValue%.2f" // Tronca a due cifre decimali

      text(
        x := "-20", y := yPos.toString,
        formattedLabel,
        svgFontSize := "10px",
        textAnchor := "start"
      )
    }

    // Crea l'elemento SVG con la linea del grafico, assi, e valori
    val svgGraph = svg(
      svgWidth := "500px",
      svgHeight := "400px",
      viewBox := "-30 0 550 450",
      axisLines,
      graphLine,
      xAxisLabels,
      yAxisLabels
    )

    // Creazione della legenda per questo grafico
    val legend = div(
      htmlPosition := "absolute",
      top := "10px",
      right := "10px",
      backgroundColor := "rgba(255, 255, 255, 0.8)",
      padding := "5px",
      border := "1px solid #ccc",
      borderRadius := "5px",
      div(
        span(
          htmlWidth := "10px",
          htmlHeight := "10px",
          backgroundColor := color,
          htmlDisplay := "inline-block",
          marginRight := "5px"
        ),
        span(entry.label)
      )
    )

    // Container principale con il grafico SVG e la legenda
    div(
      htmlPosition := "relative",
      htmlWidth := "500px",
      htmlHeight := "450px",
      border := "1px solid #ccc",
      svgGraph,
      legend
    )
