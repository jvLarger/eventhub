package com.jlarger.eventhub.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.EventoArquivoDTO;
import com.jlarger.eventhub.entities.Arquivo;
import com.jlarger.eventhub.entities.Evento;
import com.jlarger.eventhub.entities.EventoArquivo;
import com.jlarger.eventhub.repositories.EventoArquivoRepository;

@Service
public class EventoArquivoService {

	@Autowired
	private ArquivoService arquivoService;
	
	@Autowired
	private EventoArquivoRepository eventoArquivoRepository;
	
	@Transactional
	public List<EventoArquivo> vincularArquivosAUmEvento(Evento evento, List<EventoArquivoDTO> arquivos) {
		
		List<EventoArquivo> listaEventoArquivo = new ArrayList<EventoArquivo>();
		
		for (EventoArquivoDTO eventoArquivoDTO : arquivos) {
			
			Arquivo arquivo = arquivoService.getArquivo(eventoArquivoDTO.getArquivo().getId());
			
			EventoArquivo eventoArquivo = new EventoArquivo();
			eventoArquivo.setEvento(evento);
			eventoArquivo.setArquivo(arquivo);
			
			eventoArquivo = eventoArquivoRepository.save(eventoArquivo);
			
			listaEventoArquivo.add(eventoArquivo);
		}
		
		return listaEventoArquivo;
	}
	
}