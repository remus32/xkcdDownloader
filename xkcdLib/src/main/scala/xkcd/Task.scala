package xkcd

/**
  * A launch task
  */
trait Task {
  /**
    * Run task
    *
    * @param args arguments
    * @return Success indicator, true if task completed successfully
    */
  def run(args: Array[String]): Boolean

  /**
    * Task call name
    */
  def name: String
}
