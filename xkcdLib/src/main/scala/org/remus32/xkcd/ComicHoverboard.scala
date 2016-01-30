package org.remus32.xkcd

import java.time.format.DateTimeFormatter

/**
  * Special comic extension for comic 1608 Hoverboard
  */
class ComicHoverboard(id: Int = 1608) extends Comic(id: Int) {

  override lazy val image: Reference = {
    val r = cache.make("404.png")
    r.load(getClass.getResource("/xkcd/hoverboard.png"))
    r
  }
  override lazy val info: String = {
    var r = ""
    val title = data.title
    r = r + s"XKCD $id: $title\n"
    val format = DateTimeFormatter.ISO_LOCAL_DATE
    val formattedDate = format.format(date)
    r = r + s"  from $formattedDate\n"
    r = r + s"  play on $url\n"
    r
  }
}
