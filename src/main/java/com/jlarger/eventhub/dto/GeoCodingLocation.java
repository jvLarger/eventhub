package com.jlarger.eventhub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoCodingLocation {
	
	@JsonProperty("lat")
	private Double lat;
	
	@JsonProperty("lng")
	private Double lng;

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	@Override
	public String toString() {
		return "GeoCodingLocation [lat=" + lat + ", lng=" + lng + "]";
	}
	
}