/**
 * 
 */
package org.purl.linkedepcis.eem;

import org.openrdf.model.URI;

/**
 * @author monika
 *
 */
public class QuantityEvent extends EPCISEvent{
	
	private String epcClass;
	private String quantity;
	
	public QuantityEvent(Namespaces ns, String prefix, String epcClassPrefix, String epcClass,int quantity)
	{
	 super(ns, prefix, epcClassPrefix, epcClass, quantity, "QuantityEvent");	
	}
	
	public QuantityEvent(Namespaces ns, String prefix, URI epcClass,String quantity)
	{
		
	}

}
