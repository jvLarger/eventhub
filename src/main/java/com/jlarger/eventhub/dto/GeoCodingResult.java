package com.jlarger.eventhub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoCodingResult {
	
	@JsonProperty(value = "formatted_address")
	private String formattedAddress;

	@JsonProperty("place_id")
	private String placeId;

	@JsonProperty("partial_match")
	private String partialMatch;
	
	@JsonProperty("geometry")
	private GeoCodingGeometry geometry;

	public String getFormattedAddress() {
		return formattedAddress;
	}

	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getPartialMatch() {
		return partialMatch;
	}

	public void setPartialMatch(String partialMatch) {
		this.partialMatch = partialMatch;
	}

	public GeoCodingGeometry getGeometry() {
		return geometry;
	}

	public void setGeometry(GeoCodingGeometry geometry) {
		this.geometry = geometry;
	}

	@Override
	public String toString() {
		return "GeoCodingResult [formattedAddress=" + formattedAddress + ", placeId=" + placeId + ", partialMatch="
				+ partialMatch + ", geometry=" + geometry + "]";
	}
	
}