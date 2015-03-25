package ch.usi.itc.monitoringtheois.viewingparticipantslist

import javafx.scene.control.TreeView

import ch.usi.ict.dev.ch.usi.itc.ois.common.model.Participant
import ch.usi.itc.ois.gui.services.guiServicesCreationModule
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

/**
 * Created by maatary on 17/08/14.
 *
 * The Specification of the feature of system that allows to view the set of participants as List
 * to support the capability to observe the OIS.
 *
 */
class ViewingParticipantsListFeatureSpec extends FeatureSpec with Matchers with GivenWhenThen {



  feature("The viewing of the set Participants as a List") {

    info("As the OIS observer I want to be able to view the list of Participants in order to Observe the OIS")




    scenario("Adding a new participants to an empty ParticipantListViewService") {


      Given("a ParticipantsListViewService that is empty")

        val participantListView = new TreeView[String]()
        val participantsListViewservice = guiServicesCreationModule.getParticipantListViewService(participantListView)


      When("we add one participants to it")

        val participant = Participant("id", null)
        participantsListViewservice.addParticipant(participant)

      Then("its ParticipantsListView component should contain the new participants as first element under the root ")

        val child = participantListView.getRoot.getChildren.get(0)
        child should not be (null)
        child.getValue should be ("id")

    }




    scenario("Removing one of the Participant of a ParticipantsListViewService that contains 2 participants") {


      Given("a ParticipantsListViewService with 2 participants")

        val participant1 = Participant("id", null)
        val participant2 = Participant("id2", null)
        val participantListView = new TreeView[String]()
        val participantsListViewservice = guiServicesCreationModule.getParticipantListViewService(participantListView)


        participantsListViewservice.addParticipant(participant1)
        participantsListViewservice.addParticipant(participant2)

      When("we request to remove one of the participants")

        participantsListViewservice.removeParticipant(participant1)

      Then("the ParticipantsListViewService shall be left with only the participant that wasn't requested to be removed")

        participantListView.getRoot.getChildren.size() should be (1)
        participantListView.getRoot.getChildren.get(0).getValue should be (participant2.id)


    }

  }





}
