package org.remus32.xkcd.tasks

import java.io.IOException

import org.apache.commons.io.FileUtils
import org.remus32.xkcd.{Task, Util}

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
      val cache = Util.cache()
      FileUtils.deleteDirectory(cache)
      cache.mkdir()
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
