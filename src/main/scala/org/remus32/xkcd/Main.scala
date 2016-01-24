
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
  private lazy val cache = {
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
  def clean(): Unit = {
    FileUtils.deleteDirectory(cache)
    cwd.mkdirs()
  }

  println("Getting latest comic...")
  Comic.latest
  Gui(call = (call: String) => {
    var stop: Boolean = false
    val split = call.split(" ")
    split(0) match {
      case "quit" =>
        stop = true
      case "clean" =>
        println("[info][main] Cleaning...")
        clean()
      case "show" =>
        if (split.length == 1) println("Usage: show <comic id>")
        else if (split.length > 1) {
          val comic = Comic(Comic.resolveComic(split(1)))
          val img = comic.image
          Gui.showImage(img, 0, 0)
        }
      case "archive" =>
        if (split.length == 1) {
          println("Usage: archive <comic id/from> [to]")
        } else if (split.length == 2) {
          val comic = Comic(Comic.resolveComic(split(1)))
          val out = new File(cwd, "xkcd")
          out.mkdir()
          comic.copyImageTo(out)

        } else if (split.length == 3) {
          val first = Comic.resolveComic(split(1))
          val last = Comic.resolveComic(split(2))
          for (a <- first to last) {
            val comic = Comic(a)
            val out = new File(cwd, "xkcd")
            out.mkdir()
            comic.copyImageTo(out)

          }

        }
      case "info" =>
        val comic = Comic(Comic.resolveComic(split(1)))
        print(comic.info)
      case "search" =>
        if (split.length == 1) {
          println("Usage: search <regexp>")
        } else if (split.length == 2) {
          val what = split(1)
          val startTime = System.currentTimeMillis()
          var count = 0
          Comic.searchTitles(what.r).foreach(x => {
            count = count + 1
            println(x.info)
          })
          val endtime = System.currentTimeMillis()
          val time = (endtime - startTime) / 1000F
          println(s"Found $count matches in $time seconds")
        }
      case x =>
        println(s"Task $x doesn't exist")
    }
    stop
  })
}
