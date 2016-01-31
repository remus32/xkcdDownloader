package org.remus32.xkcd

import java.io.File
import javax.imageio.ImageIO
import javax.swing.ImageIcon

import com.typesafe.scalalogging.LazyLogging
import jline.console.ConsoleReader
import jline.console.history.FileHistory

import scala.swing.event.{Key, KeyPressed}

/**
  * Gui controller
  */
object Gui extends LazyLogging {
  lazy val history = new File(Util.cache(), ".xkcdhistory")
  lazy val reader = new ConsoleReader()

  /**
    * A callback function to read and parse lines
    *
    * @param call Callback; return true to stop app
    */
  def apply(call: String => Unit): Unit = {
    reader.setPrompt("> ")
    if (!history.isFile) history.createNewFile()
    reader.setHistory(new FileHistory(history))
    reader.setHistoryEnabled(true)
    while (true) {
      call(reader.readLine())
    }
  }

  /**
    * Show image in graphic environment
    *
    * @todo
      * @param img A file reference to comic
    */
  def showImage(img: Comic): Unit = {
    import scala.swing._

    class UI(var actualComic: Comic) extends MainFrame {
      val label = new Label

      def exit() = visible = false

      def show(comic: Comic): Unit = {
        actualComic = comic
        val image = actualComic.image.toFile
        val id = actualComic.id
        val comicTitle = actualComic.data.title
        val bImg = ImageIO.read(image)
        val width = bImg.getWidth()
        val height = bImg.getHeight() + (bImg.getHeight() / 20)

        title = s"XKCD $id: $comicTitle"
        preferredSize = new Dimension(width, height)
        label.icon = new ImageIcon(bImg)
        label.horizontalAlignment = Alignment.Center
        label.verticalAlignment = Alignment.Center
        contents = new BoxPanel(Orientation.Vertical) {
          contents += label
          listenTo(keys)
          reactions += {
            case KeyPressed(_, Key.Right, _, _) =>
              logger.debug("Changing by right")
              move(1)
            case KeyPressed(_, Key.Left, _, _) =>
              logger.debug("Changing by left")
              move(-1)
            case KeyPressed(_, Key.Escape, _, _) =>
              logger.debug("Exiting swing window")
              visible = false
              exit()
          }
          focusable = true
          requestFocus
        }
        visible = true
        contents.foreach(_.requestFocus())
      }

      def move(relative: Int) = {
        try {
          show(Comic(actualComic.id + relative))
        } catch {
          case e: ComicNotFoundException =>
            logger.error("Comic does not exist: in:", e)
        }
      }

      show(actualComic)
    }
    try {
      val ui = new UI(img)
    } catch {
      case e: Exception => logger.error("Gui(Swing) error in: ", e)
    }
  }
}