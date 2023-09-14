package com.jlarger.eventhub.services;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.entities.Evento;
import com.jlarger.eventhub.entities.EventoInteresse;
import com.jlarger.eventhub.entities.Usuario;
import com.jlarger.eventhub.repositories.EventoInteresseRepository;
import com.jlarger.eventhub.repositories.EventoRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.ServiceLocator;

@Service
public class EventoInteresseService {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private EventoInteresseRepository eventoInteresseRepository;
	
	@Autowired
	private EventoRepository eventoRepository;
	
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
	
	@Transactional(readOnly = true)
	public Boolean isDemonstreiInteresseEvento(Long idEvento) {
		
		validarEventoInformado(idEvento);
		
		Optional<EventoInteresse> optionalEventoInteresse = eventoInteresseRepository.buscarInteressesPorEventoEUsuario(idEvento, ServiceLocator.getUsuarioLogado().getId());
		
		return optionalEventoInteresse.isPresent();
	}
	
	@Transactional
	public void demonstrarInteresse(Long idEvento) {
		
		validarEventoInformado(idEvento);
		
		validarSeJaDemonstreiInteresse(idEvento);
		
		Evento evento = getEvento(idEvento);
		
		Usuario usuarioLogado = usuarioService.getUsuarioLogado();
		
		EventoInteresse eventoInteresse = new EventoInteresse();
		eventoInteresse.setEvento(evento);
		eventoInteresse.setUsuario(usuarioLogado);
		
		eventoInteresseRepository.save(eventoInteresse);
	}

	private void validarSeJaDemonstreiInteresse(Long idEvento) {
		
		Optional<EventoInteresse> optionalEventoInteresse = eventoInteresseRepository.buscarInteressesPorEventoEUsuario(idEvento, ServiceLocator.getUsuarioLogado().getId());

		if (optionalEventoInteresse.isPresent()) {
			throw new BusinessException("Interesse já demonstrado no evento!");
		}
		
	}

	@Transactional
	public void removerInteresse(Long idEvento) {
		
		validarEventoInformado(idEvento);
		
		Optional<EventoInteresse> optionalEventoInteresse = eventoInteresseRepository.buscarInteressesPorEventoEUsuario(idEvento, ServiceLocator.getUsuarioLogado().getId());

		if (optionalEventoInteresse.isEmpty()) {
			throw new BusinessException("Interesse não encontrado no evento!");
		}
		
		EventoInteresse eventoInteresse = optionalEventoInteresse.get();
		
		eventoInteresseRepository.delete(eventoInteresse);
	}
	
	@Transactional(readOnly = true)
	private Evento getEvento(Long idEvento) {
		
		validarEventoInformado(idEvento);
		
		Optional<Evento> optionalEvento = eventoRepository.findById(idEvento);

		Evento evento = optionalEvento.orElseThrow(() -> new BusinessException("Evento não encontrada"));

		return evento;
	}

}