package org.remus32.xkcd

import java.io.{File, FileReader}
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import org.apache.commons.io.FileUtils
import org.remus32.xkcd.Comic.Pattern

import scala.collection.mutable
import scala.util.matching.Regex

/**
  * Generic comic
  * Constructors should be never used! See Comic.apply(id:Int)
  */
class Comic(val id: Int) {

  /**
    * Parsed comic metadata
    */
  lazy val data: Pattern = Comic.parse(metaRef)
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
    r = r + s"  $url"
    r
  }
  /**
    * File with comic image
    */
  lazy val image: File = {
    val url = new URL(data.img)
    val split = url.toString.split("\\.")
    val r = Main.getRef(url, split.last)
    if (!r.isFile) Main.download(url, r)
    r
  }
  /**
    * A reference to comic metadata
    */
  private lazy val metaRef: File = {
    //noinspection FieldFromDelayedInit
    val url = new URL(Main.xkcdBase + s"$id/info.0.json")
    val ref = Main.getRef(url)
    val cache = new File(getClass.getResource("/xkcd/cache/" + Math.abs(url.toString.hashCode).toString + ".json").toURI)
    if (!(ref.isFile && ref.length() > 128L) && !cache.isFile) Main.download(url, ref)
    else if (cache.isFile) {
      println(s"Using cache from $cache...")
      FileUtils.copyFile(cache, ref)
    }
    ref
  }
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
    println(s"Copying from $image to $target")
    FileUtils.copyFile(image, target)
  }

  println(s"[INFO] Created comic: id: $id")
  println(s"[INFO][Comic#$id] Downloading metadata...")
  data
}

object Comic {
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
    println("Getting latest comic")
    val ref = Main.getRef(latestUrl)
    var file = ref
    if (((System.currentTimeMillis() / 1000L) - ref.lastModified) > 60 * 2 || !ref.isFile) {
      file = Main.download(latestUrl, ref)
    }
    Comic(parse(file).num, force = true)
  }
  /**
    * Url to latest comic api
    */
  val latestUrl = new URL(Main.xkcdBase + "info.0.json")

  /**
    * Parse comic metadata to Pattern instance
    *
    * Uses gson
    *
    * @param json Json string representation
    * @return Pattern instance
    */
  def parse(json: String): Pattern = Main.gson.fromJson(json, classOf[Pattern])

  /**
    * Parse comic metadata to Pattern instance
    *
    * Uses gson
    *
    * @param json Json file representation
    * @return Pattern instance
    */
  def parse(json: File): Pattern = parse(new FileReader(json))

  /**
    * Parse comic metadata to Pattern instance
    *
    * Uses gson
    *
    * @param json Json file reader representation
    * @return Pattern instance
    */
  def parse(json: FileReader): Pattern = Main.gson.fromJson(json, classOf[Pattern])

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
    if (in.matches("\\d+")) return in.toInt
    if (in == "last") return latest.id
    throw new BadComicIdException(s"$in")
    0
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
    try {
      if (force) {
        return new Comic(id)
      } else {
        id match {
          case x if x > latest.id => throw ComicNotFoundException(id)
          case 404 => return new Comic404()
          case 1608 => return new ComicHoverboard()
          case x => return new Comic(id)
        }
      }
    } catch {
      case e: ComicNotFoundException =>
        val id = e.id
        println(s"[ERROR][Main] Comic $id not found")
      case e: BadComicIdException =>
        val id = e.id
        println(s"[ERROR][Main] $id is not a valid comic identifier")
      case e: ComicNotFoundException =>
        println(s"[ERROR][Main] HTTP 404: Comic not found")
    }
    new Comic(id)
  }

  /**
    * Search for matches in comic titles
    *
    * @param regex Compiled regular expression pattern
    * @return List of comics
    */
  def searchTitles(regex: Regex): List[Comic] = {
    val r = mutable.MutableList[Comic]()
    println("Loading metadata...")
    comicList
    val count = comicList.length
    println(s"Searching in $count comics...")
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