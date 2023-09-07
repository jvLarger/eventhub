package com.jlarger.eventhub.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.entities.EventoInteresse;
import com.jlarger.eventhub.repositories.EventoInteresseRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;

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

}