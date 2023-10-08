package com.jlarger.eventhub.services;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jlarger.eventhub.dto.GeoCoding;

@Service
public class GeolocalizacaoService {

	@Value("${geocoding.key}")
	private String GEOCODING_KEY;
	
	private static final Logger log = LoggerFactory.getLogger(GeolocalizacaoService.class);
	private static final String GEOCODING_URI = "https://maps.googleapis.com/maps/api/geocode/json";
	
	public GeoCoding buscarGeolocalizacao(String address) {
		
		RestTemplate restTemplate = new RestTemplate();
	
		String googleurl = StringUtils.join(GEOCODING_URI,"?key=", GEOCODING_KEY, "&", "address", "=", address, "&language=", "pt-BR");
		
		log.info("Calling geocoding api with: " + googleurl);

		GeoCoding geoCoding = restTemplate.getForObject(googleurl, GeoCoding.class);
		
		log.info(geoCoding.toString());

		return geoCoding;
	}
	
}