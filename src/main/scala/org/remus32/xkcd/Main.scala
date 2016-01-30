
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

  init()
  logger.info("Getting latest comic...")
  Comic.latest
  Gui(call = (call: String) => {
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
  })
}
