package com.jlarger.eventhub.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jlarger.eventhub.dto.ArquivoDTO;
import com.jlarger.eventhub.services.ArquivoService;
@RestController
@RequestMapping(value = "api/arquivos")
public class ArquivoResource {
	
	@Autowired
	private ArquivoService arquivoService;
	
	@PostMapping
    public ResponseEntity<ArquivoDTO> saveBase64ToFile(@RequestBody ArquivoDTO dto) {
		
		ArquivoDTO arquivo = arquivoService.uploadBase64ToFile(dto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(arquivo.getId()).toUri();
		
		return ResponseEntity.created(uri).body(arquivo);
    }
	
}