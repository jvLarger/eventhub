package com.jlarger.eventhub.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jlarger.eventhub.entities.Usuario;
import com.jlarger.eventhub.security.services.UserDetailsImpl;

public class ServiceLocator {
	
	public static Usuario getUsuarioLogado() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		
		Usuario usuario = new Usuario();
		usuario.setId(userDetails.getId());
		
		return usuario;
	}
	
}