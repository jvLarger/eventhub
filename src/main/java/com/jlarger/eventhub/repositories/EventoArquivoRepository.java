package com.jlarger.eventhub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jlarger.eventhub.entities.EventoArquivo;

public interface EventoArquivoRepository extends JpaRepository<EventoArquivo, Long> {
 
}