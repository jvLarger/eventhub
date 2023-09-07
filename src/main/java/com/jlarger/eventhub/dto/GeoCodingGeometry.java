package com.jlarger.eventhub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoCodingGeometry {
	
	@JsonProperty("location")
	private GeoCodingLocation location;

	public GeoCodingLocation getLocation() {
		return location;
	}

	public void setLocation(GeoCodingLocation location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "GeoCodingGeometry [location=" + location + "]";
	}
	
}