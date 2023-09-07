package com.jlarger.eventhub.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.Faturamento;

public interface FaturamentoRepository extends JpaRepository<Faturamento, Long> {
	
	@Query("SELECT f FROM Faturamento f WHERE f.evento.id = :idEvento")
	Optional<Faturamento> buscarFaturamentoPorEvento(Long idEvento);
	
}