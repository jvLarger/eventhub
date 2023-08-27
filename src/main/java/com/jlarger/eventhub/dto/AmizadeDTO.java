package com.jlarger.eventhub.dto;

import java.io.Serializable;

import com.jlarger.eventhub.entities.Amizade;

public class AmizadeDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
    private UsuarioDTO usuario;
    private UsuarioDTO amigo;
    
    public AmizadeDTO(Long id, UsuarioDTO usuario, UsuarioDTO amigo) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.amigo = amigo;
	}
    
    public AmizadeDTO(Amizade amizade) {
		this.id = amizade.getId();
		this.usuario = new UsuarioDTO(amizade.getUsuario());
		this.amigo = new UsuarioDTO(amizade.getAmigo());
	}
    
	public AmizadeDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}

	public UsuarioDTO getAmigo() {
		return amigo;
	}

	public void setAmigo(UsuarioDTO amigo) {
		this.amigo = amigo;
	}
    
    
    
}