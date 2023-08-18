package com.jlarger.eventhub.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jlarger.eventhub.entities.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
	
    @Query("SELECT t FROM Token t WHERE t.codigo = :codigo AND t.usuario.email = :email")
    Optional<Token> findByEmailAndCodigo(String email, Integer codigo);
	
}