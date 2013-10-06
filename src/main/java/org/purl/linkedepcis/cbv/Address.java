/**
 * 
 */
package org.purl.linkedepcis.cbv;

/**
 * @author monika
 *
 */
public class Address {
	
	
	private String streetName;
	private String postCode;
	private String country;
	private String locality;
	private String post_office_box;
	private String region;
	private double latitude;
	private double longitude;
	/**
	 * @param streetName
	 * @param postCode
	 * @param country
	 * @param locality
	 * @param post_office_box
	 * @param region
	 * @param latitude
	 * @param longitude
	 */
	public Address(String streetName, String postCode, String country,
			String locality, String post_office_box, String region,
			double latitude, double longitude) {
		super();
		this.streetName = streetName;
		this.postCode = postCode;
		this.country = country;
		this.locality = locality;
		this.post_office_box = post_office_box;
		this.region = region;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the streetName
	 */
	public String getStreetName() {
		return streetName;
	}
	/**
	 * @param streetName the streetName to set
	 */
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	/**
	 * @return the postCode
	 */
	public String getPostCode() {
		return postCode;
	}
	/**
	 * @param postCode the postCode to set
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the locality
	 */
	public String getLocality() {
		return locality;
	}
	/**
	 * @param locality the locality to set
	 */
	public void setLocality(String locality) {
		this.locality = locality;
	}
	/**
	 * @return the post_office_box
	 */
	public String getPost_office_box() {
		return post_office_box;
	}
	/**
	 * @param post_office_box the post_office_box to set
	 */
	public void setPost_office_box(String post_office_box) {
		this.post_office_box = post_office_box;
	}
	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	
	
	
	public Address()
	{
		
	}

}
