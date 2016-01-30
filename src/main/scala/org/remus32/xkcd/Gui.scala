package org.remus32.xkcd

import java.io.File
import javax.imageio.ImageIO
import javax.swing.ImageIcon

import com.typesafe.scalalogging.LazyLogging
import jline.console.ConsoleReader
import jline.console.history.FileHistory

/**
  * Gui controller
  */
object Gui extends LazyLogging{
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
      * @param img  A file reference to comic
    */
  def showImage(img: Comic): Unit = {
    val image = img.image.toFile()
    val id = img.id
    val comicTitle = img.data.title
    val bImg = ImageIO.read(image)
    val width = bImg.getWidth()+((bImg.getWidth()/100)*5)
    val height = bImg.getHeight()+((bImg.getHeight()/100)*5)
    import scala.swing._

    class UI extends MainFrame {
      title = s"XKCD $id: $comicTitle"
      preferredSize = new Dimension(width, height)
      contents = new Label("",new ImageIcon(bImg),Alignment.Center)
    }
    try {
      val ui = new UI
      ui.visible = true
    }catch{
      case e:Exception => logger.error("Gui(Swing) error in: ", e)
    }
  }
}