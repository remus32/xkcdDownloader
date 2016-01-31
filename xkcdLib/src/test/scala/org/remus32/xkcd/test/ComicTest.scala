package org.remus32.xkcd.test

import org.remus32.xkcd.{BadComicIdException, Comic, ComicNotFoundException, Util}
import org.scalatest.FlatSpec

class ComicTest extends FlatSpec {

  "Comic resolver" should "throw BadComicIdException if stupid string is given" in {
    System.setProperty("xkcd.cache", "memory")
    Util.init()
    intercept[BadComicIdException] {
      Comic.resolveComic("fafpdmpaojmdf")
    }
  }

  "Comic factory" should "throw ComicNotFoundException if non-existent comic id is given" in {
    System.setProperty("xkcd.cache", "memory")
    Util.init()
    intercept[ComicNotFoundException] {
      Comic(Comic.latest.id * 2)
    }
  }

  it should "make Comic404 if 404 id given" in {
    System.setProperty("xkcd.cache", "memory")
    Util.init()
    val comic = Comic(404)
    assert(comic.data.num == 404)
    assert(comic.image.toFile().isFile)
  }

  "Comic explanation" should "exist" in {
    System.setProperty("xkcd.cache", "memory")
    Util.init()
    val comic = Comic(723)
    assert(comic.explain.toURI.isAbsolute)
  }

}