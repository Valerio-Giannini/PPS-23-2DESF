package mvc.view

import core.Entity

trait SimulationView extends View:
  def update(entities: Iterable[Entity], statsInfos: List[(String, AnyVal)]): Unit
