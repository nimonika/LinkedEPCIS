/**
 * 
 */
package org.purl.linkedepcis.cbv;

/**
 * @author monika
 * 
 */
public enum SubSiteAttribute {
	BOX_CRUSHER (413), COLD_STORAGE (402), CONVEYOR_BELT (415), ELECTRONICS (401), SHELF (403), FROZEN (404), FRESH (405), 
	PRONOTION (406), END_CAP (407), POINT_OF_SALE (408);

	private int code;

	/**
	 * private constructor
	 */
	private SubSiteAttribute(int code) {
		this.code = code;
	}

	public int code() {
		return code;
	}
}
