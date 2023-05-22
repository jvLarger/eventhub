package com.jlarger.eventhub.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/categorias")
public class CategoriaResource {
	
	@GetMapping()
	public ResponseEntity<?> teste() {
		return ResponseEntity.ok("rota privada");
	}
	
}