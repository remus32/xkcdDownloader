package org.remus32.xkcd

import java.io.File
import javax.imageio.ImageIO
import javax.swing.{ImageIcon, JFrame, JLabel, JPanel}

/**
  * Created by remus32 on 22/01/16.
  */
class ImageJPanel(img: File, width: Int, height: Int) extends JFrame {
  add(new JPanel() {
    val myPicture = ImageIO.read(img)
    val picLabel = new JLabel(new ImageIcon(myPicture))
    add(picLabel)
  })
}
