package com.jlarger.eventhub.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jlarger.eventhub.dto.EventoDTO;
import com.jlarger.eventhub.services.EventoService;

@RestController
@RequestMapping(value = "api/eventos")
public class EventoResource {

	@Autowired
	private EventoService eventoService;
	
	@PostMapping
	public ResponseEntity<EventoDTO> criarEvento(@RequestBody EventoDTO dto) {
		
		EventoDTO eventoDTO = eventoService.criarEvento(dto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(eventoDTO.getId()).toUri();
		
		return ResponseEntity.created(uri).body(eventoDTO);
	}
	
}