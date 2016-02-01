package org.remus32.xkcd.reference

import java.io
import java.io.{FileReader, IOException}
import java.net.URL

import com.typesafe.scalalogging.LazyLogging
import org.apache.commons.io.{FileUtils, IOUtils}
import org.remus32.xkcd.{Reference, Util}

/**
  * Created by remus32 on 29/01/16.
  */
class File(val name: String) extends Reference with LazyLogging {
  val file: io.File = new io.File(cache(), name)

  def copyTo(to: io.File): Unit = if (exist()) FileUtils.copyFile(file, to)

  def clean(): Unit = if (exist()) file.delete()

  def exist(): Boolean = file.isFile

  def read(): String = {
    if (exist())
      IOUtils.toString(new FileReader(file))
    else {
      logger.error(s"Something was trying to read empty reference!", new IOException())
      ""
    }
  }

  def load(from: URL): Unit = if (!(file.isFile && file.length() > 128L)) {
    create()
    Util.download(from, file)
  }

  def create() = {
    file.createNewFile()
  }

  def toFile: io.File = file
}