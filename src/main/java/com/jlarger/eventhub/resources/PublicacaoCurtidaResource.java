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
import com.jlarger.eventhub.services.PublicacaoCurtidaService;

@RestController
@RequestMapping("/api/publicacoes/curtidas")
public class PublicacaoCurtidaResource {
		
	@Autowired
	private PublicacaoCurtidaService publicacaoCurtidaService;
	
	@PostMapping("/{id}")
	public ResponseEntity<?> curtirPublicacao(@PathVariable Long id) {
		
		publicacaoCurtidaService.curtirPublicacao(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> descurtirPublicacao(@PathVariable Long id) {
		
		publicacaoCurtidaService.descurtirPublicacao(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<List<UsuarioDTO>> buscarCurtidasPorPublicacao(@PathVariable Long id) {
		
		List<UsuarioDTO> listaUsuarioDTO = publicacaoCurtidaService.buscarCurtidasPorPublicacao(id);
		
		return ResponseEntity.ok().body(listaUsuarioDTO);
	}
}