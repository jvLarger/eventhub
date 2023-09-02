package com.jlarger.eventhub.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.jlarger.eventhub.entities.PublicacaoComentario;

public class PublicacaoComentarioDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
    private UsuarioDTO usuario;
    private LocalDateTime data;
	private String descricao;
	
	public PublicacaoComentarioDTO() {
	}

	public PublicacaoComentarioDTO(Long id, UsuarioDTO usuario, LocalDateTime data, String descricao) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.data = data;
		this.descricao = descricao;
	}
	
	public PublicacaoComentarioDTO(PublicacaoComentario publicacaoComentario) {
		this.id = publicacaoComentario.getId();
		this.usuario = new UsuarioDTO(publicacaoComentario.getUsuario());
		this.data = publicacaoComentario.getData();
		this.descricao = publicacaoComentario.getDescricao();
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

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}