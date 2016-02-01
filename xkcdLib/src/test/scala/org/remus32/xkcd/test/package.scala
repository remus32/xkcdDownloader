package org.remus32.xkcd

import java.io.File

/**
  * Created by remus32 on 01/02/16.
  */
package object test {
  private val freeDir = "/tmp/xkcd"
  private val cacheDir = s"$freeDir/cache"
  private val outDir = s"$freeDir/out"

  def init() = {
    System.setProperty("xkcd.cacheDir", cacheDir)
    new File(cacheDir).mkdirs()
    System.setProperty("xkcd.out", outDir)
    new File(outDir).mkdirs()
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "warn")
    System.setProperty("xkcd.cache", "file")
    Util.init()
  }
}
