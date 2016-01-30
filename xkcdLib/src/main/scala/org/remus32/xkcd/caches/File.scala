package org.remus32.xkcd.caches

import org.remus32.xkcd.{Cache, Reference, reference}

/**
  * Created by remus32 on 30/01/16.
  */
class File extends Cache {
  def make(name: String): Reference = new reference.File(name)
}