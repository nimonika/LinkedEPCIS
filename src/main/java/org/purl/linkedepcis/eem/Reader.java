/**
 * 
 */
package org.purl.linkedepcis.eem;

import org.openrdf.model.URI;

/**
 * @author monika
 *
 */
public class Reader {
	
	private String physicalReaderID;
	private String logicalReaderID;
	
	
	/**
	 * @param physicalReaderID
	 * @param logicalReaderID
	 */
	public Reader(Namespaces ns, String prefix, String physicalReaderID, String logicalReaderID) {
		super();
		this.physicalReaderID = physicalReaderID;
		this.logicalReaderID = logicalReaderID;
		
	}
	
	
	/**
	 * @return the physicalReaderID
	 */
	public String getPhysicalReaderID() {
		return physicalReaderID;
	}
	/**
	 * @param physicalReaderID the physicalReaderID to set
	 */
	public void setPhysicalReaderID(String physicalReaderID) {
		this.physicalReaderID = physicalReaderID;
	}
	/**
	 * @return the logicalReaderID
	 */
	public String getLogicalReaderID() {
		return logicalReaderID;
	}
	/**
	 * @param logicalReaderID the logicalReaderID to set
	 */
	public void setLogicalReaderID(String logicalReaderID) {
		this.logicalReaderID = logicalReaderID;
	}
	
	
	

}
