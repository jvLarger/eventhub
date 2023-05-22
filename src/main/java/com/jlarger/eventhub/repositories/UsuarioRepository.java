package com.jlarger.eventhub.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jlarger.eventhub.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	Optional<Usuario> findByNomeUsuario(String nomeUsuario);

	Boolean existsByNomeUsuario(String nomeUsuario);

	Boolean existsByEmail(String email);
	
}