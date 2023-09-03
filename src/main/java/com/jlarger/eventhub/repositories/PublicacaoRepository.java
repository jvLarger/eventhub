package com.jlarger.eventhub.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.Publicacao;

public interface PublicacaoRepository extends JpaRepository<Publicacao, Long> {
	
	@Query("SELECT p FROM Publicacao p WHERE p.usuario.id = :idUsuaro")
	List<Publicacao> findAllByIdUsuaro(Long idUsuaro);

	@Query("SELECT p FROM Publicacao p LEFT JOIN p.usuario u LEFT JOIN Amizade a ON ((p.usuario.id = a.usuario.id AND a.amigo.id = :idUsuaro) OR (p.usuario.id = a.amigo.id AND a.usuario.id = :idUsuaro)) ORDER BY CASE WHEN a.id IS NOT NULL THEN 0 ELSE 1 END, p.data DESC")
	Page<Publicacao> findPaginatedPublicacoesForUserAndFriends(Long idUsuaro, Pageable pageable);
	
}