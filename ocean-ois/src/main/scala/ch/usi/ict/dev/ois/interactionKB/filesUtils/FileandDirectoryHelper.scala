package ch.usi.ict.dev.ois.interactionKB.filesUtils

import java.io.File

object FileandDirectoryHelper {

	/**
	 * A method that set up a directory for use
	 * 
	 * It checks if the directory already exist first, if so it clean the directory otherwise it creates it
	 * 
	 */
	def setUpDir(path: String): Unit = {
		
		val dir = new File(path)

		if (dir.exists()) {
			dir.listFiles().foreach(f => f.delete())
      println("dir: "+ dir.getName + " cleaned up")
			//dir.delete()
		}
	}

}