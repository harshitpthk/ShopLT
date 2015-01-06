/**
 * 
 */
package com.shoplite.models;


/**
 * @author I300291
 *
 */
public class Address {
	
	private Location deliveryLocation;
	private String addressString;
	
	public Location getDeliveryLocation() {
		return deliveryLocation;
	}
	public void setDeliveryLocation(Location deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
	}
	public String getAddressString() {
		return addressString;
	}
	public void setAddressString(String addressString) {
		this.addressString = addressString;
	}
	
	

}
