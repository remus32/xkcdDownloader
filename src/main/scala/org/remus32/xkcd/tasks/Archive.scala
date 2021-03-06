package org.remus32.xkcd.tasks

import org.remus32.xkcd.{Comic, Task, Util}

/**
  * Clean cache directory
  */
object Archive extends Task {
  /**
    * Run task
    *
    * @param args arguments
    * @return Success indicator, true if task completed successfully
    */
  def run(args: Array[String]): Boolean = {
    args.length match {
      case 1 =>
        println("Usage: archive <comic id/from> [to]")
        false
      case 2 =>
        val comic = Comic(Comic.resolveComic(args(1)))
        val out = Util.out
        out.mkdir()
        comic.copyImageTo(out)
        true
      case 3 =>
        val first = Comic.resolveComic(args(1))
        val last = Comic.resolveComic(args(2))
        for (a <- first to last) {
          val comic = Comic(a)
          val out = Util.out()
          out.mkdir()
          comic.copyImageTo(out)
        }
        true
    }
  }

  /**
    * Task call name
    */
  def name: String = "archive"
}
