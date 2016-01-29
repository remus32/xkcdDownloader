package xkcd

import java.awt.EventQueue
import java.io.File

/**
  * Gui controller
  */
object Gui {
  lazy val history = new File(Main.cwd, ".xkcdhistory")
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
    * @param width  Image width
    * @param height Image height
    */
  def showImage(img: File, width: Int, height: Int) = {
    EventQueue.invokeLater(new Runnable() {
      override def run() {
        val panel = new ImageJPanel(img, width, height)
        panel.setVisible(true)
      }
    })
  }
}