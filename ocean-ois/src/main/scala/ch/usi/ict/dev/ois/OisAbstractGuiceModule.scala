package ch.usi.ict.dev.ois

import com.google.inject.AbstractModule
import ch.usi.ict.dev.ois.interactionKB.OISconfigProfile
import com.google.inject.name.Names

abstract class OisAbstractGuiceModule(val configData: OISconfigProfile) extends AbstractModule {
	
	def configure(configData: OISconfigProfile): Unit
	
	def configure() : Unit = {
		
		bind(classOf[OISconfigProfile]).toInstance(configData)
		
		
		//bindConstant().annotatedWith(Names.named(PropertiesNames.kbDir)).to(configData.workingDirName)
		
		configure(configData)
		
	}
	
}