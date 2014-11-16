/**
 * 
 */
package org.purl.linkedepcis.eem;

import java.util.ArrayList;

import org.openrdf.model.Graph;
import org.openrdf.model.URI;

/**
 * @author monika 
 * A class representing the Aggregation event
 */
public class AggregationEvent extends EPCISEvent{

	private String aggregationID;
	private URI aggregationURI;
	// an array to the hold the EPC list
		private ArrayList<String> epcArray = null;
	
	public AggregationEvent(Namespaces ns, String eventPrefix, String aggregationPrefix, String aggregationID) {
		super(ns, eventPrefix, aggregationPrefix, aggregationID, "AggregationEvent");
		// setup the epclist
				epcArray = new ArrayList<String>();
	}
        
        public URI getAggregationURI()
        {
            
            
            return super.getAggregationURI();
            
        }
	
	public AggregationEvent(Namespaces ns, String prefix, URI aggregationURI) {
            
            
		
	}
	
	// add the EPC to the event
		public void addEPCTOAggregationEvent(EPC epc, String prefix) {

			epcArray.add(ns.getIRIForPrefix(prefix) + epc.getEPCValue());
			
		}
		

	/*public void persistEvent()
	{
		super.persistEvent(epcArray);
	}*/
	
	public void persistEvent(Graph g, String file)
	{
		super.persistEvent(g, epcArray, file);
	}
	
	public void persistEvent(String file)
	{
		super.persistEvent(epcArray, file);
	}
	

       
}
