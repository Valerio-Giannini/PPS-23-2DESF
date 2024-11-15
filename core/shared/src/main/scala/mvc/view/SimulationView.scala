package mvc.view

import core.Entity
import mvc.model.StatisticEntry

trait SimulationView extends View:
  def update(entities: Iterable[Entity], statsInfos: List[StatisticEntry]): Unit
