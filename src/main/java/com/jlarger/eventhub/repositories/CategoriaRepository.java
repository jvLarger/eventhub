package com.jlarger.eventhub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jlarger.eventhub.entities.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
