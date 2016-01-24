package org.remus32.xkcd.tasks

import org.remus32.xkcd.{Comic, Task}

/**
  * Clean cache directory
  */
object Info extends Task {
  /**
    * Run task
    *
    * @param args arguments
    * @return Success indicator, true if task completed successfully
    */
  def run(args: Array[String]): Boolean = {
    args.length match {
      case 1 =>
        println("Usage: info <comic id>")
        false
      case 2 =>
        val comic = Comic(Comic.resolveComic(args(1)))
        print(comic.info)
        true
    }
  }

  /**
    * Task call name
    */
  def name: String = "info"
}
