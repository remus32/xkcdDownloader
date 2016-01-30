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
  def deleteOnExit() = {
    var r: Boolean = false
    if (System.getProperty("xkcd.deleteonexit") == "true") r = true
    r
  }

  /**
    * Init system
    *
    * - makes cache directory
    */
  def init() = {
    cache.mkdirs
    out.mkdirs
    Cache.loadDefault()
  }

  /**
    * Get cache directory
    *
    * Uses system prop. 'xkcd.cache'
    *
    * @return
    */
  def cache(): io.File = {
    val prop = System.getProperty("xkcd.cache")
    prop match {
      case null =>
        new io.File(System.getProperty("user.dir") + "/xkcdCache")
      case e: String =>
        try {
          return new io.File(e)
        } catch {
          case e: io.IOException =>
            logger.error("Got IOException", e)
        }
    }
    new io.File(System.getProperty("user.dir") + "/xkcdCache")
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
      case e: String =>
        try {
          return new io.File(e)
        } catch {
          case e: io.IOException =>
            logger.error("Got IOException", e)
        }
    }
    new io.File(System.getProperty("user.dir") + "/xkcd")
  }
}