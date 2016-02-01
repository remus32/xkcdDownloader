package org.remus32.xkcd.test

import org.remus32.xkcd.{Comic, Explanation}
import org.scalatest.FlatSpec

/**
  * Created by remus32 on 31/01/16.
  */
class ExplanationTest extends FlatSpec {
  "Apply method" should "create Explanations instances" in {
    val comic = Comic(1201)
    val exp = Explanation(comic)
    assert(exp.isInstanceOf[Explanation])
  }
  "Apply method and comic instance" should "have same explanations" in {
    val comic = Comic(1201)
    val exp = Explanation(comic)
    assert(exp.apiUrl == comic.explanation.apiUrl)
  }
  "Explanation" should "download api data" in {
    val exp = Comic(623).explanation
    assert(exp.parsed.outerHtml() != "")
  }
  it should "return valid comic title" in {
    val exp = Comic(623).explanation
    assert(exp.comicTitle == "Comic #623 (August 14, 2009)")
  }
}
