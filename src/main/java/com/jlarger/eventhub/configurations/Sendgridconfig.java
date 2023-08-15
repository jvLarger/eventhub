package com.jlarger.eventhub.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sendgrid.SendGrid;

@Configuration
public class Sendgridconfig {

	@Value("${sendgrid.key}")
	private String key;

	@Bean
	SendGrid getSendgrid() {
		return new SendGrid(key);
	}

}