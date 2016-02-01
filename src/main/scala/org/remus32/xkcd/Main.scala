
package org.remus32.xkcd

import com.typesafe.scalalogging.LazyLogging

/**
  * Main application object
  *
  * @since 1.0
  */
object Main extends App with LazyLogging {
  /**
    * List of executable tasks
    */
  lazy val taskList: List[Task] = List[Task](
    tasks.Quit,
    tasks.Clean,
    tasks.Show,
    tasks.Info,
    tasks.Search,
    tasks.Archive
  )

  /**
    * Init system
    */
  private def init() = {
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace")
    Util.init()
  }

  try {
    init()
    logger.info("Getting latest comic...")
    Comic.latest
    Gui(call = (call: String) => {
      try {
        val split = call.split(" ")
        val first = split(0)
        var matched = false
        val startTime = System.currentTimeMillis()
        var success = false
        taskList.foreach((f) => {
          if (f.name == first) {
            matched = true
            success = f.run(split)
          }
        })
        val endTime = System.currentTimeMillis()
        val time = (endTime - startTime) / 1000F
        success match {
          case true => logger.info(s"Successfully done '$call' in $time s.")
          case false => logger.error(s"Done '$call' with errors in $time s.")
        }
        if (!matched) {
          logger.error(s"Task $first does not exist")
        }
      } catch {
        case e: Exception => logger.error("Error in:", e)
        case e: ComicNotFoundException =>
          val id = e.id
          logger.error(s"Comic $id not found")
        case e: BadComicIdException =>
          val id = e.id
          logger.error(s"$id is not a valid comic identifier")
        case e: ComicNotFoundException =>
          logger.error(s"HTTP 404: Comic not found")
      }

    })
  } catch {
    case e: Exception => logger.error("Error in:", e)
  }
}
