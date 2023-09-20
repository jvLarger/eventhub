package com.jlarger.eventhub.resources;

import java.net.URI;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jlarger.eventhub.dto.EventoDTO;
import com.jlarger.eventhub.dto.FeedEventosDTO;
import com.jlarger.eventhub.dto.IndicadoresEventoDTO;
import com.jlarger.eventhub.dto.IngressoDTO;
import com.jlarger.eventhub.services.EventoService;

@RestController
@RequestMapping(value = "api/eventos")
public class EventoResource {

	@Autowired
	private EventoService eventoService;
	
	@PostMapping
	public ResponseEntity<EventoDTO> criarEvento(@RequestBody EventoDTO dto) {
		
		EventoDTO eventoDTO = eventoService.criarEvento(dto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(eventoDTO.getId()).toUri();
		
		return ResponseEntity.created(uri).body(eventoDTO);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<EventoDTO> alterarEvento(@PathVariable Long id, @RequestBody EventoDTO dto) {
		
		EventoDTO eventoDTO = eventoService.alterarEvento(id, dto);
		
		return ResponseEntity.ok().body(eventoDTO);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluirEvento(@PathVariable Long id) {
		
		eventoService.excluirEvento(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EventoDTO> buscarEvento(@PathVariable Long id) {
		
		EventoDTO eventoDTO = eventoService.buscarEvento(id);
		
		return ResponseEntity.ok().body(eventoDTO);
	}
	
	@GetMapping("/pendentes")
	public ResponseEntity<List<EventoDTO>> buscarMeusEventosPendentes() {
		
		List<EventoDTO> listaEventoDTO = eventoService.buscarMeusEventosPendentes();
		
		return ResponseEntity.ok().body(listaEventoDTO);
	}
	
	@GetMapping("/concluidos")
	public ResponseEntity<List<EventoDTO>> buscarMeusEventosConcluidos() {
		
		List<EventoDTO> listaEventoDTO = eventoService.buscarMeusEventosConcluidos();
		
		return ResponseEntity.ok().body(listaEventoDTO);
	}
	
	@GetMapping("/{id}/indicadores")
	public ResponseEntity<IndicadoresEventoDTO> buscarIndicadoresEvento(@PathVariable Long id) {
		
		IndicadoresEventoDTO indicadoresEventoDTO = eventoService.buscarIndicadoresEvento(id);
		
		return ResponseEntity.ok().body(indicadoresEventoDTO);
	}
	
	@GetMapping("/{id}/indicadores/participantes")
	public ResponseEntity<List<IngressoDTO>> buscarParticipantesDoEvento(@PathVariable Long id) {
		
		List<IngressoDTO> listaIngressos = eventoService.buscarParticipantesDoEvento(id);
		
		return ResponseEntity.ok().body(listaIngressos);
	}
	
	@GetMapping
	public ResponseEntity<List<EventoDTO>> buscarEventos(@RequestParam(required = false) String nome, @RequestParam Double valorInicial, @RequestParam Double valorFinal,  @RequestParam Double latitude, @RequestParam Double longitude, @RequestParam(required = false) Double raio, @RequestParam(required = false) String categorias, @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date data, @RequestParam(required = false,name = "page") Integer pageNumber) {
		
		List<EventoDTO> listaEventoDTO = eventoService.buscarEventos(nome, latitude, longitude, raio, categorias, data, pageNumber != null ? pageNumber : 0, 4, valorInicial, valorFinal);
		
		return ResponseEntity.ok().body(listaEventoDTO);
	}
	
	@PostMapping("/{id}/visualizacoes")
	public ResponseEntity<EventoDTO> registrarVisualizacaoEvento(@PathVariable Long id) {
		
		eventoService.registrarVisualizacaoEvento(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/{idEvento}/compartilhar/{idUsuario}")
	public ResponseEntity<?> compratilharEvento(@PathVariable Long idEvento, @PathVariable Long idUsuario) {
		
		eventoService.compratilharEvento(idEvento, idUsuario);
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/feed")
	public ResponseEntity<FeedEventosDTO> buscarFeedEventos(@RequestParam Double latitude, @RequestParam Double longitude) {
		
		FeedEventosDTO feedEventosDTO = eventoService.buscarFeedEventos(latitude, longitude);
		
		return ResponseEntity.ok().body(feedEventosDTO);
	}
}