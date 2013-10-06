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
public class Site extends Location {

	private SubSite subSite;

	/**
	 * @param locationIDURI
	 * @param locationID
	 * @param address
	 * @param subSite
	 */
	public Site(URI locationIDURI, SubSite subSite, Address address) {
		super(locationIDURI, address);
		this.subSite = subSite;

	}
	
	/**
	 * @param locationID
	 * @param address
	 * @param subSite
	 */
	public Site(String locationID, SubSite subSite, Address address) {
		super(locationID, address);
		this.subSite = subSite;
	}

	/**
	 * @param locationID
	 * @param subSite
	 */
	public Site(String locationID, SubSite subSite) {
		super(locationID);
		this.subSite = subSite;
	}

	/**
	 * @param locationIDURI
	 * @param subSite
	 */
	public Site(URI locationIDURI, SubSite subSite) {
		super(locationIDURI);
		this.subSite = subSite;
	}

	/**
	 * @param locationID
	 */
	public Site(String locationID) {
		super(locationID);

	}

	/**
	 * @param locationIDURI
	 */
	public Site(URI locationIDURI) {
		super(locationIDURI);

	}


	/**
	 * @return the subSite
	 */
	public SubSite getSubSite() {
		return subSite;
	}

	/**
	 * @param subSite the subSite to set
	 */
	public void setSubSite(SubSite subSite) {
		this.subSite = subSite;
	}

	/**
	 * @param locationID
	 * @param address
	 */
	public Site(String locationID, Address address) {
		super(locationID, address);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param locationIDURI
	 * @param address
	 */
	public Site(URI locationIDURI, Address address) {
		super(locationIDURI, address);
		// TODO Auto-generated constructor stub
	}

	
}
