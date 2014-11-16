package org.purl.linkedepcis.eem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.log4j.Logger;

import org.openrdf.model.BNode;
import org.openrdf.model.Graph;
import org.openrdf.model.Literal;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;
import org.openrdf.rio.UnsupportedRDFormatException;
import org.openrdf.rio.WriterConfig;
import org.openrdf.rio.helpers.BasicWriterSettings;
import org.openrdf.rio.helpers.ContextStatementCollector;
import org.openrdf.rio.turtle.TurtleWriter;

import org.purl.linkedepcis.cbv.Address;
import org.purl.linkedepcis.cbv.Site;
import org.purl.linkedepcis.cbv.SubSite;
import org.purl.linkedepcis.cbv.SubSiteAttribute;

public class EPCISCommon {

    // counter for the event. This increments with every call to getEventIRI();
    private int eventCounter = 0;
    Logger logger = Logger.getLogger(EPCISCommon.class);

    public void addEPCTOEvent() {

    }

    /**
     * @param ns
     * @param mySubject
     * @param myGraph
     * @param epcArray
     */
    public Graph addEPCsToGraph(Namespaces ns, Graph myGraph, URI mySubject,
            ArrayList<String> epcArray) {

        if (epcArray.size() > 0) {
            ValueFactory myFactory = ValueFactoryImpl.getInstance();

            BNode bnodeEPC = myFactory.createBNode();
            // link the EPCS to the event
            myGraph.add(mySubject, myFactory.createURI(
                    ns.getIRIForPrefix("eem"), "associatedWithEPCList"),
                    bnodeEPC);
            myGraph.add(bnodeEPC, RDF.TYPE,
                    myFactory.createURI(ns.getIRIForPrefix("eem"), "SetOfEPCs"));
            for (String e : epcArray) {
                myGraph.add(bnodeEPC,
                        myFactory.createURI("http://purl.org/co#element"),
                        myFactory.createURI(e.trim()));
                // System.out.println(e);
            }
        }
        return myGraph;
    }

