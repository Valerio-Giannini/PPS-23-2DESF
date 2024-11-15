package BouncingBall.view

import mvc.view.ReportView
import mvc.model.{Point, ReportEntry}
import com.raquo.laminar.api.L.*
import com.raquo.laminar.api.L.{Div, Val, backgroundColor, border, borderRadius, children, div, padding, position, render, right, span, top, display as htmlDisplay, height as htmlHeight, width as htmlWidth}
import com.raquo.laminar.api.L.svg.{fill, points, polyline, stroke, strokeWidth, svg, viewBox, height as svgHeight, width as svgWidth}
import org.scalajs.dom

class ReportViewImpl extends ReportView:
  private var reportEntries: List[ReportEntry] = _

  override def init(infos: List[ReportEntry]): Unit =
    reportEntries = infos

  override def show(): Unit =
    val container = dom.document.getElementById("report-container")
    val renderInfos = _renderReport(reportEntries)
    render(container, renderInfos)

  override def close(): Unit =
    val container = dom.document.getElementById("report-container")
    container.innerHTML = ""

  private def _renderReport(entries: List[ReportEntry]): Div = {
    val colors = List("red", "blue", "green", "purple", "orange", "brown", "pink", "cyan", "magenta", "yellow")

    // Estrai tutti i punti e calcola min e max per scalare i valori
    val allPoints = entries.flatMap(_.data.points)
    val minX = allPoints.map(_.x.toString.toDouble).min
    val maxX = allPoints.map(_.x.toString.toDouble).max
    val minY = allPoints.map(_.y.toString.toDouble).min
    val maxY = allPoints.map(_.y.toString.toDouble).max

    // Funzioni per scalare i valori x e y
    def scaleX(x: Double): Double = ((x - minX) / (maxX - minX)) * 500
    def scaleY(y: Double): Double = 400 - ((y - minY) / (maxY - minY)) * 400 // Inverti Y per posizionare l'asse in basso

    // Aggiunge un colore unico per ogni serie
    val coloredEntries = entries.zipWithIndex.map { case (entry, index) =>
      val color = colors(index % colors.length)
      (entry, color)
    }

    // Crea una linea SVG per ogni serie di dati
    val graphLines = coloredEntries.map { case (entry, color) =>
      polyline(
        points := entry.data.points.map { point =>
          s"${scaleX(point.x.toString.toDouble)},${scaleY(point.y.toString.toDouble)}"
        }.mkString(" "),
        stroke := color,
        fill := "none",
        strokeWidth := "2"
      )
    }

    // Crea l'elemento SVG con le linee del grafico
    val svgGraph = svg(
      svgWidth := "500px",
      svgHeight := "400px",
      viewBox := "0 0 500 400",
      graphLines
    )

    // Creazione della legenda
    val legend = div(
      position := "absolute",
      top := "10px",
      right := "10px",
      backgroundColor := "rgba(255, 255, 255, 0.8)",
      padding := "5px",
      border := "1px solid #ccc",
      borderRadius := "5px",
      children <-- Val(
        coloredEntries.map { case (entry, color) =>
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
        }
      )
    )

    // Container principale con il grafico SVG e la legenda
    div(
      position := "relative",
      htmlWidth := "500px",
      htmlHeight := "400px",
      border := "1px solid #ccc",
      svgGraph,
      legend
    )
  }
