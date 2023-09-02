package com.jlarger.eventhub.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jlarger.eventhub.dto.PublicacaoDTO;
import com.jlarger.eventhub.services.PublicacaoService;

@RestController
@RequestMapping("/api/publicacoes")
public class PublicacaoResource {
	
	@Autowired
	private PublicacaoService publicacaoService;
	
	@PostMapping
	public ResponseEntity<?> criarPublicacao(@RequestBody PublicacaoDTO dto) {
		
		PublicacaoDTO publicacaoDTO = publicacaoService.criarPublicacao(dto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(publicacaoDTO.getId()).toUri();
		
		return ResponseEntity.created(uri).body(publicacaoDTO);
	}
	
}