/**
 * 
 */
package org.purl.linkedepcis.cbv;

import org.purl.linkedepcis.cbv.Address;
import org.openrdf.model.URI;



/**
 * @author monika
 *
 */
public abstract class Location {
	
	protected URI locationIDURI;
	protected String locationID;
	protected Address address;
	/**
	 * @return the locationIDURI
	 */
	public URI getLocationIDURI() {
		return locationIDURI;
	}
	/**
	 * @param locationIDURI
	 * @param address
	 */
	protected Location(URI locationIDURI,  Address address) {
		super();
		this.locationIDURI = locationIDURI;
		this.address = address;
	}
	/**
	 * @param locationID
	 * @param address
	 */
	protected Location(String locationID, Address address) {
		super();
		this.locationID = locationID;
		this.address = address;
	}
	/**
	 * @param locationIDURI
	 */
	protected Location(URI locationIDURI) {
		super();
		this.locationIDURI = locationIDURI;
	}
	/**
	 * @param locationID
	 */
	protected Location(String locationID) {
		super();
		this.locationID = locationID;
	}
	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}
	/**
	 * @param locationIDURI the locationIDURI to set
	 */
	public void setLocationIDURI(URI locationIDURI) {
		this.locationIDURI = locationIDURI;
	}
	/**
	 * @return the locationID
	 */
	public String getLocationID() {
		return locationID;
	}
	/**
	 * @param locationID the locationID to set
	 */
	public void setLocationID(String locationID) {
		this.locationID = locationID;
	}

}