    // write the graph to a file with namespace mappings
    public void persistGraphToFile(Graph myGraph, String f, Namespaces namespaces) {
        // printGraph(myGraph);
        try {
          

            FileOutputStream fout = new FileOutputStream(f, true);

        //    logger.info("file to be written ..." + f);
            //     TurtleWriter turtleWriter = new TurtleWriter(fout);

            //      addToStream(namespaces);
            ValueFactory myFactory = ValueFactoryImpl.getInstance();
            Model result = new LinkedHashModel();
            ContextStatementCollector csc = new ContextStatementCollector(result, myFactory, myFactory.createURI(namespaces.getContextURI()));

            WriterConfig config = new WriterConfig();
            config.set(BasicWriterSettings.PRETTY_PRINT, true);

//         //   turtleWriter.setWriterConfig(config);
//            //   turtleWriter.startRDF();
//            csc.startRDF();
//            for (Statement statement : myGraph) {
//                // System.out.println(statement);
//
//                csc.handleStatement(statement);
//
//                //   turtleWriter.handleStatement(statement);
//            }
            Map<String, String> ns = namespaces.getNameSpacesPrefixes();
            for (Map.Entry<String, String> entry : ns.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                //use key and value
              //  logger.info(key + "   " + value);
                //  turtleWriter.handleNamespace(key, value);
                csc.handleNamespace(key, value);

            }

            Rio.write(myGraph, csc);

            //handle namespaces
            if(f.contains("trig"))
              Rio.write(result, fout, RDFFormat.TRIG);
            else
                persistGraphToFile(myGraph, f);
                

//            csc.endRDF();
            //   turtleWriter.endRDF();
            fout.close();

            addToStream(namespaces);
            // System.out.println(f.getAbsolutePath() + " "+f.exists());
        } catch (UnsupportedRDFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RDFHandlerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //	prettyPrintTurtleFormat(f);
    }

    // write the graph to a file
    public void persistGraphToFile(Graph myGraph, String f) {
        // printGraph(myGraph);
        try {
            FileOutputStream fout = new FileOutputStream(f, true);

            TurtleWriter turtleWriter = new TurtleWriter(fout);
            WriterConfig config = new WriterConfig();
            config.set(BasicWriterSettings.PRETTY_PRINT, true);

            turtleWriter.setWriterConfig(config);
            turtleWriter.startRDF();
            for (Statement statement : myGraph) {
                // System.out.println(statement);
                turtleWriter.handleStatement(statement);
            }
            turtleWriter.endRDF();
            fout.close();
            // System.out.println(f.getAbsolutePath() + " "+f.exists());
        } catch (UnsupportedRDFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RDFHandlerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //	prettyPrintTurtleFormat(f);
    }

    // print graph
    public void printGraph(Graph myGraph) {
        try {

            RDFWriter turtleWriter = Rio.createWriter(RDFFormat.TURTLE,
                    System.out);
            WriterConfig config = new WriterConfig();
            config.set(BasicWriterSettings.PRETTY_PRINT, true);
            turtleWriter.setWriterConfig(config);

            turtleWriter.startRDF();
            for (Statement statement : myGraph) {
                turtleWriter.handleStatement(statement);
            }
            turtleWriter.endRDF();
        } catch (UnsupportedRDFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RDFHandlerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // add action to the graph
    public Graph setAction(Namespaces ns, Graph myGraph, URI subject,
            String action) {
        myGraph.add(
                subject,
                ValueFactoryImpl.getInstance().createURI(
                        ns.getIRIForPrefix("eem"), "action"),
                ValueFactoryImpl.getInstance().createURI(
                        ns.getIRIForPrefix("eem"), action.toUpperCase()));
        return myGraph;
    }

    // add action to the graph
    public Graph setAction(Namespaces ns, Graph myGraph, URI subject,
            URI actionURI) {
        myGraph.add(
                subject,
                ValueFactoryImpl.getInstance().createURI(
                        ns.getIRIForPrefix("eem"), "action"), actionURI);
        return myGraph;
    }

    // add business step type to the graph
    public Graph setBusinessStepType(Namespaces ns, Graph myGraph, URI subject,
            String businessStep) {
        myGraph.add(
                subject,
                ValueFactoryImpl.getInstance().createURI(
                        ns.getIRIForPrefix("eem"), "hasBusinessStepType"),
                ValueFactoryImpl.getInstance().createURI(
                        ns.getIRIForPrefix("cbv"),
                        businessStep.toLowerCase()));
        return myGraph;
    }

    // add business step type to the graph
    public Graph setBusinessStepType(Namespaces ns, Graph myGraph, URI subject,
            URI businessStepURI) {
        myGraph.add(
                subject,
                ValueFactoryImpl.getInstance().createURI(
                        ns.getIRIForPrefix("eem"), "hasBusinessStepType"),
                businessStepURI);
        return myGraph;
    }

    // add temporal properties to the graph
    public Graph setTemporalProperties(Namespaces ns, Graph myGraph,
            URI mySubject) {
        // set up the temporal properties
        URI myPredicate;
        Literal myLiteral;

        GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
        gc.setTimeInMillis(System.currentTimeMillis());
        DatatypeFactory df;
        try {
            df = DatatypeFactory.newInstance();

            XMLGregorianCalendar xc = df.newXMLGregorianCalendar(gc);
            String ts = xc.toXMLFormat();
            myLiteral = ValueFactoryImpl.getInstance().createLiteral(
                    xc);
            myPredicate = ValueFactoryImpl.getInstance().createURI(
                    ns.getIRIForPrefix("eem"), "eventOccurredAt");
            myGraph.add(mySubject, myPredicate, myLiteral);

            myLiteral = ValueFactoryImpl.getInstance().createLiteral(
                    TimeUnit.MILLISECONDS.toDays(gc.getTimeZone().getOffset(
                                    gc.getTimeInMillis())));
            // set up the offset
            myPredicate = ValueFactoryImpl.getInstance().createURI(
                    ns.getIRIForPrefix("eem"), "eventTimeZoneOffset");
            myGraph.add(mySubject, myPredicate, myLiteral);
        } catch (DatatypeConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return myGraph;

    }

    /**
     * @param ns
     * @param myGraph
     * @param mySubject
     * @param transactions
     */
    public Graph setBusinessTransaction(Namespaces ns, Graph myGraph,
            URI mySubject, ArrayList<Transaction> transactions) {
        if (transactions.size() > 0) {
            ValueFactory myFactory = ValueFactoryImpl.getInstance();

            BNode bnode = myFactory.createBNode();
            // link the EPCS to the event
            myGraph.add(mySubject,
                    myFactory.createURI(ns.getIRIForPrefix("eem"),
                            "associatedWithTransactionList"), bnode);
            myGraph.add(bnode, RDF.TYPE, myFactory.createURI(
                    ns.getIRIForPrefix("eem"), "SetOfTransactions"));
            for (Transaction e : transactions) {
                URI transactionID = myFactory.createURI(e.getTransactionID());
                myGraph.add(transactionID, RDF.TYPE, myFactory.createURI(
                        ns.getIRIForPrefix("eem"), "Transaction"));
                myGraph.add(transactionID, myFactory.createURI(
                        ns.getIRIForPrefix("eem"), "hasTransactionType"),
                        myFactory.createURI(ns.getIRIForPrefix("cbv"), "btt/"
                                + e.getTransactionType()));
                myGraph.add(bnode,
                        myFactory.createURI("http://purl.org/co#element"),
                        transactionID);
                // System.out.println(e);
            }
        }
        return myGraph;

    }

    /**
     * @param ns
     * @param myGraph
     * @param mySubject
     * @param disposition
     * @return
     */
    // set disposition with default URI
    public Graph setDisposition(Namespaces ns, Graph myGraph, URI mySubject,
            String disposition) {

        myGraph.add(
                mySubject,
                ValueFactoryImpl.getInstance().createURI(
                        ns.getIRIForPrefix("eem"), "hasDisposition"),
                ValueFactoryImpl.getInstance().createURI(
                        ns.getIRIForPrefix("cbv"),
                        disposition.toLowerCase()));
        return myGraph;
    }

    // set disposition with custom URI
    public Graph setDisposition(Namespaces ns, Graph myGraph, URI mySubject,
            URI disposition) {

        myGraph.add(
                mySubject,
                ValueFactoryImpl.getInstance().createURI(
                        ns.getIRIForPrefix("eem"), "hasDisposition"),
                disposition);
        return myGraph;
    }

    public Graph setLocation(Namespaces ns, URI mySubject, Graph myGraph,
            Site location, String identifier) {
        URI myPredicate = null;
        ValueFactory myFactory = ValueFactoryImpl.getInstance();

        BNode bnode = myFactory.createBNode();
        URI locationIDURI = location.getLocationIDURI();
        Address address = location.getAddress();
        SubSite subsite = location.getSubSite();

        if (identifier.equals("readPoint")) {
            myPredicate = myFactory.createURI(ns.getIRIForPrefix("eem"),
                    "hasReadPointLocation");
            myGraph.add(locationIDURI, RDF.TYPE, myFactory.createURI(
                    ns.getIRIForPrefix("eem"), "ReadPointLocation"));
        } else {
            myPredicate = myFactory.createURI(ns.getIRIForPrefix("eem"),
                    "hasBusinessLocation");
            myGraph.add(locationIDURI, RDF.TYPE, myFactory.createURI(
                    ns.getIRIForPrefix("eem"), "BusinessLocation"));

        }

        myGraph.add(mySubject, myPredicate, locationIDURI);

        if (address != null) {
            // add address properties
            if (address.getStreetName() != null) {
                myGraph.add(bnode, myFactory.createURI(ns.getIRIForPrefix("vcard"),
                        "street-address"), myFactory.createLiteral(address
                                .getStreetName()));
            }

            if (address.getPostCode() != null) {
                myGraph.add(bnode, myFactory.createURI(ns.getIRIForPrefix("vcard"),
                        "postal-code"), myFactory.createLiteral(address
                                .getPostCode()));
            }

            if (address.getCountry() != null) {
                myGraph.add(bnode, myFactory.createURI(ns.getIRIForPrefix("vcard"),
                        "country-name"), myFactory.createLiteral(address
                                .getCountry()));
            }

            if (address.getLocality() != null) {
                myGraph.add(bnode, myFactory.createURI(ns.getIRIForPrefix("vcard"),
                        "locality"), myFactory.createLiteral(address.getLocality()));
            }

            if (address.getPost_office_box() != null) {
                myGraph.add(bnode, myFactory.createURI(ns.getIRIForPrefix("vcard"),
                        "post-ffice-box"), myFactory.createLiteral(address
                                .getPost_office_box()));
            }

            if (address.getRegion() != null) {
                myGraph.add(bnode,
                        myFactory.createURI(ns.getIRIForPrefix("vcard"), "region"),
                        myFactory.createLiteral(address.getRegion()));
            }

            myGraph.add(location.getLocationIDURI(),
                    myFactory.createURI(ns.getIRIForPrefix("vcard"), "adr"), bnode);
            myGraph.add(
                    location.getLocationIDURI(),
                    myFactory.createURI(ns.getIRIForPrefix("eem"), "hasLocationID"),
                    location.getLocationIDURI());
            myGraph.add(location.getLocationIDURI(),
                    myFactory.createURI(ns.getIRIForPrefix("wgs84"), "latitude"),
                    myFactory.createLiteral(address.getLatitude()));
            myGraph.add(location.getLocationIDURI(),
                    myFactory.createURI(ns.getIRIForPrefix("wgs84"), "longitude"),
                    myFactory.createLiteral(address.getLongitude()));

        }

        if (subsite != null) {
            bnode = myFactory.createBNode();

            //add the subSiteType statements
            myGraph.add(bnode, myFactory.createURI(ns.getIRIForPrefix("cbv"), "hasSubSiteType"),
                    myFactory.createLiteral(subsite.getSubSiteType().toString()));

            if (subsite.getSubsSiteAttribute() != null) {
                ArrayList<SubSiteAttribute> ssAttributes = subsite.getSubsSiteAttribute();
                for (SubSiteAttribute ssat : ssAttributes) {
                    myGraph.add(bnode, myFactory.createURI(ns.getIRIForPrefix("cbv"), "hasSubSiteAttribute"),
                            myFactory.createLiteral(ssat.toString()));
                }

            }
            if (subsite.getSubSiteDetail() != null) {
                myGraph.add(bnode, myFactory.createURI(ns.getIRIForPrefix("cbv"), "hasSubSiteDetail"),
                        myFactory.createLiteral(subsite.getSubSiteDetail()));
            }

            myGraph.add(location.getLocationIDURI(),
                    myFactory.createURI(ns.getIRIForPrefix("cbv"), "hasSubSite"),
                    bnode);

        }
        return myGraph;

    }

    public Graph setLocation(Namespaces ns, String prefix, Graph myGraph,
            URI mySubject, Site location, String identifier) {
        URI myPredicate = null;
        ValueFactory myFactory = ValueFactoryImpl.getInstance();
        ;
        // check the strings
        String locationID = location.getLocationID();
        URI locationIDURI = location.getLocationIDURI();

        if (locationIDURI == null) {
            locationIDURI = myFactory.createURI(ns.getIRIForPrefix(prefix),
                    locationID);
            location.setLocationIDURI(locationIDURI);
        }

        myGraph = setLocation(ns, mySubject, myGraph, location, identifier);
        return myGraph;

    }

    private void prettyPrintTurtleFormat(String fileName) {
		//Model model = Rio.parse(new FileInputStream(fileName), RDFFormat.TURTLE);
        //Rio.p
        // System.out.println("#### ---- Write as Turtle") ;
        // System.out.println() ;
        // RDFDataMgr.write(System.out, model, Lang.TURTLE) ;
	/*	try {
         TurtleWriter ttlWriter = new TurtleWriter(new FileOutputStream(new File(fileName)));
         ttlWriter.startRDF();
         for (Statement st: model) {
         ttlWriter.handleStatement(st);
         }
         ttlWriter.endRDF();
	
         System.out.println("#### ---- Write -------");
         System.out.println();
	
         } catch (FileNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         }*/
        // System.out.println() ;
        // System.out.println("#### ---- Write as Turtle via model.write") ;
        // System.out.println() ;
        // model.write(System.out, "TTL") ;

    }

    /**
     * @param myGraph
     * @param mySubject2
     * @param reader
     * @return
     */
    public Graph setReaderForEvent(Namespaces ns, String prefix, Graph myGraph,
            URI mySubject, Reader reader) {

        URI readerURI = ValueFactoryImpl.getInstance().createURI(
                ns.getIRIForPrefix(prefix), reader.getPhysicalReaderID());

        myGraph.add(readerURI, RDF.TYPE, ValueFactoryImpl.getInstance()
                .createURI(ns.getIRIForPrefix("eem"), "Reader"));
        myGraph.add(
                readerURI,
                ValueFactoryImpl.getInstance().createURI(
                        ns.getIRIForPrefix("eem"), "logicalID"),
                ValueFactoryImpl.getInstance().createLiteral(
                        reader.getPhysicalReaderID()));

        myGraph.add(
                mySubject,
                ValueFactoryImpl.getInstance().createURI(
                        ns.getIRIForPrefix("eem"), "recordedByReader"),
                readerURI);

        return myGraph;
    }

    /**
     * @param recordTime
     * @return
     */
    public Graph setEventRecordedTime(Namespaces ns, Graph myGraph,
            URI subject, Literal recordTime) {

     //   System.out.println(subject);
        myGraph.add(
                subject,
                ValueFactoryImpl.getInstance().createURI(
                        ns.getIRIForPrefix("eem"), "eventRecordedAt"),
                recordTime);

        return myGraph;
    }

    public Literal getCurrentTimeAndDateInXMLGregorianCalendar() {
        GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
        gc.setTimeInMillis(System.currentTimeMillis());
        DatatypeFactory df = null;
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException ex) {
            java.util.logging.Logger.getLogger(EPCISCommon.class.getName()).log(Level.SEVERE, null, ex);
        }

        XMLGregorianCalendar xc = df.newXMLGregorianCalendar(gc);

        Literal myLiteral = ValueFactoryImpl.getInstance().createLiteral(
                xc);

        return myLiteral;
    }

    Graph streamGraph = new LinkedHashModel();

    private void addToStream(Namespaces ns) {

        setEventRecordedTime(ns, streamGraph, ValueFactoryImpl.getInstance().createURI(ns.getContextURI()), getCurrentTimeAndDateInXMLGregorianCalendar());
        persistStreamGraph(streamGraph, ns);

    }

    void persistStreamGraph(Graph streamGraph, Namespaces ns) {

        File fstream = new File("streams");
        try {
            if (!fstream.exists()) {
                fstream.mkdir();
                fstream.createNewFile();
            } else {
                logger.info("file exists");
            }
            FileOutputStream fout = new FileOutputStream("streams//streamGraph.trig", true);

//           TurtleWriter turtleWriter = new TurtleWriter(fout);
//            WriterConfig config = new WriterConfig();
//            config.set(BasicWriterSettings.PRETTY_PRINT, true);
//
//            turtleWriter.setWriterConfig(config);
//            turtleWriter.startRDF();
//            for (Statement statement : streamGraph) {
//                // System.out.println(statement);
//                turtleWriter.handleStatement(statement);
//            }
//            turtleWriter.endRDF();
//            fout.close();
            ValueFactory myFactory = ValueFactoryImpl.getInstance();
            Model result = new LinkedHashModel();
            ContextStatementCollector csc = new ContextStatementCollector(result, myFactory, myFactory.createURI(ns.getBaseIRI().concat("eventStream")));

//              Map<String, String> namespaces = ns.getNameSpacesPrefixes();
//            for (Map.Entry<String, String> entry : namespaces.entrySet()) {
//                String key = entry.getKey();
//                String value = entry.getValue();
//                //use key and value
//                logger.info(key + "   " + value);
//                //  turtleWriter.handleNamespace(key, value);
//                csc.handleNamespace(key, value);
//
//            }
            Rio.write(streamGraph, csc);

            //handle namespaces
            Rio.write(result, fout, RDFFormat.TURTLE);

            fout.close();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(EPCISCommon.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RDFHandlerException ex) {
            java.util.logging.Logger.getLogger(EPCISCommon.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
