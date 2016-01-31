package org.remus32.xkcd

import java.net.URL

import com.typesafe.scalalogging.LazyLogging
import org.apache.commons.io.IOUtils
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.remus32.xkcd.Explanation.Pattern


/**
  * Created by remus32 on 31/01/16.
  */
class Explanation(val comic: Comic) extends LazyLogging {
  /**
    * Explanation api(on [[http://explainxkcd.com]]) url
    */
  lazy val apiUrl = Util.explanationApiUrl(comic.id)
  lazy val apiRef = {
    val ref = Cache.cache.make(s"exp-$id.json")
    ref.write(load(apiUrl))
    ref
  }
  lazy val api = Util.gson.fromJson(apiRef.read(), classOf[Pattern])
  private val id = comic.id

  def load(from: URL): String = {
    val httpget = new HttpGet(from.toURI)
    httpget.addHeader("User-Agent", "Xkcd downloader")
    val response = Explanation.httpclient.execute(httpget)
    try {
      IOUtils.toString(response.getEntity.getContent)
    } finally {
      response.close()
    }
  }
}

object Explanation extends LazyLogging {
  val httpclient = HttpClients.createDefault()
  def apply(id: Comic): Explanation = new Explanation(id)

  case class Pattern()
}
