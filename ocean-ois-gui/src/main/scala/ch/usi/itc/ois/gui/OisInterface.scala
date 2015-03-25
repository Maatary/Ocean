package ch.usi.itc.ois.gui

import javafx.scene.control.{Button, TextArea, TreeView}

/**
 * Created by maatary on 21/08/14.
 */


trait OisInterface


case class OisMainInterface private[gui](val participantsListView: TreeView[String],
                                         val messageExchangeView: TextArea,
                                         val startButton: Button) extends OisInterface

