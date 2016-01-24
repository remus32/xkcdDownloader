package org.remus32.xkcd.tasks

import org.remus32.xkcd.{Comic, Task}

/**
  * Clean cache directory
  */
object Search extends Task {
  /**
    * Run task
    *
    * @param args arguments
    * @return Success indicator, true if task completed successfully
    */
  def run(args: Array[String]): Boolean = {
    args.length match {
      case 1 =>
        println("Usage: search <regexp>")
        false
      case 2 =>
        val what = args(1)
        var count = 0
        Comic.searchTitles(what.r).foreach(x => {
          count = count + 1
          println(x.info)
        })
        println(s"Found $count matches")
        true
    }
  }

  /**
    * Task call name
    */
  def name: String = "search"
}
