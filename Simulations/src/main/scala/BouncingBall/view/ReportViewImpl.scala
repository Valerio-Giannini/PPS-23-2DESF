package BouncingBall.view

import mvc.view.ReportView
import com.raquo.laminar.api.L.*
import mvc.model.{DataTrackerEntry, Point, ReportEntry}
import org.scalajs.dom

class ReportViewImpl extends ReportView:
  var reportInfos: List[ReportEntry] = _

  override def show(): Unit =
    val container = dom.document.getElementById("report-container")
    val renderInfos = _renderReport(reportInfos)
    render(container, renderInfos)

  override def close(): Unit =
    val container = dom.document.getElementById("report-container")
    container.innerHTML = ""

  override def init(infos: List[ReportEntry]): Unit =
    reportInfos = infos

  def _renderReport(infos: List[ReportEntry]): Div =
    val colors = List("red", "blue", "green", "purple", "orange", "brown", "pink", "cyan", "magenta", "yellow")

    // Genera un colore diverso per ogni elemento fino a esaurimento della lista di colori
    val coloredInfos = infos.zipWithIndex.map { (entry, index) =>
      val color = colors(index % colors.length)
      (entry.label, entry.data, color)
    }

    // Funzione per creare una serie di punti rappresentati da `div`
    def createGraphLine(data: List[Point], color: String): List[Div] =
      data.map { point =>
        div(
          position := "absolute",
          bottom := s"${point.y.toString.toDouble}px", // Posiziona il punto in base alla coordinata y
          left := s"${point.x.toString.toDouble}px", // Posiziona il punto in base alla coordinata x
          width := "5px",
          height := "5px",
          backgroundColor := color,
          borderRadius := "50%" // Rende il punto circolare
        )
      }

    // Creazione del contenitore principale del grafico con i punti
//    val graphContent = coloredInfos.flatMap { reportEntry =>  createGraphLine(reportEntry., color)}
    val graphContent = coloredInfos.flatMap { case (_, data, color) => createGraphLine(data.points, color) }

    // Creazione del contenitore principale con i punti aggiunti dinamicamente
    val graph = div(
      position := "relative",
      width := "500px",
      height := "400px",
      backgroundColor := "white",
      border := "1px solid black",
      children <-- Val(graphContent)
    )

    // Creazione della legenda in alto a destra
    val legend = div(
      position := "absolute",
      top := "10px",
      right := "10px",
      backgroundColor := "rgba(255, 255, 255, 0.8)",
      padding := "5px",
      border := "1px solid #ccc",
      borderRadius := "5px",
      children <-- Val(
        coloredInfos.map { case (label, _, color) =>
          div(
            span(
              width := "10px",
              height := "10px",
              backgroundColor := color,
              display := "inline-block",
              marginRight := "5px"
            ),
            span(label)
          )
        }
      )
    )

    // Container principale che combina il grafico e la legenda
    div(
      position := "relative",
      width := "500px",
      height := "400px",
      border := "1px solid #ccc",
      graph,
      legend
    )
