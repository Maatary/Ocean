package ch.usi.itc.ois.app

import javafx.application.Application
import javafx.stage.Stage

import ch.usi.itc.ois.gui.{OisMainInterface, OisInterfaceBuilder}

/**
 * Created by maatary on 20/08/14.
 */

object OISAPP {


  def main(args: Array[String]): Unit = {

    javafx.application.Application.launch(classOf[OISAPP], args: _*)

  }

}



class OISAPP extends Application {


  override def start(primaryStage: Stage): Unit = {



    val oisInterface = OisInterfaceBuilder.BuildInterface(primaryStage)

     oisInterface match {

       case OisMainInterface(x, y, z) => {}
    }

    //TODO InitViewServices


    primaryStage.show
  }


}



