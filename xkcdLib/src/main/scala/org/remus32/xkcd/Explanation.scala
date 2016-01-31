package org.remus32.xkcd

import com.typesafe.scalalogging.LazyLogging

/**
  * Created by remus32 on 31/01/16.
  */
class Explanation(val comic: Comic) extends LazyLogging {
  val apiUrl = Util.explanationApiUrl(comic.id)
}

object Explanation extends LazyLogging {
  def apply(id: Comic): Explanation = new Explanation(id)
}
