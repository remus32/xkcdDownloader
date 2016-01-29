
package xkcd

import java.io.File
import java.net.URL
import java.util.logging.{Level, Logger}

import com.google.gson.Gson
import com.typesafe.scalalogging.LazyLogging
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory

/**
  * Main application object
  *
  * @since 1.0
  */
object Main extends App with LazyLogging {
  /**
    * Gson instance
    */
  lazy val gson = new Gson()
  lazy val taskList: List[Task] = List[Task](
    tasks.Quit,
    tasks.Clean,
    tasks.Show,
    tasks.Info,
    tasks.Search,
    tasks.Archive
  )
  /**
    * Cache directory
    */
  lazy val cache = {
    val r = new File(cwd, "cache")
    if (!r.isDirectory) {
      loadCache()
    }
    r
  }
  /**
    * Xkcd address
    */
  val xkcdBase = "http://xkcd.com/"
  /**
    * Instance's current working directory
    */
  val cwd = new File(System.getProperty("user.dir"))

  /**
    * Gets reference to metadata
    *
    * @param url Url to source
    * @return File instance
    */
  def getRef(url: URL, end: String = "json"): File = {
    new File(cache, math.abs(url.toString.hashCode) + s".$end")
  }

  /**
    * Downloads file to cache directory
    *
    * @param source    Download source url
    * @param reference Download target reference(get via [[getRef]]
    * @return A reference to downloaded file
    * @see getRef
    */
  def download(source: URL, reference: File): File = {
    logger.debug(s"Downloading $source to $reference")
    reference.createNewFile()
    FileUtils.copyURLToFile(source, reference)
    if (deleteOnExit()) reference.deleteOnExit()
    reference
  }

  /**
    * Should we delete cache files on exit?
    *
    * Use system property "xkcd.deleteonexit" to set this to true
    *
    * @see [[https://docs.oracle.com/javase/7/docs/api/java/lang/System.html#getProperty(java.lang.String) java.lang.System]]
    */
  def deleteOnExit() = {
    var r: Boolean = false
    if (System.getProperty("xkcd.deleteonexit") == "true") r = true
    r
  }

  /**
    * Cleans cwd cache
    */
  def clean(): Boolean = {
    FileUtils.deleteDirectory(cache)
    cwd.mkdirs()
  }

  def setLoggerLevel(level: Level) = {
    val l = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).asInstanceOf[Logger]
    l.setLevel(level)
  }

  private def loadCache(): Unit = {
    Cache.load()
  }

  setLoggerLevel(Level.ALL)
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
