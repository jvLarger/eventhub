package com.jlarger.eventhub.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.jlarger.eventhub.entities.Notificacao;
import com.jlarger.eventhub.entities.type.TipoNotificacao;

public class NotificacaoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	private UsuarioDTO usuarioOrigem;
	private UsuarioDTO usuarioDestino;
	private LocalDateTime dataNotificacao;
	private LocalDateTime dataLeitura;
	private String descricao;
	private TipoNotificacao tipo;

	public NotificacaoDTO() {
	}

	public NotificacaoDTO(Long id, UsuarioDTO usuarioOrigem, UsuarioDTO usuarioDestino, LocalDateTime dataNotificacao, LocalDateTime dataLeitura, String descricao, TipoNotificacao tipo) {
		super();
		this.id = id;
		this.usuarioOrigem = usuarioOrigem;
		this.usuarioDestino = usuarioDestino;
		this.dataNotificacao = dataNotificacao;
		this.dataLeitura = dataLeitura;
		this.descricao = descricao;
		this.tipo = tipo;
	}
	
	public NotificacaoDTO(Notificacao notificacao) {
		super();
		this.id = notificacao.getId();
		this.usuarioOrigem = new UsuarioDTO(notificacao.getUsuarioOrigem());
		this.usuarioDestino = new UsuarioDTO(notificacao.getUsuarioDestino());
		this.dataNotificacao = notificacao.getDataNotificacao();
		this.dataLeitura = notificacao.getDataLeitura();
		this.descricao = notificacao.getDescricao();
		this.tipo = notificacao.getTipo();
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

	public UsuarioDTO getUsuarioDestino() {
		return usuarioDestino;
	}

	public void setUsuarioDestino(UsuarioDTO usuarioDestino) {
		this.usuarioDestino = usuarioDestino;
	}

	public LocalDateTime getDataNotificacao() {
		return dataNotificacao;
	}

	public void setDataNotificacao(LocalDateTime dataNotificacao) {
		this.dataNotificacao = dataNotificacao;
	}

	public LocalDateTime getDataLeitura() {
		return dataLeitura;
	}

	public void setDataLeitura(LocalDateTime dataLeitura) {
		this.dataLeitura = dataLeitura;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoNotificacao getTipo() {
		return tipo;
	}

	public void setTipo(TipoNotificacao tipo) {
		this.tipo = tipo;
	}
}