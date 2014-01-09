/**
 *
 */
package org.purl.linkedepcis.examples;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;

import org.openrdf.model.Graph;
import org.openrdf.model.impl.ValueFactoryImpl;

import org.openrdf.model.vocabulary.RDFS;
import org.purl.linkedepcis.cbv.Address;
import org.purl.linkedepcis.cbv.BusinessStep;
import org.purl.linkedepcis.cbv.Disposition;
import org.purl.linkedepcis.cbv.Site;
import org.purl.linkedepcis.cbv.SubSite;
import org.purl.linkedepcis.cbv.SubSiteAttribute;
import org.purl.linkedepcis.cbv.SubSiteType;
import org.purl.linkedepcis.cbv.TransactionType;
import org.purl.linkedepcis.eem.Action;
import org.purl.linkedepcis.eem.AggregationEvent;
import org.purl.linkedepcis.eem.EPC;
import org.purl.linkedepcis.eem.Namespaces;
import org.purl.linkedepcis.eem.ObjectEvent;
import org.purl.linkedepcis.eem.QuantityEvent;
import org.purl.linkedepcis.eem.Reader;
import org.purl.linkedepcis.eem.TransactionEvent;

/**
 * @author monika
 *
 */
public class GenerateEventsForFranzFarmer {

    static Logger logger = Logger.getLogger(GenerateEventsForFranzFarmer.class);
    private Namespaces ns = null;

    public static void main(String args[]) throws InterruptedException {
        GenerateEventsForFranzFarmer ge = new GenerateEventsForFranzFarmer();

        ge.generateObjectEventForCommissioningOfPunnets();
        ge.aggregateEventForPackingFirstCase();
        ge.aggregateEventForPackingSecondCase();
      //  logger.info("quantity event ");
        ge.generateQuantityEventForStoring();
        ge.generateTransactionEventForShipping();
    }

    /**
     *
     */
    private void generateTransactionEventForShipping() {
        TransactionEvent transEvent = new TransactionEvent(ns, "eve");
        //create the EPC
        EPC epc1 = new EPC("4053213.0666666666");
        EPC epc2 = new EPC("4053213.06666666667");

        //associate the object event with the epcs
        transEvent.addEPCTOEvent(epc1, "age");
        transEvent.addEPCTOEvent(epc2, "age");

        //set the other properties 
        transEvent.setAction(Action.OBSERVE);
        transEvent.setBusinessStepType(BusinessStep.SHIPPING);
        transEvent.setDisposition(Disposition.IN_TRANSIT);
        transEvent.addBusinessTransactionToEvent(TransactionType.PO, "4711", "tpo");

        //instantiate the business or read point location address
        Address address1 = new Address();
        address1.setCountry("UK");
        address1.setStreetName("University road");
        address1.setPostCode("LE1 7RH");
        address1.setLatitude(23.45);
        address1.setLongitude(5.56);

        // instantiate the subsite attributes
        ArrayList<SubSiteAttribute> ssta = new ArrayList<SubSiteAttribute>();
        ssta.add(SubSiteAttribute.FROZEN);
        ssta.add(SubSiteAttribute.SHELF);

        //instantiate the subsite
        SubSite subSite = new SubSite(SubSiteType.PACKAGING_AREA);
        //set the subsite attributes and the ssubsite details for the site
        subSite.setSubsSiteAttribute(ssta);
        subSite.setSubSiteDetail("store room");
        //instantiate site
        Site site = new Site("0614141.00300.0");
        //set the address for the site
        site.setAddress(address1);
        //set the subsite for the site
        site.setSubSite(subSite);

        //set another site, but without the subsite details
        Site site2 = new Site("0614141.00300.0", address1);

        //set the business location for the event
        transEvent.setBusinessLocation(ns, "loc", site2);
        //set the read point location for the site
        transEvent.setReadPointLocation(ns, "loc", site);
//logger.info("transaction event");
        transEvent.persistEvent("franz_trans.ttl");
    }

