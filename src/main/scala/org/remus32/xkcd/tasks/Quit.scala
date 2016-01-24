package org.remus32.xkcd.tasks

import org.remus32.xkcd.Task

/**
  * Exits program
  */
object Quit extends Task {
  /**
    * Run task
    *
    * @param args arguments
    * @return Success indicator, true if task completed successfully
    */
  def run(args: Array[String]): Boolean = {
    System.exit(0)
    true
  }

  /**
    * Task call name
    */
  def name: String = "quit"
}
