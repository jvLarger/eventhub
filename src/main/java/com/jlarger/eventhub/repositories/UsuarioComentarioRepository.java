package com.jlarger.eventhub.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.UsuarioComentario;

public interface UsuarioComentarioRepository extends JpaRepository<UsuarioComentario, Long>{

	@Query("SELECT uc FROM UsuarioComentario uc WHERE uc.usuario.id = :idUsuaro")
    List<UsuarioComentario> findAllByIdUsuaro(Long idUsuaro);
	
}