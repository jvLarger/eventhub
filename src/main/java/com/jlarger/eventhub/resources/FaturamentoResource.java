package com.jlarger.eventhub.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jlarger.eventhub.dto.FaturamentoPagamentoDTO;
import com.jlarger.eventhub.services.FaturamentoService;

@RestController
@RequestMapping(value = "api/faturamentos")
public class FaturamentoResource {
	
	@Autowired
	private FaturamentoService faturamentoService;
	
	@GetMapping
	public ResponseEntity<FaturamentoPagamentoDTO> buscarFaturamentos() {
		
		FaturamentoPagamentoDTO faturamentoPagamentoDTO = faturamentoService.buscarFaturamentos();
		
		return ResponseEntity.ok().body(faturamentoPagamentoDTO);
	}
	
	@PutMapping
	public ResponseEntity<?> pagarFechamentosLiberados() {
		
		faturamentoService.pagarFechamentosLiberados();
		
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping
	public ResponseEntity<?> removerConnectedAccount() {
		
		faturamentoService.removerConnectedAccount();
		
		return ResponseEntity.noContent().build();
	}
	
}