/**
 * 
 */
package org.purl.linkedepcis.eem;

import org.openrdf.model.URI;

/**
 * @author monika
 *
 */
public class EPC {

private String epcValue;


	/**
	 * takes as input the EPC value
	 */
	public EPC(String epcValue) {
	
		this.epcValue=epcValue;
	}
	public String getEPCValue() {
		return epcValue;
	}
	public void setEPCValue(String epcValue) {
		this.epcValue = epcValue;
	}


	

}
