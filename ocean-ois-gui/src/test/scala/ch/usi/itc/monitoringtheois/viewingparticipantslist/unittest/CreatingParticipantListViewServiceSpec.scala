package ch.usi.itc.monitoringtheois.viewingparticipantslist.unittest

import javafx.scene.control.TreeView

import org.scalatest.{FunSpec, GivenWhenThen, Matchers}

/**
 * Created by maatary on 24/08/14.
 */
class CreatingParticipantListViewServiceSpec extends FunSpec with Matchers with GivenWhenThen {

  describe("Creating a Participants List View Service") {

    it("should create a ParticipantListViewService containing a View Component with an \"Agents\" roots node ") {

      Given("the ParticipantsListView CreationModule and a ParticipantsListView component")

        val participantListView = new TreeView[String]
        import ch.usi.itc.ois.gui.services.guiServicesCreationModule

      When("a ParticipantsListViewService is requested from the Module with the ParticipantsListView Component as parameter")

        val service = guiServicesCreationModule.getParticipantListViewService(participantListView)


      Then("the ParticipantsListViewService returned should not be null")

        service should not be(null)

      And("ParticipantsListView component should be initialized with a root")

        participantListView.getRoot should not be(null)

      And("The root should be Agents")

        participantListView.getRoot.getValue should be("Agents")


    }




  }



}
