package com.jlarger.eventhub.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.Notificacao;
import com.jlarger.eventhub.entities.type.TipoNotificacao;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
	
	@Query("SELECT n FROM Notificacao n WHERE n.usuarioOrigem.id = :idUsuaroOrigem AND n.usuarioDestino.id = :idUsuaroDestino AND n.tipo = :tipo  AND n.dataLeitura IS NULL")
	List<Notificacao> buscarNotificacoesPendentesEntreUsuarios(Long idUsuaroOrigem, Long idUsuaroDestino, TipoNotificacao tipo);

	@Query("SELECT n FROM Notificacao n WHERE n.usuarioOrigem.id = :idUsuaroOrigem AND n.usuarioDestino.id = :idUsuaroDestino AND n.tipo = :tipo  AND n.dataLeitura IS NOT NULL")
	List<Notificacao> buscarNotificacoesLidasEntreUsuarios(Long idUsuaroOrigem, Long idUsuaroDestino, TipoNotificacao tipo);
	
	@Query("SELECT n FROM Notificacao n WHERE n.usuarioDestino.id = :idUsuaroDestino AND n.dataLeitura IS NULL ORDER BY n.dataNotificacao DESC")
	List<Notificacao> buscarNotificacoesPendentes(Long idUsuaroDestino);

}