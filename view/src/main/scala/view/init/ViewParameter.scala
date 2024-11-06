package view.init

// Definizione di ViewParameter per gestire i parametri con id, value, minValue, maxValue e label opzionali
case class ViewParameter(
                          label: Option[String] = None,
                          value: AnyVal,
                          minValue: Option[AnyVal] = None,
                          maxValue: Option[AnyVal] = None
                        )

case class VisualParameter(
                          dimension: Option[Int] = Some(20),
                          color: Option[String] = Some("Blue"),
                        )
