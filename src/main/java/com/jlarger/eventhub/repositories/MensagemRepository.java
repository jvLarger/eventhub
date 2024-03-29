package com.jlarger.eventhub.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.Mensagem;

public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
	
	@Query("SELECT m FROM Mensagem m WHERE m.usuarioDestino.id = :idUsuario AND m.usuarioOrigem.id IN (:listaIdUsuarioOrigem) AND m.dataLeitura IS NULL")
	List<Mensagem> buscarMensagensNaoLidasSalas(Long idUsuario, List<Long> listaIdUsuarioOrigem);
	
	@Query("SELECT m FROM Mensagem m WHERE ((m.usuarioOrigem.id = :idUsuarioPrimeiro AND m.usuarioDestino.id = :idUsuarioSegundo) OR (m.usuarioOrigem.id = :idUsuarioSegundo AND m.usuarioDestino.id = :idUsuarioPrimeiro)) ORDER BY m.dataMensagem ASC")
	List<Mensagem> buscarMensagensEntreUsuarios(Long idUsuarioPrimeiro, Long idUsuarioSegundo);
	
	@Query("SELECT m FROM Mensagem m WHERE m.usuarioOrigem.id = :idUsuarioOrigem AND m.usuarioDestino.id = :idUsuarioDestino AND m.dataLeitura IS NULL ORDER BY m.dataMensagem ASC")
	List<Mensagem> buscarNovasMensagensEntreUsuarios(Long idUsuarioDestino, Long idUsuarioOrigem);
	
	@Query("SELECT count(m) FROM Mensagem m WHERE m.usuarioDestino.id = :idUsuarioDestino AND m.dataLeitura IS NULL")
	Integer countMensagensNaoLidasUsuario(Long idUsuarioDestino);
	
	@Query("SELECT m FROM Mensagem m WHERE m.usuarioOrigem.id = :idUsuarioOrigem AND m.usuarioDestino.id = :idUsuarioDestino AND m.dataLeitura IS NULL ORDER BY m.dataMensagem ASC")
	List<Mensagem> buscarMensagensNaoLidasEntreUsuarios(Long idUsuarioDestino, Long idUsuarioOrigem);
	
	@Query("SELECT m FROM Mensagem m WHERE (m.usuarioOrigem.id = :idUsuarioPrimeiro OR m.usuarioOrigem.id = :idUsuarioSegundo) AND (m.usuarioDestino.id = :idUsuarioPrimeiro OR m.usuarioDestino.id = :idUsuarioSegundo) ORDER BY m.id DESC")
	List<Mensagem> buscarUltimaMensagemEntreUsuarios(Long idUsuarioPrimeiro, Long idUsuarioSegundo, Pageable pageable);
}