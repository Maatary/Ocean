package ch.usi.itc.ois.gui.services

import javafx.scene.control.{TreeItem, TreeView}

import ch.usi.ict.dev.ch.usi.itc.ois.common.model.Participant

import scala.collection.JavaConverters._

/**
 * Created by maatary on 24/08/14.
 */

trait ParticipantListViewService {
  def addParticipant(participant: Participant): Unit
  def removeParticipant(participant: Participant): Unit
}

class ParticipantListViewServiceImpl(private val ParticipantsListView: TreeView[String]) extends ParticipantListViewService {

  val root = {ParticipantsListView.setRoot(new TreeItem[String]("Agents")); ParticipantsListView.getRoot}

  val children = root.getChildren.asScala

  override def addParticipant(participant: Participant): Unit = {
    children += new TreeItem[String](participant.id)
  }

  override def removeParticipant(participant: Participant): Unit = {

     children.find(e => e.getValue == participant.id) match {
       case Some(item) => children -= item
       case _ => ()
    }

  }


}