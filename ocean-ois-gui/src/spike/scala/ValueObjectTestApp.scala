/**
 * Created by maatary on 23/08/14.
 *
 */




class guiInterface(val listView: String)

object guiInterface {

  def apply(listView: String) : guiInterface = {

    new guiInterface(listView)
  }

}

object ValueObjectTestApp extends App {


  println(guiInterface("myView").listView)

}
