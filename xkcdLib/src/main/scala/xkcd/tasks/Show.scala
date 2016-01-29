package xkcd.tasks

/**
  * Created by remus32 on 24/01/16.
  */
object Show extends Task {
  /**
    * Run task
    *
    * @param args arguments
    * @return Success indicator, true if task completed successfully
    */
  def run(args: Array[String]): Boolean = {
    args.length match {
      case 1 =>
        println("Usage: show <comic id>")
        false
      case 2 =>
        val comic = Comic(Comic.resolveComic(args(1)))
        val img = comic.image
        Gui.showImage(img, 0, 0)
        true
    }
  }

  /**
    * Task call name
    */
  def name: String = "show"
}
