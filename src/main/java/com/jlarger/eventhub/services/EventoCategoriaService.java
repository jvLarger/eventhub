package com.jlarger.eventhub.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.EventoCategoriaDTO;
import com.jlarger.eventhub.entities.Categoria;
import com.jlarger.eventhub.entities.Evento;
import com.jlarger.eventhub.entities.EventoCategoria;
import com.jlarger.eventhub.repositories.EventoCategoriaRepository;

@Service
public class EventoCategoriaService {
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private EventoCategoriaRepository eventoCategoriaRepository;
	
	@Transactional
	public List<EventoCategoria> vincularCategoriasAUmEvento(Evento evento, List<EventoCategoriaDTO> categorias) {
		
		List<EventoCategoria> listaEventoCategoria = new ArrayList<EventoCategoria>();
		
		for (EventoCategoriaDTO eventoCategoriaDTO : categorias) {
			
			Categoria categoria = categoriaService.getCategoria(eventoCategoriaDTO.getCategoria().getId());
			
			EventoCategoria eventoCategoria = new EventoCategoria();
			eventoCategoria.setEvento(evento);
			eventoCategoria.setCategoria(categoria);
			
			eventoCategoria = eventoCategoriaRepository.save(eventoCategoria);
			
			listaEventoCategoria.add(eventoCategoria);
		}
		
		return listaEventoCategoria;
	}
	
	@Transactional
	public List<EventoCategoria> atualizarCategoriasVinculadasAUmEvento(Evento evento, List<EventoCategoriaDTO> categorias) {
		
		HashMap<Long, Long> mapaEventoCategoriaQueSeguemExistindoOuForamCriados = new HashMap<Long, Long>();
		
		for (EventoCategoriaDTO eventoCategoriaDTO : categorias) {
			
			if (eventoCategoriaDTO.getId() == null) {
				
				Categoria categoria = categoriaService.getCategoria(eventoCategoriaDTO.getCategoria().getId());
				
				EventoCategoria eventoCategoria = new EventoCategoria();
				eventoCategoria.setEvento(evento);
				eventoCategoria.setCategoria(categoria);
				
				eventoCategoria = eventoCategoriaRepository.save(eventoCategoria);
				
				mapaEventoCategoriaQueSeguemExistindoOuForamCriados.put(eventoCategoria.getId(), eventoCategoria.getId());

			} else {
				mapaEventoCategoriaQueSeguemExistindoOuForamCriados.put(eventoCategoriaDTO.getId(), eventoCategoriaDTO.getId());
			}
			
		}
		
		List<EventoCategoria> listaEventoCategoria = eventoCategoriaRepository.buscarCategoriasPorEvento(evento.getId());
		
		for (Iterator<EventoCategoria> iterator = listaEventoCategoria.iterator(); iterator.hasNext();) {
			EventoCategoria eventoCategoria = (EventoCategoria) iterator.next();
			
			if (!mapaEventoCategoriaQueSeguemExistindoOuForamCriados.containsKey(eventoCategoria.getId())) {
				eventoCategoriaRepository.delete(eventoCategoria);
				iterator.remove();
			}
			
		}
		
		return listaEventoCategoria;
	}
	
}