package com.jlarger.eventhub.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jlarger.eventhub.dto.PerfilDTO;
import com.jlarger.eventhub.services.PerfilService;

@RestController
@RequestMapping(value = "api/perfis")
public class PerfilResource {
	
	@Autowired
	private PerfilService perfilService;
	
	@GetMapping
	public ResponseEntity<PerfilDTO> buscarMeuPerfil() {
		
		PerfilDTO perfilDTO = perfilService.buscarMeuPerfil();
		
		return ResponseEntity.ok().body(perfilDTO);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PerfilDTO> buscarPerfil(@PathVariable Long id) {
		
		PerfilDTO perfilDTO = perfilService.buscarPerfil(id);
		
		return ResponseEntity.ok().body(perfilDTO);
	}
}