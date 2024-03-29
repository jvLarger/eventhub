package com.jlarger.eventhub.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.PublicacaoCurtida;

public interface PublicacaoCurtidaRepository extends JpaRepository<PublicacaoCurtida, Long> {

	@Query("SELECT count(pc) FROM PublicacaoCurtida pc WHERE pc.publicacao.id = :idPublicacao")
	Integer countByIdPublicacao(Long idPublicacao);
	
	@Query("SELECT pc FROM PublicacaoCurtida pc WHERE pc.publicacao.id = :idPublicacao AND pc.usuario.id = :idUsuario")
	Optional<PublicacaoCurtida> buscarPublicacaoCurtidaPorUsuarioEPublicacao(Long idPublicacao, Long idUsuario);
	
	@Query("SELECT pc FROM PublicacaoCurtida pc WHERE pc.publicacao.id = :idPublicacao ORDER BY pc.usuario.nomeCompleto ASC")
	List<PublicacaoCurtida> buscarCurtidasPorPublicacao(Long idPublicacao);
	
	@Query("SELECT pc FROM PublicacaoCurtida pc WHERE pc.publicacao.id IN (:listaIdPublicacao)")
	List<PublicacaoCurtida> buscarCurtidasPorListaPublicacao(List<Long> listaIdPublicacao);
	
	@Query("SELECT pc FROM PublicacaoCurtida pc WHERE pc.publicacao.id IN (:listaIdPublicacao) AND pc.usuario.id = :idUsuario")
	List<PublicacaoCurtida> buscarCurtidasPorPublicacoesEUsuario(List<Long> listaIdPublicacao, Long idUsuario);
	
}