    /**
     *
     */
    private void generateQuantityEventForStoring() {

    //    logger.info("inside ");
        //create the quantity event
        QuantityEvent qtEvent = new QuantityEvent(ns, "eve", "qte", "4012345.022334.*", 20);

        //set the other properties 
        qtEvent.setAction(Action.OBSERVE);
        qtEvent.setBusinessStepType(BusinessStep.STORING);
        qtEvent.setDisposition(Disposition.SELLABLE_NOT_ACCESSIBLE);

        //instantiate the business or read point location address
        Address address1 = new Address();
        address1.setCountry("UK");
        address1.setStreetName("University road");
        address1.setPostCode("LE1 7RH");
        address1.setLatitude(23.45);
        address1.setLongitude(5.56);

        // instantiate the subsite attributes
        ArrayList<SubSiteAttribute> ssta = new ArrayList<SubSiteAttribute>();
        ssta.add(SubSiteAttribute.FROZEN);
        ssta.add(SubSiteAttribute.SHELF);

        //instantiate the subsite
        SubSite subSite = new SubSite(SubSiteType.PACKAGING_AREA);
        //set the subsite attributes and the ssubsite details for the site
        subSite.setSubsSiteAttribute(ssta);
        subSite.setSubSiteDetail("store room");
        //instantiate site
        Site site = new Site("0614141.00300.0");
        //set the address for the site
        site.setAddress(address1);
        //set the subsite for the site
        site.setSubSite(subSite);

        //set another site, but without the subsite details
        Site site2 = new Site("0614141.00300.0", address1);
        qtEvent.setBusinessLocation(ns, "loc", site2);
        qtEvent.setReadPointLocation(ns, "loc", site);

      //    logger.info("outside ");
        qtEvent.persistEvent("franz_quantity.ttl");
    }

    public GenerateEventsForFranzFarmer() {
        //setup namespaces. The constructor takes the baseURI for the namespaces.
        ns = new Namespaces("http://fispace.aston.ac.uk/franzfarmer/data/");
        ns.setIRIandPrefix("addr2", "addresses/id/");
        ns.setIRIandPrefix("rdr2", "readers/id/");
        ns.setIRIandPrefix("eve", "epc/id/events/");
        ns.setIRIandPrefix("epc", "epc/id/sgtin/");
        ns.setIRIandPrefix("rdr", "readers/id/");
        ns.setIRIandPrefix("loc", "epc/id/sgln/");
        ns.setIRIandPrefix("tpo", "transactions/po/bt/id/");
        ns.setIRIandPrefix("tpd", "transactions/desadv/bt/id/");
        //setup namespaces for quantity event, EPCClass
        ns.setIRIandPrefix("qte", "epc/events/idpat/sgtin/id/");

        //setup namespaces for Event
        ns.setIRIandPrefix("age", "epc/id/sgtin/");

    }

