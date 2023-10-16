package com.jlarger.eventhub.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.Faturamento;

public interface FaturamentoRepository extends JpaRepository<Faturamento, Long> {
	
	@Query("SELECT f FROM Faturamento f WHERE f.evento.id = :idEvento")
	Optional<Faturamento> buscarFaturamentoPorEvento(Long idEvento);
	
	@Query("SELECT f FROM Faturamento f WHERE f.evento.usuario.id = :idUsuario AND f.dataPagamento IS NULL ORDER BY f.dataLiberacao ASC")
	List<Faturamento> buscarFaturamentosPendentes(Long idUsuario);
	
	@Query("SELECT f FROM Faturamento f WHERE f.evento.usuario.id = :idUsuario AND f.dataPagamento IS NULL AND f.dataLiberacao <= now() ORDER BY f.id ASC")
	List<Faturamento> buscarFaturamentosLiberadosENaoPagosPorUsuario(Long idUsuario);
}