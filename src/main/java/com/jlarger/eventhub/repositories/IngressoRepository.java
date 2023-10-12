package com.jlarger.eventhub.repositories;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.Ingresso;

public interface IngressoRepository extends JpaRepository<Ingresso, Long> {
	
    @Query("SELECT count(i) FROM Ingresso i WHERE i.usuario.id = :idUsuario")
	Integer countByIdUsuario(Long idUsuario);
    
    @Query("SELECT i FROM Ingresso i WHERE i.evento.id = :idEvento ORDER BY i.nome ASC")
	List<Ingresso> buscarIngressosPorEvento(Long idEvento);
    
    @Query("SELECT count(i) FROM Ingresso i WHERE i.evento.id = :idEvento")
	Integer countIngressosPorEvento(Long idEvento);
   
    @Query("SELECT i FROM Ingresso i WHERE i.evento.id = :idEvento ORDER BY i.id DESC")
	List<Ingresso> findUltimosIngressosVendidosDoEvento(Long idEvento, Pageable pageable);

    @Query("SELECT i FROM Ingresso i WHERE i.usuario.id = :idUsuario AND ((i.evento.data = :dataAtual AND i.evento.horaInicio > :horaAtual) OR i.evento.data > :dataAtual) ORDER BY i.evento.data ASC, i.evento.horaInicio ASC")
	List<Ingresso> buscarIngressosPendentes(Long idUsuario, Date dataAtual, LocalTime horaAtual);
    
    @Query("SELECT i FROM Ingresso i WHERE i.usuario.id = :idUsuario AND ((i.evento.data = :dataAtual AND i.evento.horaInicio <= :horaAtual) OR i.evento.data < :dataAtual) ORDER BY i.evento.data ASC, i.evento.horaInicio ASC")
  	List<Ingresso> buscarIngressosConcluidos(Long idUsuario, Date dataAtual, LocalTime horaAtual);
    
    @Query("SELECT i FROM Ingresso i WHERE i.identificadorIngresso = :identificadorIngresso")
  	Optional<Ingresso> buscarIngressoPorIdentificador(String identificadorIngresso);
}