/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.purl.linkedepcis.pedigree;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.openrdf.model.Literal;
import org.openrdf.model.Model;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.vocabulary.RDF;
import org.purl.linkedepcis.eem.EPCISCommon;
import org.purl.linkedepcis.eem.Namespaces;

/**
 *
 * @author monika
 */
public class Pedigree {

    private URI pedigreeNamespace;
    private String pedigreeID;
    private PedigreeStatus pedigreeStatus;
    private URI pedigreeCreatorURI;
    private URI productMasterDataURI;
    private URI consignmentInfoURI;
    private URI transactionInfoURI;
    private URI receiverPedigree;
    private URI pedigreeURI;
    private Model pedigreeGraph;
    private Namespaces ns;
    private ValueFactoryImpl myFactory;
    org.openrdf.model.URI mySubject = null;
    org.openrdf.model.URI myPredicate = null;
    org.openrdf.model.URI myObject = null;

    Literal myObjectLiteral = null;

    public Pedigree(URI pedigreeNamespace, String pedigreeID) {
        try {
            this.pedigreeNamespace = pedigreeNamespace;
            this.pedigreeID = pedigreeID;
            ns = new Namespaces();
            pedigreeGraph = new org.openrdf.model.impl.LinkedHashModel();
            myFactory = ValueFactoryImpl.getInstance();

            mySubject = myFactory.createURI(pedigreeNamespace.toString() + pedigreeID);
            pedigreeURI = new URI(mySubject.toString());
            myObject = myFactory.createURI(ns.getIRIForPrefix("ped"), "Pedigree");
            pedigreeGraph.add(mySubject, RDF.TYPE, myObject);

            myPredicate = myFactory.createURI(ns.getIRIForPrefix("ped"), "hasSerialNumber");
            myObjectLiteral = myFactory.createLiteral(pedigreeID);
            pedigreeGraph.add(mySubject, myPredicate, myObjectLiteral);

            GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
            gc.setTimeInMillis(System.currentTimeMillis());
            DatatypeFactory df;
            
                df = DatatypeFactory.newInstance();

                XMLGregorianCalendar xc = df.newXMLGregorianCalendar(gc);
                String ts = xc.toXMLFormat();
                myObjectLiteral = ValueFactoryImpl.getInstance().createLiteral(
                        xc);
                myPredicate = ValueFactoryImpl.getInstance().createURI(
                        ns.getIRIForPrefix("ped"), "pedigreeCreationTime");
                pedigreeGraph.add(mySubject, myPredicate, myObjectLiteral);

        }  catch (URISyntaxException ex) {
                Logger.getLogger(Pedigree.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(Pedigree.class.getName()).log(Level.SEVERE, null, ex);
        }

        }

    

    public URI getPedigreeURI() {
        return pedigreeURI;
    }

    /**
     * @return the pedigreeID
     */
    public String getPedigreeID() {
        return pedigreeID;
    }

    /**
     * @return the pedigreeStatus
     */
    public PedigreeStatus getPedigreeStatus() {
        return pedigreeStatus;
    }

    /**
     * @param pedigreeStatus the pedigreeStatus to set
     */
    public void setPedigreeStatus(PedigreeStatus pedigreeStatus) {

        //add pedigree status
        if (pedigreeStatus != null) {
            this.pedigreeStatus = pedigreeStatus;
            myPredicate = myFactory.createURI(ns.getIRIForPrefix("ped"), "hasPedigreeStatus");
            myObjectLiteral = myFactory.createLiteral(pedigreeStatus.toString());
            pedigreeGraph.add(mySubject, myPredicate, myObjectLiteral);

        }
    }

    /**
     * @return the pedigreeCreator
     */
    public URI getPedigreeCreatorURI() {
        return pedigreeCreatorURI;
    }

    /**
     * @param pedigreeCreator the pedigreeCreator to set
     */
    public void setPedigreeCreatorURI(URI pedigreeCreator) {
        this.pedigreeCreatorURI = pedigreeCreator;
        //add creator URI
        if (pedigreeCreatorURI != null) {
            myPredicate = myFactory.createURI(ns.getIRIForPrefix("ped"), "pedigreeCreator");
            myObject = myFactory.createURI(pedigreeCreatorURI.toString());
            pedigreeGraph.add(mySubject, myPredicate, myObject);
        }
    }

    /**
     * @return the productMasterData
     */
    public URI getProductMasterDataURI() {
        return productMasterDataURI;
    }

    /**
     * @param productMasterData the productMasterData to set
     */
    public void setProductMasterDataURI(URI productMasterData) {
        this.productMasterDataURI = productMasterData;
        //add product master data URI

        if (productMasterDataURI != null) {
            myPredicate = myFactory.createURI(ns.getIRIForPrefix("ped"), "hasProductInfo");
            myObject = myFactory.createURI(productMasterDataURI.toString());
            pedigreeGraph.add(mySubject, myPredicate, myObject);
        }
    }

    /**
     * @return the consignmentInfo
     */
    public URI getConsignmentInfoURI() {
        return consignmentInfoURI;
    }

    /**
     * @param consignmentInfo the consignmentInfo to set
     */
    public void setConsignmentInfoURI(URI consignmentInfo) {
        this.consignmentInfoURI = consignmentInfo;
        //add consignment info
        if (consignmentInfoURI != null) {
            myPredicate = myFactory.createURI(ns.getIRIForPrefix("ped"), "hasConsignmentInfo");
            myObject = myFactory.createURI(consignmentInfoURI.toString());
            pedigreeGraph.add(mySubject, myPredicate, myObject);
        }
    }

    /**
     * @return the transactionInfo
     */
    public URI getTransactionInfoURI() {
        return transactionInfoURI;
    }

    /**
     * @param transactionInfo the transactionInfo to set
     */
    public void setTransactionInfoURI(URI transactionInfo) {
        this.transactionInfoURI = transactionInfo;
        // add transaction info
        if (transactionInfoURI != null) {
            myPredicate = myFactory.createURI(ns.getIRIForPrefix("ped"), "hasTransactionInfo");
            myObject = myFactory.createURI(transactionInfoURI.toString());
            pedigreeGraph.add(mySubject, myPredicate, myObject);
        }
    }

    public void createPedigree(String filename, Namespaces ns) {

        this.ns = ns;
        EPCISCommon epcc = new EPCISCommon();
        epcc.persistGraphToFile(pedigreeGraph, filename, ns);

    }

    /**
     * @return the receiverPedigree
     */
    public URI getReceiverPedigree() {
        return receiverPedigree;
    }

    /**
     * @param receiverPedigree the receiverPedigree to set
     */
    public void setReceiverPedigree(URI receiverPedigree) {
        this.receiverPedigree = receiverPedigree;
        System.out.println(pedigreeStatus);
        if (!(pedigreeStatus.toString().contains(PedigreeStatus.INITIAL.toString()))) {

            myPredicate = myFactory.createURI(ns.getIRIForPrefix("ped"), "hasReceiverPedigree");
            myObject = myFactory.createURI(receiverPedigree.toString());
            pedigreeGraph.add(mySubject, myPredicate, myObject);
        }
    }
}
