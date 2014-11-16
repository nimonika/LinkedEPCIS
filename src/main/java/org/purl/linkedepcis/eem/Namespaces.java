package org.purl.linkedepcis.eem;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.purl.linkedepcis.utils.EPCISPreferences;

public class Namespaces {

    // base IRI for events 
    private String eventIRI;
    Logger logger = Logger.getLogger(Namespaces.class);

    //a Map returning all the prefixes and IRIs
    private Map<String, String> namespacePrefixes;
    private String baseIRI = "";

    public String getBaseIRI() {
        return baseIRI;
    }
    private String contextURI = "";

    public String getContextURI() {
        return namespacePrefixes.get("context");
    }

    public void setContextURI(String contextURI, String artifact) {

        namespacePrefixes.put("context", baseIRI.concat(contextURI) + artifact + EPCISPreferences.getEventCounter());
    }
    
     public void setContextURI(String contextURI) {

        namespacePrefixes.put("context", baseIRI.concat(contextURI) + "event" + EPCISPreferences.getEventCounter());
    }

    public Namespaces(String baseURI) {
        this();
        this.baseIRI = baseURI;

    }

    public Namespaces() {
        namespacePrefixes = new HashMap<String, String>();
        setIRIandPrefix("eem", "http://purl.org/eem#");
        setIRIandPrefix("co", "http://purl.org/co#");
        setIRIandPrefix("cbv", "http://purl.org/cbv#");
        setIRIandPrefix("vcard", "http://www.w3.org/2006/vcard/ns#");
        setIRIandPrefix("wgs84", "http://www.w3.org/2003/01/geo/wgs84_pos#");
        setIRIandPrefix("ped", "http://purl.org/pedigree#");
        setIRIandPrefix("dc", "http://purl.org/dc/elements/1.1/");
    }

    //return the eventIRI by appending the 
    public String getNewEventIRI(String prefix) {
        EPCISCommon epcs = new EPCISCommon();
//                logger.info(prefix);
//                logger.info(EPCISPreferences.getNewEventCounter());
//                logger.info(getIRIForPrefix(prefix));
        eventIRI = getIRIForPrefix(prefix).concat(prefix + EPCISPreferences.getNewEventCounter());
//                logger.info(eventIRI);
        return eventIRI;
    }

    //get the event IRI for  a specific prefix
    public String getIRIForPrefix(String prefix) {
//             for (Entry<String, String> entry : namespacePrefixes.entrySet()) {
//            String key=entry.getKey();
//            String value=entry.getValue();
//             System.out.println("key=" + entry.getKey() + ", value=" + entry.getValue());
//        }
//            logger.info(prefix);
//          logger.info(namespacePrefixes.get(prefix));

        return namespacePrefixes.get(prefix);
    }

    // set te base IRI for events and the prefix
    public void setIRIandPrefix(String prefix, String eventIRI) {

        //	namespacePrefixes.remove(prefix);
        namespacePrefixes.put(prefix, baseIRI.concat(eventIRI));
        //  logger.info(namespacePrefixes.get(prefix));
    }

    // A map of namespace-to-prefix
    public Map getNameSpacesPrefixes() {
        return namespacePrefixes;
    }

}
