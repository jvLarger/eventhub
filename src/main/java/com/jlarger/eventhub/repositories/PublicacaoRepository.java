package com.jlarger.eventhub.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.Publicacao;

public interface PublicacaoRepository extends JpaRepository<Publicacao, Long> {
	
	@Query("SELECT p FROM Publicacao p WHERE p.usuario.id = :idUsuaro")
	List<Publicacao> findAllByIdUsuaro(Long idUsuaro);

}