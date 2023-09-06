package com.jlarger.eventhub.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.jlarger.eventhub.entities.Mensagem;

public class MensagemDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
    private UsuarioDTO usuarioOrigem;
    private UsuarioDTO usuarioDestino;
    private LocalDateTime dataMensagem;
    private LocalDateTime dataLeitura;
	private String descricao;
    private EventoDTO evento;
    
    public MensagemDTO() {
	}

	public MensagemDTO(Long id, UsuarioDTO usuarioOrigem, UsuarioDTO usuarioDestino, LocalDateTime dataMensagem, LocalDateTime dataLeitura, String descricao, EventoDTO evento) {
		this.id = id;
		this.usuarioOrigem = usuarioOrigem;
		this.usuarioDestino = usuarioDestino;
		this.dataMensagem = dataMensagem;
		this.dataLeitura = dataLeitura;
		this.descricao = descricao;
		this.evento = evento;
	}
    
	public MensagemDTO(Mensagem mensagem) {
		this.id = mensagem.getId();
		this.usuarioOrigem = new UsuarioDTO(mensagem.getUsuarioOrigem());
		this.usuarioDestino = new UsuarioDTO(mensagem.getUsuarioDestino());
		this.dataMensagem = mensagem.getDataMensagem();
		this.dataLeitura = mensagem.getDataLeitura();
		this.descricao = mensagem.getDescricao();
		this.evento = mensagem.getEvento() != null ? new EventoDTO(mensagem.getEvento()) : null;
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

	public LocalDateTime getDataMensagem() {
		return dataMensagem;
	}

	public void setDataMensagem(LocalDateTime dataMensagem) {
		this.dataMensagem = dataMensagem;
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

	public EventoDTO getEvento() {
		return evento;
	}

	public void setEvento(EventoDTO evento) {
		this.evento = evento;
	}

}