package view.init

  // Definizione di ViewParameter per gestire i parametri con id, value, minValue, maxValue e label opzionali
case class ViewParameter(
                          label: String,
                          value: AnyVal,
                          minValue: Option[AnyVal] = None,
                          maxValue: Option[AnyVal] = None
                        )


