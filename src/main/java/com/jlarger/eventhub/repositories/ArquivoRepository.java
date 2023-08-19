package com.jlarger.eventhub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jlarger.eventhub.entities.Arquivo;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {

}
