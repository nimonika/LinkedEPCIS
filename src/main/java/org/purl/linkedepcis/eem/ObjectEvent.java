
 
package org.purl.linkedepcis.eem;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.openrdf.model.Graph;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;


/**
 * @author monika
 *
 */
public class ObjectEvent extends EPCISEvent {

	// an array to the hold the EPC list
	private ArrayList<String> epcArray = null;

	
	

	
	public ObjectEvent(Namespaces ns, String eventPrefix) {
		
		super(ns, eventPrefix, "ObjectEvent");
	
		// setup the epclist
		epcArray = new ArrayList<String>();

	}

// add the EPC to the event
	public void addEPCTOEvent(EPC epc, String prefix) {

		epcArray.add(ns.getIRIForPrefix(prefix) + epc.getEPCValue());
		
	}
	
	//add EPCArray to the event
        
        public void addEPCListToEvent(ArrayList<EPC> listOfEPCs, String prefix)
        {
          
            for(EPC s: listOfEPCs)
            {
                epcArray.add(ns.getIRIForPrefix(prefix) + s.getEPCValue());
            }
        }

	/*public void persistEvent()
	{
		super.persistEvent(epcArray);
	}*/
	
	public void persistEvent(String file)
	{
		super.persistEvent(epcArray, file);
	}
	
	public void persistEvent(Graph g, String file)
	{
		super.persistEvent(g, epcArray, file);
	}

}
