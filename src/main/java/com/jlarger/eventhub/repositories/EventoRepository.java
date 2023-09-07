package com.jlarger.eventhub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jlarger.eventhub.entities.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {

}
