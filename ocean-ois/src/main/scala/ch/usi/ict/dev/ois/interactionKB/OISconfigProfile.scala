package ch.usi.ict.dev.ois.interactionKB

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.NoSuchFileException
import java.net.URL


trait OISconfigProfile {
	
	def domainOntoIRIName: String
	
	def domainOntoName: String
	
//	def workingDirName: String
	
}


@throws [java.nio.file.FileSystemException]
case class OISconfigProfileImpl(private val _OntoUrlName: String/*, private val _WorkdingDirName: String*/) extends OISconfigProfile {
	

	
	def domainOntoIRIName = { (new URL(_OntoUrlName)).toString() }
	
	
	def domainOntoName = {
		
		val splits = domainOntoIRIName.split("/")
		splits.last
	}
	
//	def workingDirName = {
//
//		if (Files.notExists(Paths.get(_WorkdingDirName)))
//			throw new NoSuchFileException(_WorkdingDirName)
//		else
//			_WorkdingDirName
//
//	}
}

//object OISconfigProfileBasicImpl {
//	
//	def apply(aOntoUrlName:String, aOntoLocalFileName: String, aWorkdingDirName: String) = new OISconfigProfileBasicImpl(aOntoUrlName, aOntoLocalFileName, aWorkdingDirName)
//	
//}