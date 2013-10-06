/**
 * 
 */
package org.purl.linkedepcis.query;

import java.util.ArrayList;

import org.openrdf.model.URI;

/**
 * @author monika
 *
 */
public interface EPCISQueryInterface {
	
	public ArrayList<String> getEventsInRepository();
	
	public ArrayList<String> getAddressesInRepository();
	
	public ArrayList<String> getTransactionsInRepository();
	
	public ArrayList<String> getReadersInRepository();
	
	public ArrayList<ArrayList<String>> getEventDetails(String eventID);
	
	
	

}
