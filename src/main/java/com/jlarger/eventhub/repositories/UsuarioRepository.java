package com.jlarger.eventhub.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	Optional<Usuario> findByNomeUsuario(String nomeUsuario);

	Optional<Usuario> findByEmail(String email);

	Boolean existsByNomeUsuario(String nomeUsuario);

	Boolean existsByEmail(String email);
	
	@Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.senha = :senha")
    Optional<Usuario> findByEmailAndSenha(String email, String senha);
	
	@Query("SELECT u FROM Usuario u WHERE u.documentoPrincipal = :documentoPrincipal AND u.id <> :id")
    Optional<Usuario> findByDocumentoPrincipalDifferentUsuario(String documentoPrincipal, Long id);
}