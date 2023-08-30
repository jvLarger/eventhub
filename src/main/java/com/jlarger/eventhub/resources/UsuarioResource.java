package com.jlarger.eventhub.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jlarger.eventhub.dto.ArquivoDTO;
import com.jlarger.eventhub.dto.UsuarioAutenticadoDTO;
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

	@PutMapping("/foto")
	public ResponseEntity<UsuarioDTO> alterarFotoUsuario(@RequestBody ArquivoDTO dto) {

		UsuarioDTO usuarioDTO = service.alterarFotoUsuario(dto);

		return ResponseEntity.ok().body(usuarioDTO);
	}

	@GetMapping("/encontrados")
	public ResponseEntity<Page<UsuarioDTO>> buscarUsuarios(
			@RequestParam(required = false) String nomeCompleto,
			@RequestParam(required = false) Integer page, 
			@RequestParam(required = false) Integer size) {

		Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 10);

		return ResponseEntity.ok().body(service.buscarUsuariosPaginadosOrdenados(nomeCompleto, pageable));
	}
	
	@PutMapping("/identificador")
	public ResponseEntity<UsuarioDTO> alteraIdentificadorUsuario(@RequestBody UsuarioAutenticadoDTO dto) {

		UsuarioDTO usuarioDTO = service.alteraIdentificadorUsuario(dto);

		return ResponseEntity.ok().body(usuarioDTO);
	}
}