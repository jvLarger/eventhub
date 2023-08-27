package com.jlarger.eventhub.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jlarger.eventhub.dto.NotificacaoDTO;
import com.jlarger.eventhub.services.NotificacaoService;

@RestController
@RequestMapping(value = "api/notificacoes")
public class NotificacaoResource {
	
	@Autowired
	private NotificacaoService notificacaoService;
	
	@PutMapping("/{id}/ler")
	public ResponseEntity<?> marcarComoLida(@PathVariable Long id) {
		
		notificacaoService.marcarComoLida(id);
		
		return ResponseEntity.noContent().build();
	}
	

	@GetMapping("/pendentes")
	public ResponseEntity<List<NotificacaoDTO>> buscarNotificacoesPendentes() {
		
		List<NotificacaoDTO> listaNotificacaoDTO = notificacaoService.buscarNotificacoesPendentes();
		
		return ResponseEntity.ok().body(listaNotificacaoDTO);
	}
}