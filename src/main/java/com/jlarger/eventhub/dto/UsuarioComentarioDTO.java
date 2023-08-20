package com.jlarger.eventhub.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.jlarger.eventhub.entities.UsuarioComentario;

public class UsuarioComentarioDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
    private UsuarioDTO usuarioOrigem;
    private LocalDateTime dataComentario;
	private String comentario;
	
	public UsuarioComentarioDTO() {
	}
	
	public UsuarioComentarioDTO(Long id, UsuarioDTO usuarioOrigem, LocalDateTime dataComentario, String comentario) {
		super();
		this.id = id;
		this.usuarioOrigem = usuarioOrigem;
		this.dataComentario = dataComentario;
		this.comentario = comentario;
	}
	
	public UsuarioComentarioDTO(UsuarioComentario usuarioComentario) {
		super();
		this.id = usuarioComentario.getId();
		this.usuarioOrigem = new UsuarioDTO(usuarioComentario.getUsuarioOrigem());
		this.dataComentario = usuarioComentario.getDataComentario();
		this.comentario = usuarioComentario.getComentario();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public UsuarioDTO getUsuarioOrigem() {
		return usuarioOrigem;
	}
	public void setUsuarioOrigem(UsuarioDTO usuarioOrigem) {
		this.usuarioOrigem = usuarioOrigem;
	}
	public LocalDateTime getDataComentario() {
		return dataComentario;
	}
	public void setDataComentario(LocalDateTime dataComentario) {
		this.dataComentario = dataComentario;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
}