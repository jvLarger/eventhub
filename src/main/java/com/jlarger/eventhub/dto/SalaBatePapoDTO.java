package com.jlarger.eventhub.dto;

import java.io.Serializable;

public class SalaBatePapoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private UsuarioDTO usuario;
	private Integer mensagensNaoLidas;
	
	public SalaBatePapoDTO() {
	}

	public SalaBatePapoDTO(UsuarioDTO usuario, Integer mensagensNaoLidas) {
		super();
		this.usuario = usuario;
		this.mensagensNaoLidas = mensagensNaoLidas;
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}

	public Integer getMensagensNaoLidas() {
		return mensagensNaoLidas;
	}

	public void setMensagensNaoLidas(Integer mensagensNaoLidas) {
		this.mensagensNaoLidas = mensagensNaoLidas;
	}
	
}
