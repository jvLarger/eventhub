package com.jlarger.eventhub.dto;

import java.io.Serializable;

import com.jlarger.eventhub.entities.EventoCategoria;

public class EventoCategoriaDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	private CategoriaDTO categoria;
	
	public EventoCategoriaDTO() {
	}

	public EventoCategoriaDTO(Long id, CategoriaDTO categoria) {
		this.id = id;
		this.categoria = categoria;
	}
	
	public EventoCategoriaDTO(EventoCategoria eventoCategoria) {
		this.id = eventoCategoria.getId();
		this.categoria = new CategoriaDTO(eventoCategoria.getCategoria());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CategoriaDTO getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaDTO categoria) {
		this.categoria = categoria;
	}
	
}