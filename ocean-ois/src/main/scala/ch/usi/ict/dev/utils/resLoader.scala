package ch.usi.ict.dev.utils

import java.io.File

/**
 * Created by maatary on 15/08/14.
 */
object resLocator {


  /**
   * Return the file representing this resource on the system
   * @param s The name of the resource to locate
   * @param clazz The Class that provide for the context to Load the resource
   * @tparam T  The type of the class. Not rely important. A scala trick
   * @throws
   * @return
   */
  @throws[IllegalArgumentException]("the file does not exist")
  def loadResource[T](s: String, clazz: Class[T]): File =  {

    val resURl = clazz.getClassLoader.getResource(s)

    new File(resURl.toURI)

  }

  /**
   *  Locate a Resource in the classPath by returning its absolute path
   * @param s The name of the resource to locate
   * @param clazz The Class that provide for the context to Load the resource
   * @tparam T  The type of the class. Not rely important. A scala trick
   * @throws
   * @return
   */
  @throws[IllegalArgumentException]("the file does not exist")
  def locateResource[T](s: String, clazz: Class[T]): String =  {

    val resURl = clazz.getClassLoader.getResource(s)

    new File(resURl.toURI).getAbsolutePath

  }

}
