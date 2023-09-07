package com.jlarger.eventhub.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.EventoArquivo;

public interface EventoArquivoRepository extends JpaRepository<EventoArquivo, Long> {
	
	@Query("SELECT ea FROM EventoArquivo ea WHERE ea.evento.id = :idEvento")
	List<EventoArquivo> buscarArquivosPorEvento(Long idEvento);
	
	@Query("SELECT ea FROM EventoArquivo ea WHERE ea.evento.id IN (:listaIdEvento)")
	List<EventoArquivo> buscarArquivosPorListaEventos(List<Long> listaIdEvento);
 
}