package com.jlarger.eventhub.dto;

import java.io.Serializable;
import java.util.List;

public class PerfilDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private UsuarioDTO usuario;
	private Integer numeroAmigos;
	private Integer numeroEventos;
	private Boolean isAmigo;
	private Boolean isSolicitacaoAmizadePendente;
	private List<PublicacaoResumidaDTO> publicacoes;
	private List<UsuarioComentarioDTO> comentarios;
	
	public PerfilDTO(UsuarioDTO usuario, Integer numeroAmigos, Integer numeroEventos, Boolean isAmigo, List<PublicacaoResumidaDTO> publicacoes, List<UsuarioComentarioDTO> comentarios, Boolean isSolicitacaoAmizadePendente) {
		super();
		this.usuario = usuario;
		this.numeroAmigos = numeroAmigos;
		this.numeroEventos = numeroEventos;
		this.isAmigo = isAmigo;
		this.publicacoes = publicacoes;
		this.comentarios = comentarios;
		this.isSolicitacaoAmizadePendente = isSolicitacaoAmizadePendente;
	}
	
	public PerfilDTO() {
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}
	public Integer getNumeroAmigos() {
		return numeroAmigos;
	}
	public void setNumeroAmigos(Integer numeroAmigos) {
		this.numeroAmigos = numeroAmigos;
	}
	public Integer getNumeroEventos() {
		return numeroEventos;
	}
	public void setNumeroEventos(Integer numeroEventos) {
		this.numeroEventos = numeroEventos;
	}
	public Boolean getIsAmigo() {
		return isAmigo;
	}
	public void setIsAmigo(Boolean isAmigo) {
		this.isAmigo = isAmigo;
	}
	public List<PublicacaoResumidaDTO> getPublicacoes() {
		return publicacoes;
	}
	public void setPublicacoes(List<PublicacaoResumidaDTO> publicacoes) {
		this.publicacoes = publicacoes;
	}
	public List<UsuarioComentarioDTO> getComentarios() {
		return comentarios;
	}
	public void setComentarios(List<UsuarioComentarioDTO> comentarios) {
		this.comentarios = comentarios;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getIsSolicitacaoAmizadePendente() {
		return isSolicitacaoAmizadePendente;
	}

	public void setIsSolicitacaoAmizadePendente(Boolean isSolicitacaoAmizadePendente) {
		this.isSolicitacaoAmizadePendente = isSolicitacaoAmizadePendente;
	}
}