package com.jlarger.eventhub.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jlarger.eventhub.dto.PublicacaoComentarioDTO;
import com.jlarger.eventhub.services.PublicacaoComentarioService;

@RestController
@RequestMapping("/api/publicacoes/comentarios")
public class PublicacaoComentarioResource {
	
	@Autowired
	private PublicacaoComentarioService publicacaoComentarioService;
	
	@PostMapping("/{id}")
	public ResponseEntity<PublicacaoComentarioDTO> criarPublicacaoComentario(@PathVariable Long id, @RequestBody PublicacaoComentarioDTO dto) {
		
		PublicacaoComentarioDTO publicacaoComentarioDTO = publicacaoComentarioService.criarPublicacaoComentario(dto, id);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(publicacaoComentarioDTO.getId()).toUri();
		
		return ResponseEntity.created(uri).body(publicacaoComentarioDTO);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluirPublicacaoComentario(@PathVariable Long id) {
		
		publicacaoComentarioService.excluirPublicacaoComentario(id);
		
		return ResponseEntity.noContent().build();
	}
}