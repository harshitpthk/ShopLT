package com.homelybuysapp.models;

import com.homelybuysapp.Utils.Constants;

public class OrderDetail {

	Constants.ORDERStatus status;
	String address;
	double latitude;
	double longitude;
	double amount;
	String refNumber;
	
	public Constants.ORDERStatus getState() {
		return status;
	}

	public void setState(Constants.ORDERStatus state) {
		this.status = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

}
