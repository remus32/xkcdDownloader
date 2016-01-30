package org.remus32.xkcd

/**
  * Created by remus32 on 22/01/16.
  */
class ComicNotFoundException(val id: Int) extends Exception {
}

object ComicNotFoundException {
  def apply(id: Int) = new ComicNotFoundException(id)
}