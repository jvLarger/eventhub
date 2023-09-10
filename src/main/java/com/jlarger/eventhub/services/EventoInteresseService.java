package com.jlarger.eventhub.services;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.entities.EventoInteresse;
import com.jlarger.eventhub.repositories.EventoInteresseRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.ServiceLocator;

@Service
public class EventoInteresseService {
	
	@Autowired
	private EventoInteresseRepository eventoInteresseRepository;
	
	@Transactional
	public void excluirInteressesPorEvento(Long idEvento) {
		
		validarEventoInformado(idEvento);
		
		List<EventoInteresse> listaEventoInteresse = eventoInteresseRepository.buscarInteressesPorEvento(idEvento);
		
		for (EventoInteresse eventoInteresse : listaEventoInteresse) {
			eventoInteresseRepository.delete(eventoInteresse);
		}
		
	}
	
	private void validarEventoInformado(Long idEvento) {
		
		if (idEvento == null || idEvento.compareTo(0L) <= 0) {
			throw new BusinessException("Evento não informado!");
		}
		
	}
	
	@Transactional(readOnly = true)
	public HashMap<Long, Long> getMapaMapaEventosQueDemonstreiInteresse(List<Long> listaIdEvento) {
		
		HashMap<Long, Long> mapaMapaEventosQueDemonstreiInteresse = new HashMap<Long, Long>();
		
		if (listaIdEvento != null && listaIdEvento.size() > 0) {
			
			List<EventoInteresse> listaEventoInteresse = eventoInteresseRepository.buscarEventosQueUsuarioDemonstrouInteresse(listaIdEvento, ServiceLocator.getUsuarioLogado().getId());
			
			for (EventoInteresse eventoInteresse : listaEventoInteresse) {
				mapaMapaEventosQueDemonstreiInteresse.put(eventoInteresse.getEvento().getId(), eventoInteresse.getEvento().getId());
			}
			
		}
		
		return mapaMapaEventosQueDemonstreiInteresse;
	}

}