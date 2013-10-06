/**
 * 
 */
package org.purl.linkedepcis.cbv;

import java.net.URI;
import java.util.ArrayList;

/**
 * @author monika
 * 
 */
public class SubSite {

	private SubSiteType subSiteType;
	private ArrayList<SubSiteAttribute> subsSiteAttributes;
	private String subSiteDetail;

	/**
	 * 
	 * @param subSiteType
	 */
	public SubSite(SubSiteType subSiteType) {

		this.subSiteType = subSiteType;

	}

	/**
	 * @param subSiteType
	 * @param subsSiteAttributes
	 * @param subSiteDetail
	 */
	public SubSite(SubSiteType subSiteType,
			ArrayList<SubSiteAttribute> subsSiteAttributes, String subSiteDetail) {
		super();
		this.subSiteType = subSiteType;
		this.subsSiteAttributes = subsSiteAttributes;
		this.subSiteDetail = subSiteDetail;

	}

	/**
	 * @return the subSiteType
	 */
	public SubSiteType getSubSiteType() {
		return subSiteType;
	}

	/**
	 * @return the subsSiteAttribute
	 */
	public ArrayList<SubSiteAttribute> getSubsSiteAttribute() {
		return subsSiteAttributes;
	}

	/**
	 * @param subsSiteAttribute
	 *            the subsSiteAttribute to set
	 */
	public void setSubsSiteAttribute(
			ArrayList<SubSiteAttribute> subsSiteAttributes) {
		this.subsSiteAttributes = subsSiteAttributes;
	}

	/**
	 * @return the subSiteDetail
	 */
	public String getSubSiteDetail() {
		return subSiteDetail;
	}

	/**
	 * @param subSiteDetail
	 *            the subSiteDetail to set
	 */
	public void setSubSiteDetail(String subSiteDetail) {
		this.subSiteDetail = subSiteDetail;
	}

}
