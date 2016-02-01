package org.remus32.xkcd

/**
  * Created by remus32 on 01/02/16.
  */
package object test {
  private val freeDir = "/tmp/xkcd"

  def init() = {
    System.setProperty("xkcd.cacheDir", s"$freeDir/cache")
    System.setProperty("xkcd.out", s"$freeDir/out")
  }
}
