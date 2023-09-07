package com.jlarger.eventhub.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
	@Query("SELECT c FROM Categoria c ORDER BY c.nome ASC")
	List<Categoria> buscarCategoriasOrdenadas();
	
}	