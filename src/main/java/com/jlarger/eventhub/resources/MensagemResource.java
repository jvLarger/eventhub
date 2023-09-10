package com.jlarger.eventhub.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jlarger.eventhub.dto.MensagemDTO;
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
	
	@PostMapping("/{id}")
	public ResponseEntity<MensagemDTO> enviarMensagem(@PathVariable Long id, @RequestBody MensagemDTO dto) {
		
		MensagemDTO mensagemDTO = mensagemService.enviarMensagem(id, dto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(mensagemDTO.getId()).toUri();
		
		return ResponseEntity.created(uri).body(mensagemDTO);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<List<MensagemDTO>> buscarMensagens(@PathVariable Long id) {
		
		List<MensagemDTO> listaMensagemDTO = mensagemService.buscarMensagens(id);
		
		return ResponseEntity.ok().body(listaMensagemDTO);
	}
	
	@GetMapping("/{id}/novas")
	public ResponseEntity<List<MensagemDTO>> buscarNovasMensagens(@PathVariable Long id) {
		
		List<MensagemDTO> listaMensagemDTO = mensagemService.buscarNovasMensagens(id);
		
		return ResponseEntity.ok().body(listaMensagemDTO);
	}
	
	@GetMapping("/nao-lidas")
	public ResponseEntity<Integer> getNumeroMensagensNaoLidas() {
		
		Integer countMensagensNaoLidasUsuario = mensagemService.getNumeroMensagensNaoLidas();
		
		return ResponseEntity.ok().body(countMensagensNaoLidasUsuario);
	}
	
}