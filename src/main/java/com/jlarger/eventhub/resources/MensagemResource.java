package com.jlarger.eventhub.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jlarger.eventhub.dto.SalaBatePapoDTO;
import com.jlarger.eventhub.services.MensagemService;

@RestController
@RequestMapping(value = "api/mensagens")
public class MensagemResource {
	
	@Autowired
	private MensagemService mensagemService;
	
	@GetMapping("/salas")
	public ResponseEntity<List<SalaBatePapoDTO>> buscarSalasBatePapo() {
		
		List<SalaBatePapoDTO> listaSalaBatePapoDTO = mensagemService.buscarSalasBatePapo();
		
		return ResponseEntity.ok().body(listaSalaBatePapoDTO);
	}
	
}