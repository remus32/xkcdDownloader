package org.remus32.xkcd

import java.io.File

import org.rauschig.jarchivelib.ArchiverFactory

/**
  * Built-in cache controller
  */
object Cache {
  lazy val res = new File(getClass.getResource("/xkcd/cache.tar.gz").toURI)
  lazy val archiver = ArchiverFactory.createArchiver("tar", "gz")

  def comicFile(id: Comic) = {
    new File(res, Math.abs(id.url.toString.hashCode).toString + ".json")
  }

  def load(to: File = Main.cwd): Unit = {
    archiver.extract(res, to)
  }
}
