/**
 * Created by maatary on 21/08/14.
 */
package ch.usi.itc.ois.gui

import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.Scene
import javafx.scene.control.{Button, TextArea, TreeItem, TreeView}
import javafx.scene.layout.{BorderPane, HBox, Priority}
import javafx.stage.Stage


/**
 * Created by maatary on 20/08/14.
 *
 * The service that sets up the integrated Layout of the OIS GUI
 *
 */

object OisInterfaceBuilder {

  /**
   * Set up the Interface
   *
   */
  def BuildInterface(primaryStage: Stage): OisInterface = {

    primaryStage.setTitle("OCean Open Interaction system")

    val rootPane             = createRootPane(primaryStage)
    val participantsListView = createParticipantsListView
    val messageExchangeView  = createMessageExchangeView
    val mainPane             = createMainPane(participantsListView, messageExchangeView)
    val startButton          = createStartButton

    rootPane.setCenter(mainPane)
    rootPane.setBottom(startButton)

    OisMainInterface(participantsListView, messageExchangeView, startButton)
  }

  /**
   *  Create the main Pane of the interface. It contains the
   *  Participants List View and the MessageExchange View
   * @param participantsListView
   * @param messageExchangeView
   * @return Participants List View and the MessageExchange View in a Pane
   */
  private def createMainPane(participantsListView: TreeView[String], messageExchangeView: TextArea): HBox = {

    val mainPane = new HBox
    mainPane.getChildren.add(messageExchangeView)
    mainPane.getChildren.add(participantsListView)
    HBox.setHgrow(participantsListView, Priority.ALWAYS)
    HBox.setHgrow(messageExchangeView, Priority.ALWAYS)
    mainPane

  }

  private def createRootPane(primaryStage: Stage): BorderPane = {
    val rootPane: BorderPane = new BorderPane
    primaryStage.setScene(new Scene(rootPane))
    rootPane
  }

  private def createParticipantsListView: TreeView[String] = {
    val rootItem: TreeItem[String] = new TreeItem[String]("Agents")
    rootItem.setExpanded(true)
    rootItem.getChildren.add(new TreeItem[String]("Agent1"))//TODO To remove
    new TreeView[String](rootItem)
  }


  private def createMessageExchangeView: TextArea = {
    val textArea = new TextArea
    textArea.setEditable(false)
    textArea.setWrapText(true)
    textArea
  }

  private def createStartButton: Button = {

    val btn: Button = new Button
    btn.setText("Start")
    btn.setOnAction(new EventHandler[ActionEvent] {
      def handle(event: ActionEvent) {
        //textArea.appendText("A new click\n")
        //rootItem.getChildren.add(new TreeItem[String]("Agent" + updateCount))
      }
    })

    btn
  }


}

