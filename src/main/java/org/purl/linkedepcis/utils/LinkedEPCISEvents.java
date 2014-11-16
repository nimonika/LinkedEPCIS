/**
 * 
 */
package org.purl.linkedepcis.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.openrdf.model.Graph;
import org.openrdf.model.URI;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.rio.RDFFormat;

//import solanki.uk.epcis.testLibrary.GenerateEvent;


/**
 * @author monika
 *
 */
public class LinkedEPCISEvents {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		LinkedEPCISEvents lee = new LinkedEPCISEvents();
		lee.dumpFilesInRepository();
			
	}

	public void dumpFilesInRepository()
	{
		CommonFunctions cf = new CommonFunctions();
		String defaultNS = "http://fispace.aston.ac.uk/linkedEPCIS/franzfarmer/data/events/";
		URI context = ValueFactoryImpl.getInstance().createURI("http://fispace.aston.ac.uk/linkedEPCIS/franzfarmer/data/events");
		InputStream config= LinkedEPCISEvents.class.getClassLoader().getResourceAsStream("conf/linkedEpcis.ttl");
     
                  String url = "http://windermere.aston.ac.uk/openrdf-sesame/";;
		RepositoryConnection con =cf.setUpSesameRepository(config, url, defaultNS, false);
		cf.dumpFileInRepository(new File("franz_tomato_punnets_commissioning.ttl"), defaultNS, RDFFormat.TURTLE, context);
		
		 config= LinkedEPCISEvents.class.getClassLoader().getResourceAsStream("conf/linkedEpcis.ttl");
	     
		con =cf.setUpSesameRepository(config, url, defaultNS, false);
		cf.dumpFileInRepository(new File("franz_age1.ttl"), defaultNS, RDFFormat.TURTLE, context);
		
		 config= LinkedEPCISEvents.class.getClassLoader().getResourceAsStream("conf/linkedEpcis.ttl");
	     
		con =cf.setUpSesameRepository(config, url, defaultNS, false);
		cf.dumpFileInRepository(new File("franz_age2.ttl"), defaultNS, RDFFormat.TURTLE, context);
			
		config= LinkedEPCISEvents.class.getClassLoader().getResourceAsStream("conf/linkedEpcis.ttl");
	     
		con =cf.setUpSesameRepository(config, url,  defaultNS, false);
		cf.dumpFileInRepository(new File("franz_quantity.ttl"), defaultNS, RDFFormat.TURTLE, context);
		
		
		config= LinkedEPCISEvents.class.getClassLoader().getResourceAsStream("conf/linkedEpcis.ttl");
	     
		con =cf.setUpSesameRepository(config, url, defaultNS, false);
		cf.dumpFileInRepository(new File("franz_trans.ttl"), defaultNS, RDFFormat.TURTLE, context);
		
		
	}
	/**
	 * @param con
	 * @param defaultNS 
	 * @param config 
	 */
	private static void dumpEventGraph(InputStream config, String defaultNS) {
//		String context = "http://windermere.aston.ac.uk/linkedEPCIS/company1/data/events";
//		GenerateEvent ge = new GenerateEvent();
//		Graph myGraph =ge.getObjectEventGraph();
//		CommonFunctions cf = new CommonFunctions();
//        
//		RepositoryConnection con =cf.setUpSesameRepository(config, defaultNS, true);
//		
//		cf.dumpdataInRepository(con, myGraph, context, true);
//		System.out.println("hello..1...");
//		myGraph=ge.getAggregateEventGraph();
//		System.out.println("hello...2.." +myGraph);
//		config= LinkedEPCISEvents.class.getClassLoader().getResourceAsStream("conf/linkedEpcis.ttl");
//		con =cf.setUpSesameRepository(config, defaultNS, false);
//		System.out.println("hello....3.");
//		cf.dumpdataInRepository(con, myGraph, context, true);
	}

}
