package BouncingBall.view

import mvc.view.ReportView
import mvc.model.{Point, ReportEntry}

import com.raquo.laminar.api.L._
import com.raquo.laminar.api.L.svg.{fontSize => svgFontSize}
import com.raquo.laminar.api.L.{Div, backgroundColor, border, borderRadius, children, div, padding, position as htmlPosition, render, right, span, top, display as htmlDisplay, height as htmlHeight, width as htmlWidth}
import com.raquo.laminar.api.L.svg.{fill, points, polyline, stroke, strokeWidth, svg, viewBox, height as svgHeight, width as svgWidth, x, y, text, textAnchor, x1, y1, x2, y2, line, transform}
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

    println("DEBUGGINO")
    println(entry.points)

    // Estrai i punti e calcola min e max per scalare i valori
    val pointsList = entry.points
    val minX = pointsList.map(_.x.toString.toDouble).min
    val maxX = pointsList.map(_.x.toString.toDouble).max
    val minY = pointsList.map(_.y.toString.toDouble).min
    val maxY = pointsList.map(_.y.toString.toDouble).max

    // Funzioni per scalare i valori x e y
    def scaleX(x: Double): Double = ((x - minX) / (maxX - minX)) * 500
    def scaleY(y: Double): Double = 380 - ((y - minY) / (maxY - minY)) * 380 // Spazio per mostrare l'ultimo valore

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
        x1 := "0", y1 := "380", x2 := "500", y2 := "380",
        stroke := "black", strokeWidth := "1" // Asse X
      ),
      line(
        x1 := "0", y1 := "0", x2 := "0", y2 := "380",
        stroke := "black", strokeWidth := "1" // Asse Y
      )
    )

    // Etichette sugli assi con 10 divisioni
    val xAxisLabels = (0 to 10).map { i =>
      val xPos = i * (500.0 / 10)
      val labelValue = minX + i * ((maxX - minX) / 10)
      text(
        x := xPos.toString, y := "400",
        f"$labelValue%.2f",
        svgFontSize := "10px",
        textAnchor := "middle"
      )
    }

    val yAxisLabels = (0 to 10).map { i =>
      val yPos = 380 - i * (380.0 / 10)
      val labelValue = minY + i * ((maxY - minY) / 10)
      text(
        x := "-10", y := yPos.toString,
        f"$labelValue%.2f",
        svgFontSize := "10px",
        textAnchor := "end"
      )
    }

    // Nome degli assi
    val xAxisName = entry.labelX.map { label =>
      text(
        x := "250", y := "430",
        label,
        svgFontSize := "12px",
        textAnchor := "middle"
      )
    }

    val yAxisName = entry.labelY.map { label =>
      text(
        x := "-40", y := "190",
        label,
        svgFontSize := "12px",
        textAnchor := "middle",
        transform := "rotate(-90 -40 190)"
      )
    }

    // Crea l'elemento SVG con la linea del grafico, assi, valori e nomi
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

    // Container principale con il grafico SVG
    div(
      htmlPosition := "relative",
      htmlWidth := "500px",
      htmlHeight := "450px",
      border := "1px solid #ccc",
      svgGraph
    )
