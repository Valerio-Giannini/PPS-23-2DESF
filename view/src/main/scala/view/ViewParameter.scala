package view

// Definizione di ViewParameter per gestire i parametri con id, value, minValue, maxValue e label opzionali
case class ViewParameter(
                          label: Option[String] = None,
                          value: AnyVal,
                          minValue: Option[AnyVal] = None,
                          maxValue: Option[AnyVal] = None
                        )


