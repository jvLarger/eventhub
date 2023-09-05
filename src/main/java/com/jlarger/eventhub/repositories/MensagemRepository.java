package com.jlarger.eventhub.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.Mensagem;

public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
	
	@Query("SELECT m FROM Mensagem m WHERE m.usuarioDestino.id = :idUsuario AND m.usuarioOrigem.id IN (:listaIdUsuarioOrigem) AND m.dataLeitura IS NULL")
	List<Mensagem> buscarMensagensNaoLidasSalas(Long idUsuario, List<Long> listaIdUsuarioOrigem);
	
}