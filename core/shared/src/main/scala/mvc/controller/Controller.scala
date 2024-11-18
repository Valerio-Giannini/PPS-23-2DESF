package mvc.controller

import mvc.view.*

trait Controller:
  def start(): Unit
  def end(): Unit

