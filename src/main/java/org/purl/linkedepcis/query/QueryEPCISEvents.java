/**
 *
 */
package org.purl.linkedepcis.query;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import org.openrdf.query.BindingSet;
import org.purl.linkedepcis.utils.CommonFunctions;

/**
 * @author monika
 *
 */
public class QueryEPCISEvents implements EPCISQueryInterface {

    private CommonFunctions cf = null;
    private InputStream config = null;
    private String defaultNS = null;
    private String updateString = null;
    private Map<String, Object> siteData = null;
    private int numberOfStatements = 0;
    private String prefix = "";

    public QueryEPCISEvents() {
        prefix = "PREFIX eem:<http://fispace.aston.ac.uk/ontologies/eem#>";
        prefix = prefix + "PREFIX sesame: <http://www.openrdf.org/schema/sesame#>"
                + "PREFIX co: <http://purl.org/co#>"
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>";
        try {
            cf = new CommonFunctions();
            config = QueryEPCISEvents.class.getClassLoader().getResourceAsStream("conf/linkedEpcis.ttl");;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // config = "/Users/monika/Documents/workspace/BioMass/biomass.ttl";
        defaultNS = "http://fispace.aston.ac.uk/linkedEPCIS/franzfarmer/data/events/";

    }

    //open the config stream
    private void setConfigStream() {
        config = QueryEPCISEvents.class.getClassLoader().getResourceAsStream("conf/linkedEpcis.ttl");

    }

    //close the config stream
    private void closeConfigStream() {
        try {
            config.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // send the query to the repository
    private ArrayList<BindingSet> sendQueryToRepos(String query) {
        setConfigStream();
        ArrayList<BindingSet> results = cf.returnQueryResults(query, config,
                defaultNS);
        closeConfigStream();
        return results;
    }

    public ArrayList<String> getEventsInRepository() {
        ArrayList<String> results = new ArrayList<String>();

        System.out.println(config);

        String query = prefix + " SELECT ?event  WHERE{"
                + "?subclass sesame:directSubClassOf  eem:EPCISEvent."
                + "?event a ?subclass."
                + "} ";
        ArrayList<BindingSet> bst = sendQueryToRepos(query);
        for (BindingSet b1 : bst) {
            System.out.println(b1.getValue("event"));
            results.add(b1.getValue("event").toString());
        }
        setConfigStream();
        query = prefix + " CONSTRUCT {?event a ?subclass } WHERE {"
                + "?subclass sesame:directSubClassOf  eem:EPCISEvent."
                + "?event a ?subclass. }";
        cf.getGraphQueryResults(query, config, defaultNS);

        return results;
    }

    /* (non-Javadoc)
     * @see solanki.uk.epcis.utils.EPCISQueryInterface#getAddressesInRepository()
     */
    @Override
    public ArrayList<String> getAddressesInRepository() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see solanki.uk.epcis.utils.EPCISQueryInterface#getTransactionsInRepository()
     */
    @Override
    public ArrayList<String> getTransactionsInRepository() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see solanki.uk.epcis.utils.EPCISQueryInterface#getReadersInRepository()
     */
    @Override
    public ArrayList<String> getReadersInRepository() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see solanki.uk.epcis.utils.EPCISQueryInterface#getEventDetails(java.lang.String)
     */
    @Override
    public ArrayList<ArrayList<String>> getEventDetails(String eventID) {

        ArrayList<String> property = new ArrayList<String>();
        ArrayList<String> value = new ArrayList<String>();
        ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
        System.out.println(config);

        String query = prefix + " SELECT * "
                + " WHERE {"
                + "?subclass sesame:directSubClassOf  eem:EPCISEvent."
                + "?s a ?subclass."
                + "?s eem:hasEventID \"" + eventID + "\";"
                + "?p ?o."
                + " FILTER (!isBlank(?o)) \n"
                + ""
                + "}";
        ArrayList<BindingSet> bst = sendQueryToRepos(query);
        /*	System.out.println("size of details" +bst.size());
         for (BindingSet b1 : bst) {
         property.add("Event type");
         value.add(b1.getValue("subclass").toString());
         property.add("Label");
         value.add(b1.getValue("label").toString());
         property.add("Comment");
         value.add(b1.getValue("comment").toString());
         property.add("Event ID");
         value.add(eventID);
         property.add("Action ");
         value.add(b1.getValue("action").toString());
         property.add("Disposition");
         value.add(b1.getValue("dsp").toString());
         property.add("Event occurred at");
         value.add(b1.getValue("occurred").toString());
         property.add("Event recorded at");
         value.add(b1.getValue("record").toString());
         property.add("Time Zone offset");
         value.add(b1.getValue("offset").toString());
         
			
         }
         results.add(property);
         results.add(value);
		
         property.add("List of EPC");
      
		
         query=prefix +" SELECT * " + 
         " WHERE {" +
	 	  	  
         "?s eem:hasEventID \""+eventID+"\";" +
         "   eem:associatedWithEPCList ?y. " +
         "?y co:element ?list." +
         "}" ;
         bst = sendQueryToRepos(query);
         System.out.println("size of epc list" +bst.size());
         String listString="";
         for (BindingSet b1 : bst) {
         listString =listString+b1.getValue("list")+"\n";
         }
         value.add(listString);*/
        setConfigStream();
        query = prefix + " CONSTRUCT "
                + "{ "
                + "?s a ?subclass."
                + "?s eem:action ?c; "
                + "   eem:hasBusinessStepType ?bst;\n"
                + "   eem:hasDisposition ?dsp;\n"
                + "   eem:hasEventID \"" + eventID + "\";"
                + "   eem:eventRecordedAt ?record;\n"
                + "   eem:eventOccurredAt ?occurred;\n"
                + "   eem:eventTimeZoneOffset ?offset;\n"
                + "   eem:hasReadPointLocation ?rloc;\n"
                + "   eem:hasBusinessLocation ?bizLoc;"
                + "    eem:associatedWithEPCList ?y."
                + " ?y co:element ?x."
                + "} WHERE {"
                + "?subclass sesame:directSubClassOf  eem:EPCISEvent."
                + "?s a ?subclass."
                + "?s eem:hasEventID \"" + eventID + "\";"
                + "  eem:hasBusinessStepType ?bst;\n"
                + "   eem:hasDisposition ?dsp;\n"
                + "   eem:action ?action;\n"
                + "   eem:eventRecordedAt ?record;\n"
                + "   eem:eventOccurredAt ?occurred;\n"
                + "   eem:eventTimeZoneOffset ?offset;\n"
                + "   eem:hasReadPointLocation ?rloc;\n"
                + "   eem:hasBusinessLocation ?bizLoc;"
                + "   eem:associatedWithEPCList ?y. "
                + "?y co:element ?x."
                + "}";
        cf.getGraphQueryResults(query, config, defaultNS);
        return results;
    }

}
