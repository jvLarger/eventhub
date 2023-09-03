package com.jlarger.eventhub.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.PublicacaoArquivo;

public interface PublicacaoArquivoRepository extends JpaRepository<PublicacaoArquivo, Long> {
	
	@Query("SELECT pa FROM PublicacaoArquivo pa WHERE pa.publicacao.id = :idPublicacao ORDER BY pa.id ASC")
	List<PublicacaoArquivo> buscarArquivosPorPublicacao(Long idPublicacao);
	
	@Query("SELECT pa FROM PublicacaoArquivo pa WHERE pa.publicacao.id IN (:listaIdPublicacao) ORDER BY pa.id ASC")
	List<PublicacaoArquivo> buscarArquivosPorListaPublicacao(List<Long> listaIdPublicacao);
	
	
}