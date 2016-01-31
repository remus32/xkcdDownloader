package org.remus32.xkcd.reference

import java.io
import java.io.{FileOutputStream, FileReader}
import java.net.URL

import com.typesafe.scalalogging.LazyLogging
import org.apache.commons.io.{FileUtils, IOUtils}
import org.remus32.xkcd.{Reference, Util}

/**
  * Created by remus32 on 29/01/16.
  */
class File(val name: String) extends Reference with LazyLogging {
  val file: io.File = new io.File(cache(), name)

  def copyTo(to: io.File): Unit = FileUtils.copyFile(file, to)

  def clean(): Unit = file.delete()

  def exist(): Boolean = file.isFile

  def read(): String = IOUtils.toString(new FileReader(file))

  def write(what: String): Unit = IOUtils.write(what, new FileOutputStream(file))

  def load(from: URL): Unit = if (!(file.isFile && file.length() > 128L)) Util.download(from, file)

  def toFile: io.File = file
}