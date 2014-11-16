/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.purl.linkedepcis.pedigree;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sparql.SPARQLRepository;
import org.purl.linkedepcis.eem.Namespaces;
import org.purl.linkedepcis.examples.QueryEventStream;

/**
 *
 * @author monika
 */
public class LinkedPedigreeGenerator {

    String sparqlEndpoint = null;
    PedigreeStatus status;
    Namespaces ns  = null;

    public LinkedPedigreeGenerator(String sparqlEndpoint, PedigreeStatus status, Namespaces ns) {
        this.status = status;
        this.sparqlEndpoint = sparqlEndpoint;
        this.ns=ns;

    }

  

   
   private void getPedigreeMap() {
        try {
            SPARQLRepository sRep = new SPARQLRepository(sparqlEndpoint);
            sRep.initialize();
            RepositoryConnection con = sRep.getConnection();
            
              String query = "PREFIX eem:<http://purl.org/eem#>\n"
                + "SELECT DISTINCT ?obj ?agg ?shp WHERE\n"
                + "{\n"
                + " ?obj a eem:ObjectEvent;\n"
                + "    eem:hasBusinessStepType ?x;\n"
                + "    eem:associatedWithEPCList ?y.\n"
                + "    ?y  <http://purl.org/co#element> ?epc1.\n"
                + "    {\n"
                + "        ?agg a eem:AggregationEvent;\n"
                + "        eem:hasAggregationURI ?au;\n"
                + "        eem:hasBusinessStepType ?x1;\n"
                + "        eem:associatedWithEPCList ?y1.\n"
                + "        ?y1  <http://purl.org/co#element> ?epc1.\n"
                + "        FILTER( contains(str(?x1), \"packing\")) \n"
                + "        {\n"
                + "         ?shp a eem:ObjectEvent;\n"
                + "         eem:hasBusinessStepType ?x2;\n"
                + "         eem:associatedWithEPCList ?z1.\n"
                + "         ?z1  <http://purl.org/co#element> ?au.\n"
                + "         FILTER( contains(str(?x2), \"shipping\")) \n"
                + "        }\n"
                + "    }\n"
                + "   FILTER (contains(str(?x), \"commissioning\")) \n"
                + "}";

            TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, query);

            TupleQueryResult result = tupleQuery.evaluate();
            HashMap<URI, HashSet<URI>> pedigreeMap = new HashMap<URI, HashSet<URI>>();
         
            while (result.hasNext()) {
                //     System.out.println("********");
                BindingSet bindingSet = result.next();
                Value object = bindingSet.getValue("obj");
                Value aggregate = bindingSet.getValue("agg");
                //   System.out.println("object.."+object.toString());
                // System.out.println("aggregation.."+aggregate.toString());
                Value shipping = bindingSet.getValue("shp");
                // System.out.println("shipping.."+shipping.toString());
                ValueFactory vf = ValueFactoryImpl.getInstance();
                URI tempURI = null;
                tempURI = vf.createURI(shipping.toString());
                if (pedigreeMap.containsKey(tempURI)) {
                    pedigreeMap.get(tempURI).add(vf.createURI(object.toString()));
                    pedigreeMap.get(tempURI).add(vf.createURI(aggregate.toString()));
                } else {
                    HashSet<URI> tempArray = new HashSet<URI>();
                    tempArray.add(vf.createURI(object.toString()));
                    tempArray.add(vf.createURI(aggregate.toString()));
                    pedigreeMap.put(tempURI, tempArray);
                }
//
            }
            
              generatePedigree(pedigreeMap);
            con.close();
        } catch (RepositoryException ex) {
            Logger.getLogger(LinkedPedigreeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedQueryException ex) {
            Logger.getLogger(LinkedPedigreeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(LinkedPedigreeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    public Pedigree generatePedigree(HashMap<URI, HashSet<URI>> pedigreeMap) {
        Pedigree pedigreePharma = null;

      

        int counter = 0;
        try {

            for (Map.Entry<URI, HashSet<URI>> entry : pedigreeMap.entrySet()) {
                URI key = entry.getKey();
         
                pedigreePharma = new Pedigree(new java.net.URI(ns.getIRIForPrefix("cpd")), "pharamCo1Pedigree" + counter);
                pedigreePharma.setPedigreeStatus(status);
                //    pedigreePharma.setReceiverPedigree(new java.net.URI("http://www.gsk.com/data/pedigrees/gskPedigree345"));
                //  pedigreePharma.setReceiverPedigree(new java.net.URI("http://www.gsk.com/data/pedigrees/gskPedigree123"));
                pedigreePharma.setPedigreeCreatorURI(new java.net.URI("http://fispace.aston.ac.uk/pharmaCo1/foaf/"));
                pedigreePharma.setTransactionInfoURI(new java.net.URI(key.toString()));
                //   System.out.println("shipping " + key);
                HashSet<URI> value = entry.getValue();
                for (URI u : value) {
                    //       System.out.println("events " + u);
                    pedigreePharma.setConsignmentInfoURI(new java.net.URI(u.toString()));
                }
                pedigreePharma.setProductMasterDataURI(new java.net.URI("http://fispace.aston.ac.uk/data/products/product123"));
                //     System.out.println("*****************************");
                pedigreePharma.createPedigree("pedigrees/pharma_pedigree" +  "_" + counter + ".ttl", ns);
                counter++;
            }

        } catch (URISyntaxException ex) {
            Logger.getLogger(QueryEventStream.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

   
}
