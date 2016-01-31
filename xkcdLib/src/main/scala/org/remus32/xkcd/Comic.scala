package org.remus32.xkcd

import java.io.File
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.typesafe.scalalogging.{LazyLogging, StrictLogging}
import org.remus32.xkcd.Comic.Pattern

import scala.collection.mutable
import scala.util.Random
import scala.util.matching.Regex

/**
  * Generic comic
  * Constructors should be never used! See Comic.apply(id:Int)
  */
class Comic(val id: Int) extends StrictLogging {
  /**
    * Comic explanation on [[http://www.explainxkcd.com]]
    */
  lazy val explain = new URL(s"http://www.explainxkcd.com/wiki/index.php/$id")
  /**
    * Parsed comic metadata
    */
  lazy val data: Pattern = Comic.parse(metaRef.read())
  /**
    * Date of comic publication
    */
  lazy val date: LocalDate = LocalDate.of(data.year, data.month, data.day)
  /**
    * A human readable information about the comic
    */
  lazy val info = {
    var r = ""
    val title = data.title
    r = r + s"XKCD $id: $title\n"
    val format = DateTimeFormatter.ISO_LOCAL_DATE
    val formattedDate = format.format(date)
    r = r + s"  from $formattedDate\n"
    r = r + s"  $url\n"
    r = r + s"  Explanation on $explain\n"
    r
  }
  /**
    * File with comic image
    */
  lazy val image: Reference = {
    val url = new URL(data.img)
    val split = url.toString.split("\\.").last
    val r = cache.make(s"$id.$split")
    r.load(url)
    r
  }
  /**
    * A reference to comic metadata
    */
  lazy val metaRef: Reference = {
    val url = new URL(Util.xkcdBase + s"$id/info.0.json")
    val ref = cache.make(s"$id.json")
    ref.load(url)
    ref
  }
  protected lazy val cache = Cache.cache
  /**
    * URL to comic on [[http://xkcd.com/]]
    */
  val url = new URL(s"http://xkcd.com/$id")

  /**
    * Copy comic image
    *
    * @param to Where to copy
    */
  def copyImageTo(to: File): Unit = {
    val end = image.toString.split("\\.").last
    val target = new File(to, s"$id.$end")
    logger.debug(s"Copying from $image to $target")
    image.copyTo(to)
  }


  logger.debug(s"[INFO] Created comic: id: $id")
  logger.debug(s"[INFO][Comic#$id] Downloading metadata...")
  data

  override def toString: String = id.toString
}

object Comic extends LazyLogging {
  /**
    * List of all comics
    */
  lazy val comicList: List[Comic] = {
    var r = mutable.MutableList[Comic]()
    val first = 1
    val last = latest.id
    for (a <- first to last) {
      r.+=(Comic(a))
    }
    r.toList
  }
  /**
    * A reference to latest comic
    */
  lazy val latest: Comic = {
    val ref = Cache.cache.make("latest.json")
    ref.load(latestUrl)
    val r = Comic(parse(ref.read).num, force = true)
    ref.clean()
    r
  }
  /**
    * Url to latest comic api
    */
  val latestUrl = new URL(Util.xkcdBase + "info.0.json")

  /**
    * Parse comic metadata to Pattern instance
    *
    * Uses gson
    *
    * @param json Json string representation
    * @return Pattern instance
    */
  def parse(json: String): Pattern = Util.gson.fromJson(json, classOf[Pattern])

  /**
    * Resolve comic id from its string representation
    *
    * Currently supports
    * - comic id(1,5,251)
    * - last comic(last)
    *
    * @param in Comic id string representation
    * @return Comic id
    */
  def resolveComic(in: String): Int = {
    in match {
      case x if x.matches("\\d+") =>
        in.toInt
      case "last" =>
        latest.id
      case "random" =>
        randomComicId()
      case x =>
        throw new BadComicIdException(s"$in")
        0
    }
  }

  def randomComicId(): Int = {
    val rand = new Random()
    rand.nextInt(latest.id + 1)
  }

  /**
    * Loads metadata of all comics
    */
  def loadAllMetadata() = {
    val first = 1
    val last = latest.id
    for (a <- first to last) {
      Comic(a)
    }
  }

  /**
    * Creates new comic
    *
    * @param id    Comic id. See [[Comic.resolveComic()]]
    * @param force Forces creation(don't check if exists)
    * @return Comic instance
    */
  def apply(id: Int, force: Boolean = false): Comic = {
      if (force) {
        new Comic(id)
      } else {
        id match {
          case x if x > latest.id => throw ComicNotFoundException(id)
          case 404 => new Comic404()
          case 1608 => new ComicHoverboard()
          case x => new Comic(id)
        }
      }
  }

  /**
    * Search for matches in comic titles
    *
    * @param regex Compiled regular expression pattern
    * @return List of comics
    */
  def searchTitles(regex: Regex): List[Comic] = {
    val r = mutable.MutableList[Comic]()
    logger.info("Loading metadata...")
    comicList
    val count = comicList.length
    logger.debug(s"Searching in $count comics...")
    comicList.foreach(x => {
      val m = regex.findAllMatchIn(x.data.title)
      m.foreach(x2 => {
        r.+=(x)
      })
    })
    r.toList
  }

  /**
    * Json encoding pattern
    */
  case class Pattern(month: Int, num: Int, year: Int, title: String, day: Int, img: String, alt: String)
}