package com.jlarger.eventhub.repositories;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {
	
	@Query("SELECT e FROM Evento e WHERE e.usuario.id = :idUsuario AND ((e.data = :dataAtual AND e.horaInicio > :horaAtual) OR e.data > :dataAtual) ORDER BY e.data ASC, e.horaInicio ASC")
	List<Evento> buscarEventosPedentesDoUsuario(Long idUsuario, Date dataAtual, LocalTime horaAtual);
	
	@Query("SELECT e FROM Evento e WHERE e.usuario.id = :idUsuario AND ((e.data = :dataAtual AND e.horaInicio <= :horaAtual) OR e.data < :dataAtual) ORDER BY e.data DESC, e.horaInicio DESC")
	List<Evento> buscarMeusEventosConcluidos(Long idUsuario, Date dataAtual, LocalTime horaAtual);
	
}