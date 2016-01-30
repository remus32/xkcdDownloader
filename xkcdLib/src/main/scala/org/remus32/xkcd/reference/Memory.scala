package org.remus32.xkcd.reference

import java.io
import java.net.URL

import com.typesafe.scalalogging.LazyLogging
import org.apache.commons.io.{FileUtils, IOUtils}
import org.remus32.xkcd.Reference

/**
  * Created by remus32 on 30/01/16.
  */
class Memory(val name: String) extends Reference with LazyLogging {
  var contents: String = ""
  var url:URL = null

  def copyTo(to: io.File): Unit = FileUtils.writeStringToFile(to, contents)

  def clean(): Unit = contents = ""

  def read(): String = contents

  def load(from: URL): Unit = {
    url=from
    if (!exist()) contents = IOUtils.toString(from.openStream())
  }

  def exist(): Boolean = !(contents == "")

  /**
    * This method uses files
    */
  def toFile(): io.File = {
    val r = new File(name)
    if(url!=null) r.load(url)
    r.toFile()
  }
}