package com.jlarger.eventhub.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jlarger.eventhub.dto.UsuarioComentarioDTO;
import com.jlarger.eventhub.services.UsuarioComentarioService;

@RestController
@RequestMapping(value = "api/usuarios/comentarios")
public class UsuarioComentarioResource {
	

	@Autowired
	private UsuarioComentarioService usuarioComentarioService;
	
	@PostMapping("/{id}")
	public ResponseEntity<?> enviarSolicitacaoComentario(@PathVariable Long id, @RequestBody UsuarioComentarioDTO dto) {
		
		usuarioComentarioService.enviarSolicitacaoComentario(id, dto);
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/{id}/aceitar")
	public ResponseEntity<?> aceitarSolicitacaoComentario(@PathVariable Long id) {
		
		usuarioComentarioService.aceitarSolicitacaoComentario(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> removerComentario(@PathVariable Long id) {
		
		usuarioComentarioService.removerComentario(id);
		
		return ResponseEntity.noContent().build();
	}
}