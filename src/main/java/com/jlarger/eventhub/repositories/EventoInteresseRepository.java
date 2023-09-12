package com.jlarger.eventhub.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.EventoInteresse;

public interface EventoInteresseRepository extends JpaRepository<EventoInteresse, Long> {

	@Query("SELECT ei FROM EventoInteresse ei WHERE ei.evento.id = :idEvento")
	List<EventoInteresse> buscarInteressesPorEvento(Long idEvento);
	
	@Query("SELECT ei FROM EventoInteresse ei WHERE ei.evento.id IN (:listaIdEvento) AND ei.usuario.id = :idUsuario")
	List<EventoInteresse> buscarEventosQueUsuarioDemonstrouInteresse(List<Long> listaIdEvento, Long idUsuario);
	
	@Query("SELECT ei FROM EventoInteresse ei WHERE ei.evento.id = :idEvento AND ei.usuario.id = :idUsuario")
	Optional<EventoInteresse> buscarInteressesPorEventoEUsuario(Long idEvento, Long idUsuario);
	
}
