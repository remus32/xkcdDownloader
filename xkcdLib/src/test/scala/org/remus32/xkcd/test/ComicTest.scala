package org.remus32.xkcd.test

import org.remus32.xkcd.{BadComicIdException, Comic, ComicNotFoundException}
import org.scalatest.FlatSpec

class ComicTest extends FlatSpec {

  "Comic resolver" should "throw BadComicIdException if stupid string is given" in {
    intercept[BadComicIdException] {
      Comic.resolveComic("fafpdmpaojmdf")
    }
  }

  "Comic factory" should "throw ComicNotFoundException if non-existent comic id is given" in {
    intercept[ComicNotFoundException] {
      Comic(Comic.latest.id * 2)
    }
  }

  it should "make Comic404 if 404 id given" in {
    val comic = Comic(404)
    assert(comic.data.num == 404)
    assert(comic.image.toFile().isFile)
  }
}