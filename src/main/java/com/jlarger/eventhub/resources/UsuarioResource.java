package com.jlarger.eventhub.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jlarger.eventhub.dto.UsuarioDTO;
import com.jlarger.eventhub.services.UsuarioService;

@RestController
@RequestMapping(value = "api/usuarios")
public class UsuarioResource {
	
	@Autowired
	private UsuarioService service;
	
	@GetMapping
	public ResponseEntity<UsuarioDTO> getUsuarioLogado() {
		
		UsuarioDTO usuarioDTO = service.buscarUsuarioLogado();
		
		return ResponseEntity.ok().body(usuarioDTO);
	}
	
	@PutMapping
	public ResponseEntity<UsuarioDTO> alterarInformacoesUsuario(@RequestBody UsuarioDTO dto) {
	
		UsuarioDTO usuarioDTO = service.alterarInformacoesUsuario(dto);
		
		return ResponseEntity.ok().body(usuarioDTO);
	}
	
}