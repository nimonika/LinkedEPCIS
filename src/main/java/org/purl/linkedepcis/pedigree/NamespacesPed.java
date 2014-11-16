/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.purl.linkedepcis.pedigree;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author monika
 */
public class NamespacesPed {

    private Map<String, String> namespaces = null;

    /**
     * @return the namespaces
     */
    public Map<String, String> getNamespaces() {
        return namespaces;
    }

    public void setIRIandPREFIX(String prefix, String IRI) {
        namespaces.put(prefix, IRI);
    }

    public NamespacesPed() {
        namespaces = new HashMap<String, String>();
        setIRIandPREFIX("ped", "http://www.fispace.aston.ac.uk/ontologies/pedigree#");
        setIRIandPREFIX("dc", "http://purl.org/dc/elements/1.1/");
    }

    //get the event IRI for  a specific prefix
    public String getIRIForPrefix(String prefix) {
        return namespaces.get(prefix);
    }

}
