package org.remus32.xkcd

import java.time.LocalDate

import org.remus32.xkcd.Comic.Pattern

/**
  * Special comic extension for comic 404 404
  */
class Comic404(id: Int = 404) extends Comic(id: Int) {

  override lazy val data: Pattern = new Pattern(4, 404, 2008, "404: Not found", 1, "", "")
  override lazy val date: LocalDate = LocalDate.of(2008, 4, 1)
  override lazy val info: String = s"Comic $id not found!\n"
  override lazy val image: Reference = {
    val r = cache.make("404.png")
    r.load(getClass.getResource("/xkcd/404.png"))
    r
  }
}
