package com.jlarger.eventhub.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.Amizade;

public interface AmizadeRepository extends JpaRepository<Amizade, Long> {

	@Query("SELECT count(a) FROM Amizade a WHERE a.usuario.id = :idUsuario")
	Integer countByIdUsuarioOrigem(Long idUsuario);

	@Query("SELECT count(a) FROM Amizade a WHERE a.amigo.id = :idUsuario")
	Integer countByIdUsuarioDestino(Long idUsuario);
	
	@Query("SELECT a FROM Amizade a WHERE ((a.usuario.id = :idUsuario1 AND a.amigo.id = :idUsuario2) OR (a.usuario.id = :idUsuario2 AND a.amigo.id = :idUsuario1))")
	List<Amizade> findAmizadeEntreUsuarios(Long idUsuario1, Long idUsuario2);
	
}
