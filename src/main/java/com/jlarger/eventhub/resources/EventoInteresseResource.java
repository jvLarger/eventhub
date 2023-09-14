package com.jlarger.eventhub.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jlarger.eventhub.dto.EventoDTO;
import com.jlarger.eventhub.services.EventoInteresseService;

@RestController
@RequestMapping(value = "api/eventos/interesses")
public class EventoInteresseResource {
	
	@Autowired
	private EventoInteresseService eventoInteresseService;
	
	@PostMapping("/{id}")
	public ResponseEntity<EventoDTO> demonstrarInteresse(@PathVariable Long id) {
		
		eventoInteresseService.demonstrarInteresse(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<EventoDTO> removerInteresse(@PathVariable Long id) {
		
		eventoInteresseService.removerInteresse(id);
		
		return ResponseEntity.noContent().build();
	}
	
}