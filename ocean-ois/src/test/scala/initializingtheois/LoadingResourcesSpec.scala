package initializingtheois

import java.io.File

import ch.usi.ict.dev.utils.resLocator
import org.scalatest.{Matchers, GivenWhenThen, FunSpec}

/**
 * Created by maatary on 15/08/14.
 */
class LoadingResourcesSpec extends FunSpec with Matchers with GivenWhenThen {


  describe("Loading resources files Specification") {

    it ("should return a File properly initiated when it is requested out of a resource") {

      Given("a resource name representing a file on the classPath and a the Class object of the context in which the resource is asked")

        val resName = "dataset"
        val aclass  = classOf[LoadingResourcesSpec]

      When("a request to load the resource is performed")

        val file = resLocator.loadResource(resName, aclass)


      Then(" the return file should not be null and shall have the name of the resource as its name")

        file.getName should be (resName)

        info(file.toString)
        info(file.getAbsolutePath)
        info(file.getCanonicalPath)

    }


    it ("should return a String representing the location of a resource when a name is requested out of a resource") {

      Given("")

      When("")

      Then("")

    }


  }

}
