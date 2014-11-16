package org.purl.linkedepcis.eem;

import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;
import org.openrdf.model.Graph;
import org.openrdf.model.Literal;
import org.openrdf.model.Model;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.purl.linkedepcis.cbv.BusinessStep;
import org.purl.linkedepcis.cbv.Disposition;
import org.purl.linkedepcis.cbv.Site;
import org.purl.linkedepcis.cbv.TransactionType;
import org.purl.linkedepcis.utils.EPCISPreferences;


/**
 * @author monika THis class is the base class for all EPCIS events. It exploits
 *         the Sesame libraries for generating the graph model for EPCIS events,
 *         based on the vocabularies defined at
 *         http://windermere.aston.ac.uk/FISpace/ontologies/epcis/eem.owl and
 *         http://windermere.aston.ac.uk/FISpace/ontologies/epcis/cbv.owl
 */
public abstract class EPCISEvent {

	protected Graph myGraph = null;
	protected ValueFactory myFactory = null;
	protected URI myObject = null;
	protected URI mySubject = null;
	protected URI myPredicate = null;
	protected Literal myLiteral = null;
	private EPCISCommon epcs = null;
	protected Namespaces ns = new Namespaces();
	protected ArrayList<Transaction> transactions = null;
	Logger logger = Logger.getLogger(EPCISEvent.class);
        protected URI eventURI=null;
         URI aggregationURI=null;
         

	/**
	 * @param recordTime the recordTime to set
	 */
	public void setRecordTime(Date recordTime) {
	//	logger.info(mySubject+"...is the subject");
		myGraph=epcs.setEventRecordedTime(ns, myGraph, mySubject, epcs.getCurrentTimeAndDateInXMLGregorianCalendar());
	}

	private void setupBaseEventModel(Namespaces ns1, String eventPrefix,
			String eventType) {
		// an empty sesame graph
		myGraph = new org.openrdf.model.impl.LinkedHashModel();
		myFactory = ValueFactoryImpl.getInstance();

		epcs = new EPCISCommon();
		this.ns = ns1;
              //  logger.info(ns.getIRIForPrefix("epc"));
                //extract the graph URI
              
		// create the event
		mySubject = myFactory.createURI(ns.getNewEventIRI(eventPrefix));
                eventURI=mySubject;
		myObject = myFactory.createURI(ns.getIRIForPrefix("eem"), eventType);
                
               
		myGraph.add(mySubject, RDF.TYPE, myObject);
		
		myGraph.add(
				mySubject,
				myFactory.createURI(ns.getIRIForPrefix("eem"), "hasEventID"),
				myFactory.createLiteral(eventPrefix + EPCISPreferences.getEventCounter()));
		myGraph.add(
				mySubject,
				RDFS.LABEL,
				myFactory.createLiteral("An " + eventType + " with event ID "
						+ eventPrefix + EPCISPreferences.getEventCounter()));
		setTemporalProperties(ns, myGraph, mySubject);
		transactions = new ArrayList<Transaction>();
	}

	public EPCISEvent() {

	}

	protected EPCISEvent(Namespaces ns, String eventPrefix,
			String aggregationPrefix, String aggregationID, String eventType) {
		setupBaseEventModel(ns, eventPrefix, eventType);
                aggregationURI=myFactory.createURI(
				ns.getIRIForPrefix(aggregationPrefix), aggregationID);
		myGraph.add(mySubject, myFactory.createURI(ns.getIRIForPrefix("eem"),
				"hasAggregationURI"), aggregationURI);
	}

    protected URI getAggregationURI() {
        return aggregationURI;
    }

	protected EPCISEvent(Namespaces ns, String eventPrefix, String eventType) {

		setupBaseEventModel(ns, eventPrefix, eventType);

	}

	/**
	 * @param ns
	 * @param prefix
	 * @param epcClassPrefix
	 * @param epcClass
	 * @param quantity
	 * @param eventType
	 * 
	 * 
	 */
	protected EPCISEvent(Namespaces ns, String eventPrefix,
			String epcClassPrefix, String epcClass, int quantity,
			String eventType) {
		setupBaseEventModel(ns, eventPrefix, eventType);
		myGraph.add(mySubject, myFactory.createURI(ns.getIRIForPrefix("eem"),
				"hasEPCClass"), myFactory.createURI(
				ns.getIRIForPrefix(epcClassPrefix), epcClass));
		myGraph.add(mySubject,
				myFactory.createURI(ns.getIRIForPrefix("eem"), "quantity"),
				myFactory.createLiteral(quantity));

	}

