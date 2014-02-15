/**
 * 
 */
package org.purl.linkedepcis.examples;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.purl.linkedepcis.cbv.BusinessStep;
import org.purl.linkedepcis.cbv.Disposition;
import org.purl.linkedepcis.cbv.Site;
import org.purl.linkedepcis.cbv.TransactionType;
import org.purl.linkedepcis.eem.Action;
import org.purl.linkedepcis.eem.AggregationEvent;
import org.purl.linkedepcis.eem.EPC;
import org.purl.linkedepcis.eem.Namespaces;
import org.purl.linkedepcis.eem.ObjectEvent;
import org.purl.linkedepcis.pedigree.Pedigree;
import org.purl.linkedepcis.pedigree.PedigreeStatus;



/**
 * @author monika
 *
 */
public class GeneratePharmaPedigreeEvents {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GeneratePharmaPedigreeEvents gppe = new GeneratePharmaPedigreeEvents();
		gppe.generateCommissioningEvent();
		gppe.generatePackingEvent();
		gppe.generateShippingEvent();
              

	}

public GeneratePharmaPedigreeEvents() {
		
		//setup namespaces. The constructor takes the baseURI for the namespaces.
		ns = new Namespaces("http://fispace.aston.ac.uk/ranbaxy/data/");
                //set the context of the graph identifier for the event. This 
           
	
                ns.setContextURI("eventGraphID/") ;
                
		ns.setIRIandPrefix("eve", "epc/id/events/");
             
		ns.setIRIandPrefix("epc", "epc/id/sgtin/");
		ns.setIRIandPrefix("loc", "epc/id/sgln/");
		
		 //setup namespaces for shipping Event
		ns.setIRIandPrefix("age", "epc/id/sscc/");
		//setup namespaces for transaction identifiers
		ns.setIRIandPrefix("tinv", "/bt/inv/id/");
		ns.setIRIandPrefix("tpo", "/bt/po/id/");
                
              

       
              
	}
	
	/**
	 * 
	 */
	private void generateShippingEvent() {
		
		// create the event
		ObjectEvent obe1 = new ObjectEvent(ns, "eve");
		// create the EPCs
		EPC epc1 = new EPC("030001.01234567890");
		obe1.addEPCTOEvent(epc1, "age");
		obe1.setAction(Action.OBSERVE);
		obe1.setBusinessStepType(BusinessStep.SHIPPING);
		obe1.setDisposition(Disposition.IN_TRANSIT);
		Site readSite = new Site("030001.111111.0");
		obe1.setReadPointLocation(ns, "loc", readSite);
		obe1.setBusinessLocation(readSite);
		obe1.addBusinessTransactionToEvent(TransactionType.INV, "0300011111116:A123", "tinv");
		obe1.addBusinessTransactionToEvent(TransactionType.PO, "0399999999991:XYZ567", "tpo");
		obe1.setReadPointLocation(ns, "loc", readSite);
		obe1.persistEvent("pharma_shipping" +".ttl");
		
	}


	public void generatePackingEvent() {
		
		AggregationEvent age= new AggregationEvent(ns, "eve", "age", "030001.1012345.22222223333");
		// create the EPCs
		EPC epc1 = new EPC("030001.0012345.10000000001");
		EPC epc2 = new EPC("030001.0012345.10000000002");
		EPC epc3 = new EPC("030001.0012345.10000000003");
		EPC epc4 = new EPC("030001.0012345.10000000004");
		
		// associate the aggregation event with the epcs
		age.addEPCTOAggregationEvent(epc1, "epc");
		age.addEPCTOAggregationEvent(epc2, "epc");
	        age.addEPCTOAggregationEvent(epc3, "epc");
		age.addEPCTOAggregationEvent(epc4, "epc");
		age.setAction(Action.ADD);
		age.setBusinessStepType(BusinessStep.PACKING);
		age.setDisposition(Disposition.IN_PROGRESS);
		
		Site readSite = new Site("030001.111111.0");
		age.setReadPointLocation(ns, "loc", readSite);
		age.setBusinessLocation(readSite);
                
		age.persistEvent("pharma_packing.ttl");
		
	}

	private Namespaces ns;
	
	


        public void generateCommissioningEvent()
	{
		// create the event
          
		ObjectEvent obe1 = new ObjectEvent(ns, "eve");
		// create the EPCs
		EPC epc1 = new EPC("030001.0012345.10000000001");
		EPC epc2 = new EPC("030001.0012345.10000000002");
		EPC epc3 = new EPC("030001.0012345.10000000003");
		EPC epc4 = new EPC("030001.0012345.10000000004");
		
		// associate the object event with the epcs
		obe1.addEPCTOEvent(epc1, "epc");
		obe1.addEPCTOEvent(epc2, "epc");
	        obe1.addEPCTOEvent(epc3, "epc");
		obe1.addEPCTOEvent(epc4, "epc");
		
                System.out.println(obe1.getEventURI());
		obe1.setAction(Action.ADD);
		obe1.setBusinessStepType(BusinessStep.COMMISSIONING);
		obe1.setDisposition(Disposition.ACTIVE);
		Site readSite = new Site("030001.111111.0");
		obe1.setReadPointLocation(ns, "loc", readSite);
		obe1.setBusinessLocation(readSite);
		obe1.persistEvent("pharma_commissioning.ttl");
	
	}

 
    

	
}
