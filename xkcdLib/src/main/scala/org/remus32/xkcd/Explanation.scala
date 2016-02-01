package org.remus32.xkcd

import com.typesafe.scalalogging.LazyLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import org.remus32.xkcd.Explanation.Pattern


/**
  * Created by remus32 on 31/01/16.
  */
class Explanation(val comic: Comic) extends LazyLogging {
  /**
    * Explanation webpage(on [[http://explainxkcd.com]]) url
    */
  lazy val apiUrl = comic.explainUrl
  lazy val apiRef = {
    Cache.cache.make(s"exp-$id.html")

  }
  lazy val parsed: Document = {
    if (apiRef.exist()) {
      Jsoup.parse(apiRef.read())
    } else {
      val r = Jsoup.connect(apiUrl.toString)
        .userAgent("Xkcd downloader")
        .get()
      apiRef.write(r.outerHtml())
      r
    }
  }
  lazy val api: Pattern = {
    new Pattern(
      parsed,
      Parser.unescapeEntities(parsed.select("a.text.external > span").html(), false)
    )
  }
  lazy val comicTitle = api.comicTitle
  private val id = comic.id

  def info(): String = {
    api
    var r = ""
    r += "Ahoj\n"
    r
  }
}

object Explanation extends LazyLogging {
  def apply(id: Comic): Explanation = new Explanation(id)

  case class Pattern(document: Document, comicTitle: String)
}
