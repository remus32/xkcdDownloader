package org.remus32.xkcd.test

import java.io
import java.io.{File, FileInputStream}

import org.apache.commons.io.IOUtils
import org.remus32.xkcd.Util._
import org.remus32.xkcd.{Comic, Util}
import org.scalatest.FlatSpec

class UtilTest extends FlatSpec {
  def init() = {
  }

  case class Pattern(int: Int, string: String)

  "Gson in util" should "parse json" in {
    init()
    val a = new Pattern(2, "hello")
    val b = "{int: 2, string: \"hello\"}"
    assert(a.int == gson.fromJson(b, classOf[Pattern]).int)
    assert(a.string == gson.fromJson(b, classOf[Pattern]).string)
  }

  "Gson in util" should "make json" in {
    init()
    val a = new Pattern(2, "hello")
    val b = "{int: 2, string: \"hello\"}"
    assert(
      gson.fromJson(b, classOf[Pattern]) == gson.fromJson(gson.toJson(a), classOf[Pattern])
    )
  }

  "Downloader" should "download files from url" in {
    init()
    val res = getClass.getResource("/test0")
    val out = new File(cache(), "test0")
    val compare = "hello world"
    download(res, out)
    assert(compare == IOUtils.toString(new FileInputStream(out)))
  }

  "Deleteonexit" should "be false if property is not defined" in {
    init()
    //noinspection SimplifyBoolean
    assert(deleteOnExit() == false)
  }

  it should "be true if property set to true" in {
    init()
    System.setProperty("xkcd.deleteonexit", "true")
    //noinspection SimplifyBoolean
    assert(deleteOnExit() == true)
  }

  "Init method" should "create directory for cache" in {
    init()
    Util.init()
    assert(cache().isDirectory)
  }

  it should "create directory for output" in {
    init()
    Util.init()
    assert(out().isDirectory)
  }

  it should "load default cache" in {
    init()
    Util.init()
    val int: Int = cache().listFiles.length
    assert(int > Comic.latest.id || int == Comic.latest.id)
  }

  "Cache method" should "return cwd+/xkcdCache if no property given" in {
    assert(cache().getAbsolutePath == new io.File(System.getProperty("user.dir") + "/xkcdCache").getAbsolutePath)
  }

  it should "be property value if given" in {
    val before = cache().getAbsolutePath
    System.setProperty("xkcd.cacheDir", "/home")
    assert(cache().getAbsolutePath == "/home")
    System.setProperty("xkcd.cacheDir", before)
  }

  "Output method" should "return cwd+/xkcd if no property given" in {
    assert(out.getAbsolutePath == new io.File(System.getProperty("user.dir") + "/xkcd").getAbsolutePath)
  }

  it should "be property value if given" in {
    val before = out().getAbsolutePath
    System.setProperty("xkcd.out", "/home")
    assert(out().getAbsolutePath == "/home")
    System.setProperty("xkcd.out", before)
  }

  "Explanation api url" should "work" in {
    assert(explanationApiUrl(452).toURI.isAbsolute)
  }
}