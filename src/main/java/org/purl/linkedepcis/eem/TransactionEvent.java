/**
 * 
 */
package org.purl.linkedepcis.eem;

import java.util.ArrayList;

import org.openrdf.model.Graph;

/**
 * @author monika
 *
 */
public class TransactionEvent extends EPCISEvent{
	
	// an array to the hold the EPC list
		private ArrayList<String> epcArray = null;

		
		

		
		public TransactionEvent(Namespaces ns, String eventPrefix) {
			
			super(ns, eventPrefix, "TransactionEvent");
		
			// setup the epclist
			epcArray = new ArrayList<String>();

		}

		// add the EPC to the event
		public void addEPCTOEvent(EPC epc, String prefix) {

			epcArray.add(ns.getIRIForPrefix(prefix) + epc.getEPCValue());
					
		}
		
	  /* 	public void persistEvent()
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
