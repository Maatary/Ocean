package ch.usi.itc.ois.gui.services

import javafx.scene.control.TreeView

import com.softwaremill.macwire.MacwireMacros._

/**
 * Created by maatary on 24/08/14.
 *
 */


trait guiServicesCreationModule {

  def getParticipantListViewService (ParticipantsListView: TreeView[String]): ParticipantListViewService = wire[ParticipantListViewServiceImpl]
}


object guiServicesCreationModule extends guiServicesCreationModule {

}