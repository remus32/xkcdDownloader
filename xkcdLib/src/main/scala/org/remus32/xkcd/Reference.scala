package org.remus32.xkcd

import java.io.File
import java.net.URL

/**
  * Created by remus32 on 29/01/16.
  */
trait Reference {
  def name: String

  def read(): String

  def clean(): Unit

  def load(from: URL): Unit

  def exist(): Boolean

  def copyTo(to: File): Unit

  def toFile(): File

  def cache(): File = Util.cache()
}