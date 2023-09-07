package com.jlarger.eventhub.dto;

import java.io.Serializable;

import com.jlarger.eventhub.entities.Categoria;

public class CategoriaDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nome;
	private String icone;
	
	public CategoriaDTO() {
	}

	public CategoriaDTO(Long id, String nome, String icone) {
		this.id = id;
		this.nome = nome;
		this.icone = icone;
	}
	
	public CategoriaDTO(Categoria categoria) {
		this.id = categoria.getId();
		this.nome = categoria.getNome();
		this.icone = categoria.getIcone();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIcone() {
		return icone;
	}

	public void setIcone(String icone) {
		this.icone = icone;
	}
	
}