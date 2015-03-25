package ch.usi.ict.dev.ois.interactionKB.timeUtils

import java.util.Date
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.text.SimpleDateFormat

object JodaJavaConverter {
	
	/**
	 * Convert a java.util.Date in a joda.DateTime Object in the simpleformat supplied
	 */
	def asJodaDateTime(date: Date, simpleDateFormat:SimpleDateFormat): DateTime = {
		
		new DateTime(simpleDateFormat.format(date))
		
	}

}