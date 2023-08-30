package com.jlarger.eventhub.dto;

import java.io.Serializable;

public class UsuarioAutenticadoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String email;
	private String nomeUsuario;
	private String nomeCompleto;
	private String token;
	private String identificadorNotificacao;
	private ArquivoDTO foto;
	
	public UsuarioAutenticadoDTO() {
	}

	public UsuarioAutenticadoDTO(Long id, String email, String nomeUsuario, String nomeCompleto, String token, String identificadorNotificacao) {
		super();
		this.id = id;
		this.email = email;
		this.nomeUsuario = nomeUsuario;
		this.nomeCompleto = nomeCompleto;
		this.token = token;
		this.identificadorNotificacao = identificadorNotificacao;
	}
	
	public UsuarioAutenticadoDTO(Long id, String email, String nomeUsuario, String nomeCompleto, String token, ArquivoDTO foto, String identificadorNotificacao) {
		super();
		this.id = id;
		this.email = email;
		this.nomeUsuario = nomeUsuario;
		this.nomeCompleto = nomeCompleto;
		this.token = token;
		this.foto = foto;
		this.identificadorNotificacao = identificadorNotificacao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public ArquivoDTO getFoto() {
		return foto;
	}

	public void setFoto(ArquivoDTO foto) {
		this.foto = foto;
	}

	public String getIdentificadorNotificacao() {
		return identificadorNotificacao;
	}

	public void setIdentificadorNotificacao(String identificadorNotificacao) {
		this.identificadorNotificacao = identificadorNotificacao;
	}

}