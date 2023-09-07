package com.jlarger.eventhub.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.EventoArquivoDTO;
import com.jlarger.eventhub.entities.Arquivo;
import com.jlarger.eventhub.entities.Evento;
import com.jlarger.eventhub.entities.EventoArquivo;
import com.jlarger.eventhub.repositories.EventoArquivoRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;

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
	
	@Transactional
	public List<EventoArquivo> atualizarArquivosVinculadosAUmEvento(Evento evento, List<EventoArquivoDTO> arquivos) {
		
		HashMap<Long, Long> mapaEventoARquivoQueSeguemExistindoOuForamCriados = new HashMap<Long, Long>();
		
		for (EventoArquivoDTO eventoArquivoDTO : arquivos) {
			
			if (eventoArquivoDTO.getId() == null) {
				
				Arquivo arquivo = arquivoService.getArquivo(eventoArquivoDTO.getArquivo().getId());
				
				EventoArquivo eventoArquivo = new EventoArquivo();
				eventoArquivo.setEvento(evento);
				eventoArquivo.setArquivo(arquivo);
				
				eventoArquivo = eventoArquivoRepository.save(eventoArquivo);
				
				mapaEventoARquivoQueSeguemExistindoOuForamCriados.put(eventoArquivo.getId(), eventoArquivo.getId());

			} else {
				mapaEventoARquivoQueSeguemExistindoOuForamCriados.put(eventoArquivoDTO.getId(), eventoArquivoDTO.getId());
			}
			
		}
		
		List<EventoArquivo> listaEventoArquivo = eventoArquivoRepository.buscarArquivosPorEvento(evento.getId());
		
		for (Iterator<EventoArquivo> iterator = listaEventoArquivo.iterator(); iterator.hasNext();) {
			EventoArquivo eventoArquivo = (EventoArquivo) iterator.next();
			
			if (!mapaEventoARquivoQueSeguemExistindoOuForamCriados.containsKey(eventoArquivo.getId())) {
				
				eventoArquivoRepository.delete(eventoArquivo);
				
				arquivoService.excluirArquivo(eventoArquivo.getArquivo());
				
				iterator.remove();
			}
			
		}
		
		return listaEventoArquivo;
	}
	
	@Transactional
	public void excluirArquivosPorEvento(Long idEvento) {
		
		validarEventoInformado(idEvento);
		
		List<EventoArquivo> listaEventoArquivo = eventoArquivoRepository.buscarArquivosPorEvento(idEvento);
		
		for (EventoArquivo eventoArquivo : listaEventoArquivo) {
			
			eventoArquivoRepository.delete(eventoArquivo);
			
			arquivoService.excluirArquivo(eventoArquivo.getArquivo());
			
		}
	}

	private void validarEventoInformado(Long idEvento) {
		
		if (idEvento == null || idEvento.compareTo(0L) <= 0) {
			throw new BusinessException("Evento não informado!");
		}
		
	}

	@Transactional(readOnly = true)
	public List<EventoArquivo> buscarArquviosPorEvento(Long idEvento) {
		
		validarEventoInformado(idEvento);
		
		List<EventoArquivo> listaEventoArquivo = eventoArquivoRepository.buscarArquivosPorEvento(idEvento);
	
		return listaEventoArquivo;
	}

	public HashMap<Long, ArrayList<EventoArquivo>> getMapaArquivosPorEventos(List<Long> listaIdEvento) {
		
		HashMap<Long, ArrayList<EventoArquivo>> mapaArquivosPorEventos = new HashMap<Long, ArrayList<EventoArquivo>>();
		
		if (listaIdEvento.size() > 0) {
			
			List<EventoArquivo> listaEventoArquivo = eventoArquivoRepository.buscarArquivosPorListaEventos(listaIdEvento);
			
			for (EventoArquivo eventoArquivo : listaEventoArquivo) {
				
				if (!mapaArquivosPorEventos.containsKey(eventoArquivo.getEvento().getId())) {
					mapaArquivosPorEventos.put(eventoArquivo.getEvento().getId(), new ArrayList<EventoArquivo>());
				}
				
				mapaArquivosPorEventos.get(eventoArquivo.getEvento().getId()).add(eventoArquivo);
			}
			
		}
		
		return mapaArquivosPorEventos;
	}
	
}