package org.remus32.xkcd

import java.net.URL

import com.typesafe.scalalogging.LazyLogging

/**
  * Created by remus32 on 31/01/16.
  */
class Explanation(url: URL) extends LazyLogging {

}

object Explanation extends LazyLogging {
  def apply(id: Comic): Explanation = apply(id.id)

  private def apply(id: Int): Explanation = new Explanation(Util.explanationApiUrl(id))
}
