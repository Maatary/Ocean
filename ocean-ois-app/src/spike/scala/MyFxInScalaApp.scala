package ch.usi.itc.ois.app

import javafx.event.{EventHandler, ActionEvent}
import javafx.scene.Scene
import javafx.scene.control.{Button, SplitPaneBuilder, TextArea, TreeView}
import javafx.scene.layout.BorderPaneBuilder
import javafx.stage.Stage

class MyFxInScalaApp extends javafx.application.Application {

  override def start(stage: Stage) : Unit = {

    stage.setTitle("Hello World!")

    val btn = new Button();
    btn.setText("Say 'Hello World'");

    btn.setOnAction(new EventHandler[ActionEvent]() {

      override def handle(event: ActionEvent ) {
        System.out.println("Hello World!");
      }
    });


    //root.getChildren().add(btn);

    val splitpaneBuilder = SplitPaneBuilder.create()
    splitpaneBuilder.items(new TextArea, new TreeView)
    val splitPane = splitpaneBuilder.build()
    splitPane.setDividerPositions(0.65)

    //splitPane.setMinSize(1000, 600)

    val LayoutPane = BorderPaneBuilder.create().build();
    LayoutPane.setCenter(splitPane)
    //LayoutPane.setMinSize(1000, 600)
    stage.setScene(new Scene(LayoutPane));
    stage.setMinWidth(1000)
    stage.setMinHeight(600)
    stage.show();

  }

}

object MyFxInScalaApp  {

  def main(args: Array[String]): Unit = {

    javafx.application.Application.launch(classOf[MyFxInScalaApp], args:_*)

  }
}