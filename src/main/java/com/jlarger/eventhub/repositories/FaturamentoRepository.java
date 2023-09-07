package com.jlarger.eventhub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jlarger.eventhub.entities.Faturamento;

public interface FaturamentoRepository extends JpaRepository<Faturamento, Long> {

}
