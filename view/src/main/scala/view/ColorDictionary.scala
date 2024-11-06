package  view

def toColorString(color: Int): String =
  color match
    case 1 => "blue"
    case 2 => "yellow"
    case 3 => "red"
    case _ => "black"