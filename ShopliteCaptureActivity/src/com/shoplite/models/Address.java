/**
 * 
 */
package com.shoplite.models;

import java.io.Serializable;

import com.google.gson.Gson;


/**
 * @author I300291
 *
 */
public class Address implements Serializable{
	
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
	public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
 
    static public Address create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, Address.class);
    }
	
	

}
