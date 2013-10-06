/**
 * 
 */
package org.purl.linkedepcis.cbv;

/**
 * @author monika
 *
 */

public enum SubSiteType {
	BACK_ROOM (201), CARGO_TERMINAL (215), CONTAINER_DECK (214), CUSTOMER_PICK_UP_AREA (212),
	PACKAGING_AREA (251), PHARMACY_AREA (253), PICKING_AREA (252), PRODUCTION_AREA (208) , RECEIVING_AREA (209), RETURNS_AREA (207), 
	SALES_FLOOR (203), SALES_FLOOR_TRANSITION_AREA (211), SHIPPING_AREA (210), STORAGE_AREA (202), UNDEFINED (299), YARD (213);
	
	private int code;
	SubSiteType(int code)
	{
		this.code=code;
	}
	
	public int code()
	{
		return code;
	}
	
}
