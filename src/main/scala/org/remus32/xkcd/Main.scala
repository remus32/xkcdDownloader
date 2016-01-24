
package org.remus32.xkcd

import java.io.File
import java.net.URL

import com.google.gson.Gson
import org.apache.commons.io.FileUtils

/**
  * Main application object
  *
  * @since 1.0
  */
object Main extends App {
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
      r.mkdir()
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
    println(s"Downloading $source to $reference")
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

  println("Getting latest comic...")
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
    val r = {
      var l = ""
      success match {
        case true => l = s"Successfully done '$call'"
        case false => l = s"Done '$call' with errors"
      }
      l
    }
    if (matched) {
      println(s"$r in $time s.")
    } else {
      println(s"Task $first does not exist")
    }
  })
}
