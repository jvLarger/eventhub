package com.jlarger.eventhub.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.EventoCategoria;

public interface EventoCategoriaRepository extends JpaRepository<EventoCategoria, Long> {

	@Query("SELECT ec FROM EventoCategoria ec WHERE ec.evento.id = :idEvento")
	List<EventoCategoria> buscarCategoriasPorEvento(Long idEvento);

	@Query("SELECT ec FROM EventoCategoria ec WHERE ec.evento.id IN (:listaIdEvento)")
	List<EventoCategoria> buscarCategoriasPorListaEventos(List<Long> listaIdEvento);
	
}