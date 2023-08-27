package com.jlarger.eventhub.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jlarger.eventhub.services.AmizadeService;

@RestController
@RequestMapping(value = "api/amizades")
public class AmizadeResource {
	
	@Autowired
	private AmizadeService amizadeService;
	
	@PostMapping("/{id}")
	public ResponseEntity<?> enviarSolicitacaoAmizade(@PathVariable Long id) {
		
		amizadeService.enviarSolicitacaoAmizade(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/{id}/aceitar")
	public ResponseEntity<?> aceitarSolicitacaoAmizade(@PathVariable Long id) {
		
		amizadeService.aceitarSolicitacaoAmizade(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> removerAmizade(@PathVariable Long id) {
		
		amizadeService.removerAmizade(id);
		
		return ResponseEntity.noContent().build();
	}
	
}