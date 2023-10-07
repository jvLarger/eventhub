package com.jlarger.eventhub.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jlarger.eventhub.dto.IngressoDTO;
import com.jlarger.eventhub.services.IngressoService;

@RestController
@RequestMapping(value = "api/ingressos")
public class IngressoResource {
	
	@Autowired
	private IngressoService ingressoService;

	@PostMapping
	public ResponseEntity<IngressoDTO> comprarIngresso(@RequestBody IngressoDTO dto) {
		
		IngressoDTO ingressoDTO = ingressoService.comprarIngresso(dto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(ingressoDTO.getId()).toUri();
		
		return ResponseEntity.created(uri).body(ingressoDTO);
	}
	
	@GetMapping("/pendentes")
	public ResponseEntity<List<IngressoDTO>> buscarIngressosPendentes() {
		
		List<IngressoDTO> liataIngresso = ingressoService.buscarIngressosPendentes();
		
		return ResponseEntity.ok().body(liataIngresso);
	}
	
	@GetMapping("/concluidos")
	public ResponseEntity<List<IngressoDTO>> buscarIngressosConcluidos() {
		
		List<IngressoDTO> liataIngresso = ingressoService.buscarIngressosConcluidos();
		
		return ResponseEntity.ok().body(liataIngresso);
	}
	
}