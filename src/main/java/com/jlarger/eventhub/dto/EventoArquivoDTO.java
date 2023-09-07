package com.jlarger.eventhub.dto;

import java.io.Serializable;

import com.jlarger.eventhub.entities.EventoArquivo;

public class EventoArquivoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	private ArquivoDTO arquivo;
	
	public EventoArquivoDTO() {
	}

	public EventoArquivoDTO(Long id, ArquivoDTO arquivo) {
		this.id = id;
		this.arquivo = arquivo;
	}
	
	public EventoArquivoDTO(EventoArquivo eventoArquivo) {
		this.id = eventoArquivo.getId();
		this.arquivo = new ArquivoDTO(eventoArquivo.getArquivo());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ArquivoDTO getArquivo() {
		return arquivo;
	}

	public void setArquivo(ArquivoDTO arquivo) {
		this.arquivo = arquivo;
	}

}