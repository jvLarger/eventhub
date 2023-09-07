package com.jlarger.eventhub.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.Ingresso;

public interface IngressoRepository extends JpaRepository<Ingresso, Long> {
	
    @Query("SELECT count(i) FROM Ingresso i WHERE i.usuario.id = :idUsuario")
	Integer countByIdUsuario(Long idUsuario);
    
    @Query("SELECT i FROM Ingresso i WHERE i.evento.id = :idEvento")
	List<Ingresso> buscarIngressosPorEvento(Long idEvento);
	
}