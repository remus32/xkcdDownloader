package org.remus32.xkcd

import java.io.File

import com.typesafe.scalalogging.LazyLogging
import org.apache.commons.io.FileUtils
import org.rauschig.jarchivelib.ArchiverFactory

/**
  * /**
  * Built-in cache controller
  */
  * object Cache {
  * lazy val res = new File(getClass.getResource("/xkcd/cache.tar.gz").toURI)
  * lazy val archiver = ArchiverFactory.createArchiver("tar", "gz")

  * def comicFile(id: Comic) = {
  * new File(res, Math.abs(id.url.toString.hashCode).toString + ".json")
  * }

  * def load(to: File = Main.cwd): Unit = {
  * archiver.extract(res, to)
  * }
  * }
  **/
/**
  *
  */
trait Cache {
  def make(name: String): Reference
}

object Cache extends LazyLogging {
  lazy val res = new File(getClass.getResource("/xkcd/cache.tar.gz").toURI)
  lazy val archiver = ArchiverFactory.createArchiver("tar", "gz")
  lazy val cache: Cache = {
    System.getProperty("xkcd.cache") match {
      case x: String if x != "" => cacheList(x)
      case "" => cacheList("File")
    }
  }
  lazy val cacheList = Map[String, Cache](
    ("file", new caches.File),
    ("memory", new caches.Memory)
  )
  val outName = "xkcdCache"

  def loadDefault(): Unit = {
    val first = new File(System.getProperty("java.io.tmpdir") + "/xkcdTmp")
    logger.debug(s"Using $first as tmp dir")
    archiver.extract(res, first)
    val tmp = new File(first, outName)
    val files = tmp.listFiles()
    files.foreach(x => {
      FileUtils.copyFileToDirectory(x, Util.cache())
    })
  }
}