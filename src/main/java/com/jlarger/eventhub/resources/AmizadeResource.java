package com.jlarger.eventhub.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jlarger.eventhub.dto.UsuarioDTO;
import com.jlarger.eventhub.services.AmizadeService;
import com.jlarger.eventhub.utils.ServiceLocator;

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
	
	@GetMapping
	public ResponseEntity<List<UsuarioDTO>> buscarAmigos() {
		
		List<UsuarioDTO> listaUsuarioDTO = amizadeService.buscarAmigos(ServiceLocator.getUsuarioLogado().getId());
		
		return ResponseEntity.ok().body(listaUsuarioDTO);
	}
	
}