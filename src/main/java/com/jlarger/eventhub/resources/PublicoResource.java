package com.jlarger.eventhub.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jlarger.eventhub.dto.UsuarioAutenticadoDTO;
import com.jlarger.eventhub.dto.UsuarioDTO;
import com.jlarger.eventhub.services.UsuarioService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/publico")
public class PublicoResource {
	
	@Autowired
	private UsuarioService service;
	
	@PostMapping("/login")
	public ResponseEntity<UsuarioAutenticadoDTO> login(@RequestBody UsuarioDTO dto) {
		
		UsuarioAutenticadoDTO usuarioAutenticadoDTO = service.login(dto);
		
		return ResponseEntity.ok().body(usuarioAutenticadoDTO);
	}
	
	@PostMapping("/nova-conta")
	public ResponseEntity<UsuarioAutenticadoDTO> novoUsuario(@RequestBody UsuarioDTO dto) {
		
		UsuarioAutenticadoDTO usuarioAutenticadoDTO = service.novoUsuario(dto);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		
		return ResponseEntity.created(uri).body(usuarioAutenticadoDTO);
	}
	
	@GetMapping("/token-valido")
	public ResponseEntity<UsuarioAutenticadoDTO> isTokenValido() {
		return null;
	}
	
}