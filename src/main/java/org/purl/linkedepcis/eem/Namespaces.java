package org.purl.linkedepcis.eem;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.purl.linkedepcis.utils.EPCISPreferences;


public class Namespaces {

	// base IRI for events 
	private String eventIRI;
        Logger logger = Logger.getLogger(Namespaces.class);
	
	//a Map returning all the prefixes and IRIs
	private Map<String, String> namespacePrefixes = new HashMap<String, String>();
	private String baseIRI="";
	public Namespaces(String baseURI)
	{
		this();
		this.baseIRI=baseURI;
		
	
	}
	public Namespaces()
	{
		
		setIRIandPrefix("http://purl.org/FIspace/eem#", "eem");
		setIRIandPrefix("http://purl.org/co#", "co");
		setIRIandPrefix("http://purl.org/FIspace/cbv#", "cbv");
		setIRIandPrefix("http://www.w3.org/2006/vcard/ns#", "vcard");
	}
	
	
	//return the eventIRI by appending the 
	public String getNewEventIRI(String prefix) {
		EPCISCommon epcs = new EPCISCommon();
		eventIRI =namespacePrefixes.get(prefix).concat(prefix+EPCISPreferences.getNewEventCounter());
		return eventIRI;
	}

	
	//get the event IRI for  a specific prefix
	public String getIRIForPrefix(String prefix)
	{
		return namespacePrefixes.get(prefix);
	}
	
	
	// set the base IRI for events and the prefix
	public void setIRIandPrefix(String eventIRI, String prefix) {
	
		namespacePrefixes.remove(prefix);
		namespacePrefixes.put(prefix, baseIRI.concat(eventIRI));
	}
	
    // A map of namespace-to-prefix
	public Map getNameSpacesPrefixes() {
	   return namespacePrefixes;
	}
}
