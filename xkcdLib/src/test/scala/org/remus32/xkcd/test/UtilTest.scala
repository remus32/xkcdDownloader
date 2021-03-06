package org.remus32.xkcd.test

import java.io.{File, FileInputStream}

import org.apache.commons.io.IOUtils
import org.remus32.xkcd.Util._
import org.remus32.xkcd.{Comic, Util}
import org.scalatest.{BeforeAndAfter, FlatSpec}

class UtilTest extends FlatSpec with BeforeAndAfter {
  before {
    init()
  }

  case class Pattern(int: Int, string: String)

  "Gson in util" should "parse json" in {
    val a = new Pattern(2, "hello")
    val b = "{int: 2, string: \"hello\"}"
    assert(a.int == gson.fromJson(b, classOf[Pattern]).int)
    assert(a.string == gson.fromJson(b, classOf[Pattern]).string)
  }

  "Gson in util" should "make json" in {
    val a = new Pattern(2, "hello")
    val b = "{int: 2, string: \"hello\"}"
    assert(
      gson.fromJson(b, classOf[Pattern]) == gson.fromJson(gson.toJson(a), classOf[Pattern])
    )
  }

  "Downloader" should "download files from url" in {
    val res = getClass.getResource("/test0")
    val out = new File(cache(), "test0")
    val compare = "hello world"
    download(res, out)
    assert(compare == IOUtils.toString(new FileInputStream(out)))
  }

  "Deleteonexit" should "be false if property is not defined" in {
    //noinspection SimplifyBoolean
    assert(deleteOnExit() == false)
  }

  it should "be true if property set to true" in {
    System.setProperty("xkcd.deleteonexit", "true")
    //noinspection SimplifyBoolean
    assert(deleteOnExit() == true)
  }

  "Init method" should "create directory for cache" in {
    Util.init()
    assert(cache().isDirectory)
  }

  it should "create directory for output" in {
    Util.init()
    assert(out().isDirectory)
  }

  it should "load default cache" in {
    Util.init()
    val int: Int = cache().listFiles.length
    assert(int > Comic.latest.id || int == Comic.latest.id)
  }

  it should "be property value if given" in {
    val before = cache().getAbsolutePath
    System.setProperty("xkcd.cacheDir", "/home")
    assert(cache().getAbsolutePath == "/home")
    System.setProperty("xkcd.cacheDir", before)
  }

  "Method output()" should "be property value if given" in {
    val before = out().getAbsolutePath
    System.setProperty("xkcd.out", "/home")
    assert(out().getAbsolutePath == "/home")
    System.setProperty("xkcd.out", before)
  }

  "Cleaned dirs" should "still exist after cleaning" in {
    Util.fullClean()
    assert(Util.cache().isDirectory)
    assert(Util.out().isDirectory)
  }
}