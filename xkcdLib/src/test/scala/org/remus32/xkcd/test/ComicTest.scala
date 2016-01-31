package org.remus32.xkcd.test

import java.io.FileInputStream
import java.net.URL

import org.apache.commons.io.IOUtils
import org.remus32.xkcd._
import org.remus32.xkcd.caches.File
import org.scalatest.FlatSpec

class ComicTest extends FlatSpec {

  def init() = {
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "warn")
    System.setProperty("xkcd.cache", "file")
    Util.init()
  }

  "Cache controller" should "select File wrapper if no property given" in {
    assert(Cache.cache.isInstanceOf[File])
  }

  "Cache controller" should "select File wrapper if empty property given" in {
    System.setProperty("xkcd.cache", "")
    assert(Cache.cache.isInstanceOf[File])
  }

  "Comic resolver" should "throw BadComicIdException if stupid string is given" in {
    init()
    intercept[BadComicIdException] {
      Comic.resolveComic("fafpdmpaojmdf")
    }
  }

  "Comic factory" should "throw ComicNotFoundException if non-existent comic id is given" in {
    init()
    intercept[ComicNotFoundException] {
      Comic(Comic.latest.id * 2)
    }
  }

  it should "make Comic404 if 404 id given" in {
    init()
    val comic = Comic(404)
    assert(comic.data.num == 404)
    assert(comic.image.toFile.isFile)
  }

  "Comic explanation" should "exist" in {
    init()
    val comic = Comic(723)
    assert(comic.explainUrl.toURI.isAbsolute)
  }

  "Reference" should "work" in {
    init()
    val ref = Cache.cache.make("test0")
    ref.load(new URL(Util.xkcdBase + "732/info.0.json"))
    assert(ref.read == Comic(732).metaRef.read())
    assert(ref.toFile.toString != Comic(732).metaRef.toFile.toString)
    assert(IOUtils.toString(new FileInputStream(ref.toFile)) == IOUtils.toString(new FileInputStream(Comic(732).metaRef.toFile)))
  }

  "List of all comics" should "contain all comics" in {
    init()
    assert(Comic.comicList.length == Comic.latest.id)
  }

  "Comic name resolver" should "resolve Ints into their Int representation" in {
    assert(Comic.resolveComic("255") == 255)
  }

  it should "resolve last to latest comic id" in {
    assert(Comic.resolveComic("last") == Comic.latest.id)
  }

  it should "resolve random into valid random comic ids" in {
    for (x <- 1 to 1000) {
      assert(0 < Comic.resolveComic("random"))
      assert(Comic.resolveComic("random") < (Comic.latest.id + 1))
    }
  }

  "Random comic number generator" should "return existing comic ids" in {
    for (x <- 1 to 1000) {
      val d = Comic.randomComicId()
      assert(0 < d)
      assert(d < (Comic.latest.id + 1))
    }
  }
}