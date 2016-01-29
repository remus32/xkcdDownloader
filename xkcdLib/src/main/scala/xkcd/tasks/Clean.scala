package xkcd.tasks

import java.io.IOException

/**
  * Clean cache directory
  */
object Clean extends Task {
  /**
    * Run task
    *
    * @param args arguments
    * @return Success indicator, true if task completed successfully
    */
  def run(args: Array[String]): Boolean = {
    try {
      Main.clean()
      Main.cache.mkdir()
      true
    } catch {
      case e: IOException =>
        println("Error: ")
        e.printStackTrace()
        false
    }
  }

  /**
    * Task call name
    */
  def name: String = "clean"
}
