package com.jlarger.eventhub.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.jlarger.eventhub.dto.GeoCoding;

@Service
public class GeolocalizacaoService {

	@Value("${geocoding.key}")
	private String GEOCODING_KEY;
	
	private static final Logger log = LoggerFactory.getLogger(GeolocalizacaoService.class);
	private static final String GEOCODING_URI = "https://maps.googleapis.com/maps/api/geocode/json";
	
	public GeoCoding buscarGeolocalizacao(String address) {
		
		RestTemplate restTemplate = new RestTemplate();
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(GEOCODING_URI).queryParam("address", address).queryParam("key", GEOCODING_KEY);
			
		log.info("Calling geocoding api with: " + builder.toUriString());
		
		GeoCoding geoCoding = restTemplate.getForObject(builder.toUriString(), GeoCoding.class);
		
		log.info(geoCoding.toString());

		return geoCoding;
	}
	
}