    /**
     *
     */
    private void aggregateEventForPackingFirstCase() {
        // set the reader for the event
        Reader reader = new Reader(ns, "rdr", "reader102", "123-456-789");

        // create the event
        AggregationEvent age1 = new AggregationEvent(ns, "eve", "age", "4053213.0666666666");

        age1.setReaderForEvent(reader, "rdr");
        // create the EPCs
        EPC epc1 = new EPC("4053213-022222-1234");

        EPC epc2 = new EPC("4053213/022222/1235");
        EPC epc3 = new EPC("4053213.022222.1236");
        EPC epc4 = new EPC("4053213.022222.1237");

        // associate the object event with the epcs
        age1.addEPCTOAggregationEvent(epc1, "epc");
        age1.addEPCTOAggregationEvent(epc2, "epc");
        age1.addEPCTOAggregationEvent(epc3, "epc");
        age1.addEPCTOAggregationEvent(epc4, "epc");

        // set the other properties, select from values defined in the vocabulary at 
        age1.setAction(Action.ADD);
        age1.setBusinessStepType(BusinessStep.PACKING);
        age1.setDisposition(Disposition.SELLABLE_NOT_ACCESSIBLE);

        age1.setRecordTime(new GregorianCalendar().getTime());
        /*obe1.addBusinessTransactionToEvent("po", "45678", "tpo");
         obe1.addBusinessTransactionToEvent("desadv", "45679", "tpd");
         */
        //instantiate the business or read point location address
        Address address1 = new Address();
        address1.setCountry("UK");
        address1.setStreetName("University road");
        address1.setPostCode("LE1 7RH");
        address1.setLatitude(23.45);
        address1.setLongitude(5.56);

        // instantiate the subsite attributes
        ArrayList<SubSiteAttribute> ssta = new ArrayList<SubSiteAttribute>();
        ssta.add(SubSiteAttribute.FROZEN);
        ssta.add(SubSiteAttribute.SHELF);

        //instantiate the subsite
        SubSite subSite = new SubSite(SubSiteType.PACKAGING_AREA);
        //set the subsite attributes and the ssubsite details for the site
        subSite.setSubsSiteAttribute(ssta);
        subSite.setSubSiteDetail("store room");
        //instantiate site
        Site site = new Site("0614141.00300.0");
        //set the address for the site
        site.setAddress(address1);
        //set the subsite for the site
        site.setSubSite(subSite);

        //set another site, but without the subsite details
        Site site2 = new Site("0614141.00300.0", address1);
        age1.setBusinessLocation(ns, "loc", site2);
        age1.setReadPointLocation(ns, "loc", site);

        // add a new predicate to the graph. This takes care of extensions
        Graph g = age1.returnEventGraph();
        age1.addNewTriplesForTheEvent(
                ValueFactoryImpl.getInstance().createURI(
                        "http://abc.com/predicate#prd"), ValueFactoryImpl
                .getInstance().createLiteral("10"));
        // save the event
        age1.persistEvent(g, "franz_age1.ttl");

    }

    private void aggregateEventForPackingSecondCase() {
        // set the reader for the event
        Reader reader = new Reader(ns, "rdr", "reader102", "123-456-789");

        // create the event
        AggregationEvent age1 = new AggregationEvent(ns, "eve", "age", "4053213.0666666667");

        age1.setReaderForEvent(reader, "rdr");
        // create the EPCs
        EPC epc1 = new EPC("4053213.022222.1238");

        EPC epc2 = new EPC("4053213.022222.1239");
        EPC epc3 = new EPC("4053213.022222.12310");
        EPC epc4 = new EPC("4053213.022222.12311");

        // associate the object event with the epcs
        age1.addEPCTOAggregationEvent(epc1, "epc");
        age1.addEPCTOAggregationEvent(epc2, "epc");
        age1.addEPCTOAggregationEvent(epc3, "epc");
        age1.addEPCTOAggregationEvent(epc4, "epc");

        // set the other properties, select from values defined in the vocabulary at 
        age1.setAction(Action.ADD);
        age1.setBusinessStepType(BusinessStep.PACKING);
        age1.setDisposition(Disposition.IN_PROGRESS);

        age1.setRecordTime(new GregorianCalendar().getTime());
        /*obe1.addBusinessTransactionToEvent("po", "45678", "tpo");
         obe1.addBusinessTransactionToEvent("desadv", "45679", "tpd");
         */
        //instantiate the business or read point location address
        Address address1 = new Address();
        address1.setCountry("UK");
        address1.setStreetName("University road");
        address1.setPostCode("LE1 7RH");
        address1.setLatitude(23.45);
        address1.setLongitude(5.56);

        // instantiate the subsite attributes
        ArrayList<SubSiteAttribute> ssta = new ArrayList<SubSiteAttribute>();
        ssta.add(SubSiteAttribute.FROZEN);
        ssta.add(SubSiteAttribute.SHELF);

        //instantiate the subsite
        SubSite subSite = new SubSite(SubSiteType.PACKAGING_AREA);
        //set the subsite attributes and the ssubsite details for the site
        subSite.setSubsSiteAttribute(ssta);
        subSite.setSubSiteDetail("store room");
        //instantiate site
        Site site = new Site("0614141.00300.0");
        //set the address for the site
        site.setAddress(address1);
        //set the subsite for the site
        site.setSubSite(subSite);

        //set another site, but without the subsite details
        Site site2 = new Site("0614141.00300.0", address1);
        age1.setBusinessLocation(ns, "loc", site2);
        age1.setReadPointLocation(ns, "loc", site);

        // add a new predicate to the graph. This takes care of extensions
        Graph g = age1.returnEventGraph();
        age1.addNewTriplesForTheEvent(
                ValueFactoryImpl.getInstance().createURI(
                        "http://abc.com/predicate#prd"), ValueFactoryImpl
                .getInstance().createLiteral("10"));
        // save the event
        age1.persistEvent(g, "franz_age2.ttl");

    }

