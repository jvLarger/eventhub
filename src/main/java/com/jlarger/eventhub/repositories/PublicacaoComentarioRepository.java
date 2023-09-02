package com.jlarger.eventhub.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.PublicacaoComentario;

public interface PublicacaoComentarioRepository extends JpaRepository<PublicacaoComentario, Long> {

	@Query("SELECT pc FROM PublicacaoComentario pc WHERE pc.publicacao.id = :idPublicacao ORDER BY pc.data ASC")
	List<PublicacaoComentario> buscarComentariosPorPublicacao(Long idPublicacao);
	
}