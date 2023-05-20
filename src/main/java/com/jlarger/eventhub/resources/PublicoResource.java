package com.jlarger.eventhub.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/publico")
public class PublicoResource {
	
	@GetMapping("/token-valido")
	public ResponseEntity<?> teste() {
		return ResponseEntity.ok("Rota Pública");
	}
	
}