    // how to generate an ObjectEvent
    private void generateObjectEventForCommissioningOfPunnets() {

        // set the reader for the event
        Reader reader = new Reader(ns, "rdr", "reader102", "123-456-789");

        // create the event
        ObjectEvent obe1 = new ObjectEvent(ns, "eve");

        obe1.setReaderForEvent(reader, "rdr");

        // create the EPCs
        EPC epc1 = new EPC("4053213.022222.1234");
        EPC epc2 = new EPC("4053213.022222.1235");
        EPC epc3 = new EPC("4053213.022222.1236");
        EPC epc4 = new EPC("4053213.022222.1237");

        // create the EPCs
        EPC epc5 = new EPC("4053213.022222.1238");
        EPC epc6 = new EPC("4053213.022222.1239");
        EPC epc7 = new EPC("4053213.022222.12310");
        EPC epc8 = new EPC("4053213.022222.12311");

        // associate the object event with the epcs
        obe1.addEPCTOEvent(epc1, "epc");
        obe1.addEPCTOEvent(epc2, "epc");
        obe1.addEPCTOEvent(epc3, "epc");
        obe1.addEPCTOEvent(epc4, "epc");
        // associate the object event with the epcs
        obe1.addEPCTOEvent(epc5, "epc");
        obe1.addEPCTOEvent(epc6, "epc");
        obe1.addEPCTOEvent(epc7, "epc");
        obe1.addEPCTOEvent(epc8, "epc");

		// set the other properties, select from values defined in the
        // vocabulary at
        obe1.setAction(Action.ADD);
        obe1.setBusinessStepType(BusinessStep.COMMISSIONING);
        obe1.setDisposition(Disposition.ACTIVE);

        obe1.setRecordTime(new GregorianCalendar().getTime());
        /*
         * obe1.addBusinessTransactionToEvent("po", "45678", "tpo");
         * obe1.addBusinessTransactionToEvent("desadv", "45679", "tpd");
         */
        // instantiate the business or read point location address
        Address address1 = new Address();
        address1.setCountry("UK");
        address1.setStreetName("University road");
        address1.setPostCode("LE1 7RH");
        address1.setLatitude(23.45);
        address1.setLongitude(5.56);

        // instantiate the subsite attributes
        ArrayList<SubSiteAttribute> ssta = new ArrayList<SubSiteAttribute>();
        ssta.add(SubSiteAttribute.FROZEN);
        ssta.add(SubSiteAttribute.SHELF);

        // instantiate the subsite
        SubSite subSite = new SubSite(SubSiteType.PACKAGING_AREA);
        // set the subsite attributes and the ssubsite details for the site
        subSite.setSubsSiteAttribute(ssta);
        subSite.setSubSiteDetail("store room");
        // instantiate site
        Site site = new Site("0614141.00300.0");
        // set the address for the site
        site.setAddress(address1);
        // set the subsite for the site
        site.setSubSite(subSite);

        // set another site, but without the subsite details
        Site site2 = new Site("0614141.00300.1", address1);
        obe1.setBusinessLocation(ns, "loc", site2);
        obe1.setReadPointLocation(ns, "loc", site);

		// add a new predicate to the graph
        Graph g = obe1.returnEventGraph();
        obe1.addNewTriplesForTheEvent(
                ValueFactoryImpl.getInstance().createURI(
                        "http://fispace.aston.ac.uk/predicate#newPredicate"),
                ValueFactoryImpl.getInstance().createLiteral(
                        "new literal example"));

        obe1.addNewTriplesForTheEvent(
                RDFS.COMMENT,
                ValueFactoryImpl.getInstance().createLiteral(
                        "The object event for commissoning of tomato punnets"));

        // save the event
        obe1.persistEvent(g, "franz_tomato_punnets_commissioning.ttl");

    }

}
