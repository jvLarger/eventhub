package com.jlarger.eventhub.dto;

import java.io.Serializable;

import com.jlarger.eventhub.entities.Mensagem;

public class SalaBatePapoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private UsuarioDTO usuario;
	private Integer mensagensNaoLidas;
	private MensagemDTO ultimaMensagem;
	
	public SalaBatePapoDTO() {
	}

	public SalaBatePapoDTO(UsuarioDTO usuario, Integer mensagensNaoLidas, Mensagem ultimaMensagem) {
		super();
		this.usuario = usuario;
		this.mensagensNaoLidas = mensagensNaoLidas;
		this.ultimaMensagem = new MensagemDTO(ultimaMensagem);
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

	public MensagemDTO getUltimaMensagem() {
		return ultimaMensagem;
	}

	public void setUltimaMensagem(MensagemDTO ultimaMensagem) {
		this.ultimaMensagem = ultimaMensagem;
	}
	
}