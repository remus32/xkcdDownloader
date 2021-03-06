package org.remus32.xkcd

import java.io
import java.io.File
import java.net.URL

import com.google.gson.Gson
import com.typesafe.scalalogging.LazyLogging
import org.apache.commons.io.FileUtils

/**
  * Created by remus32 on 30/01/16.
  */
object Util extends LazyLogging {
  lazy val gson = new Gson()
  /**
    * Xkcd address
    */
  val xkcdBase = "http://xkcd.com/"

  /**
    * Downloads file to cache directory
    *
    * @param source    Download source url
    * @param reference Download target reference
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
  def deleteOnExit(): Boolean = {
    if (System.getProperty("xkcd.deleteonexit") == "true") return true
    false
  }

  /**
    * Init system
    *
    * - makes cache directory
    */
  def init() = {
    cache().mkdirs
    out.mkdirs
    val cacheS = Cache.cache.getClass.getCanonicalName
    logger.info(s"Using $cacheS as cache wrapper")
    Cache.loadDefault()
  }

  def fullClean() = {
    cleanCache()
    cleanOut()
  }

  def cleanCache() = clean(cache())

  /**
    * Get cache directory
    *
    * Uses system prop. 'xkcd.cacheDir'
    *
    * @return
    */
  def cache(): io.File = {
    val prop = System.getProperty("xkcd.cacheDir")
    prop match {
      case null =>
        new io.File(System.getProperty("user.dir") + "/xkcdCache")
      case e: String if new File(e).isDirectory =>
        new io.File(e)
      case e =>
        new io.File(System.getProperty("user.dir") + "/xkcdCache")
    }
  }

  def cleanOut() = clean(out())

  private def clean(what: File) = {
    FileUtils.deleteDirectory(what)
    what.mkdirs()
  }

  /**
    * Get archive(output) directory
    *
    * Uses system prop.'xkcd.out'
    *
    * @return
    */
  def out(): io.File = {
    val prop = System.getProperty("xkcd.out")
    prop match {
      case null =>
        new io.File(System.getProperty("user.dir") + "/xkcd")
      case e if new File(e).isDirectory =>
        new File(e)
      case e =>
        new io.File(System.getProperty("user.dir") + "/xkcd")
    }
  }
}
