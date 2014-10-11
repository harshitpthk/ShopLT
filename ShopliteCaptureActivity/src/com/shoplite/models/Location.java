package com.shoplite.models;

public class Location {
	
	private Double longitude;
	private Double latitude;

	public Location(Double latitude,Double longitude)
	{
		super();
		this.longitude = longitude;
		this.latitude = latitude;
	}
	public void setLongitude (Double longitude)
	{
		this.longitude = longitude;
	}
	
	public void setLatitude (Double latitude)
	{
		this.latitude = latitude;
	}
	
	public Double getLongitude()
	{
		return this.longitude;
	}
	
	public Double getLatitude(){
		return this.latitude;
	}
	public boolean equals(Location loc){
		if(loc.getLatitude() == this.latitude && loc.getLongitude() == this.longitude)
			return true;
		else
			return false;
		
	}
}