	// add action to the event
	public void setAction(Action action) {
		myGraph = epcs.setAction(ns, myGraph, mySubject, action.toString());
	}

	// add action to the event
	public void setAction(URI actionURI) {
		myGraph = epcs.setAction(ns, myGraph, mySubject, actionURI);
	}

	// add business step to the event
	public void setBusinessStepType(BusinessStep businessStepType) {
		myGraph = epcs.setBusinessStepType(ns, myGraph, mySubject,
				businessStepType.toString());
	}

	// set disposition for the event
	public void setDisposition(Disposition disposition) {
		myGraph = epcs.setDisposition(ns, myGraph, mySubject, disposition.toString());
	}

	// set disposition for the event
	public void setDisposition(URI disposition) {
		myGraph = epcs.setDisposition(ns, myGraph, mySubject, disposition);
	}

	// add business transaction to the event
	public void addBusinessTransactionToEvent(TransactionType transactionType, 
			String transactionID, String transactionIDPrefix) {
		// System.out.println("hello***************");
		Transaction trans = new Transaction();
		trans.setTransactionID(ns.getIRIForPrefix(transactionIDPrefix)
				+ transactionID);
		trans.setTransactionType(transactionType.toString().toLowerCase());
		transactions.add(trans);

	}

	// persist transactions
	private void persistTransaction() {
		// System.out.println(transactions.size());
		if (transactions.size() > 0) {

			myGraph = epcs.setBusinessTransaction(ns, myGraph, mySubject,
					transactions);
		}
	}

	public void persistEvent(String file) {
		persistTransaction();
		epcs.persistGraphToFile(myGraph, file, ns);
	}
	

	
	public Graph returnEventGraph()
	{
		return myGraph;
	}
	
	public void addNewTriplesForTheEvent(URI predicate, URI object)
	{
		myGraph.add(mySubject, predicate, object);
	}
	
	public void addNewTriplesForTheEvent(URI predicate, Literal literal)
	{
		myGraph.add(mySubject, predicate, literal);
	}
	
	
	
	//method to persist with Graph
	protected void persistEvent(Graph g, ArrayList epcArray, String file) {
		myGraph=g;
		persistEvent(epcArray);
		epcs.persistGraphToFile(myGraph, file, ns);
               
              
	}

	// method for persisting the event
	protected void persistEvent(ArrayList epcArray) {

		persistTransaction();
		myGraph = epcs.addEPCsToGraph(ns, myGraph, mySubject, epcArray);
                
              
	}

	// method for persisting the event
	protected void persistEvent(ArrayList epcArray, String file) {
		persistEvent(epcArray);
		epcs.persistGraphToFile(myGraph, file, ns);
                
             
	}

        
         
        
        
	private Graph setTemporalProperties(Namespaces ns, Graph myGraph,
			URI subject) {
		myGraph = epcs.setTemporalProperties(ns, myGraph, subject);
		return myGraph;

	}

	// set the business location of the event
	public void setBusinessLocation(Namespaces ns, String prefix,
			Site location) {
		myGraph = epcs.setLocation(ns, prefix, myGraph, mySubject, location,
				"businessLocation");
	}

	// set the business location of the event with location URI included in the
	// location object
	public void setBusinessLocation(Site location) {
		myGraph = epcs.setLocation(ns, mySubject, myGraph, location,
				"businessLocation");
	}

	// set the read point location for the event
	public void setReadPointLocation(Site location) {

		myGraph = epcs.setLocation(ns, mySubject, myGraph, location,
				"readPoint");
	}

	// set the business location of the event
	public void setReadPointLocation(Namespaces ns, String prefix,
			Site location) {
		myGraph = epcs.setLocation(ns, prefix, myGraph, mySubject, location,
				"readPoint");
	}

	public void setReaderForEvent(Reader reader, String prefix) {
		myGraph = epcs
				.setReaderForEvent(ns, prefix, myGraph, mySubject, reader);
	}
        
         public URI getEventURI()
        {
            
            return eventURI;
        }
}
