/**
 * 
 */
package org.purl.linkedepcis.utils;

import java.util.prefs.Preferences;

/**
 * @author monika
 *
 */
public class EPCISPreferences {
	
	private static Preferences userPreferences = Preferences.systemRoot();
	private static int counter;
	
	public static int getNewEventCounter()
	{
		int tempCount=counter;
		userPreferences.putInt("eventCounter", tempCount);
		counter++;
		return userPreferences.getInt("eventCounter", 0);
	}
	
	public static int getEventCounter()
	{
		return userPreferences.getInt("eventCounter", 0);
	}
	public static void resetEventCounter()
	{
		counter=0;
	}

  



}
