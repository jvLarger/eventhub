package com.jlarger.eventhub.dto;

import java.io.Serializable;
import java.util.Objects;

import com.jlarger.eventhub.entities.Arquivo;

public class ArquivoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nome;
	private String caminhoAbsoluto;
	private String descricao;
	
	public ArquivoDTO() {
	}

	public ArquivoDTO(Long id, String nome, String caminhoAbsoluto, String descricao) {
		this.id = id;
		this.nome = nome;
		this.caminhoAbsoluto = caminhoAbsoluto;
		this.descricao = descricao;
	}
	
	public ArquivoDTO(Arquivo arquivo) {
		this.id = arquivo.getId();
		this.nome = arquivo.getNome();
		this.caminhoAbsoluto = arquivo.getCaminhoAbsoluto();
		this.descricao = arquivo.getDescricao();
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

	public String getCaminhoAbsoluto() {
		return caminhoAbsoluto;
	}

	public void setCaminhoAbsoluto(String caminhoAbsoluto) {
		this.caminhoAbsoluto = caminhoAbsoluto;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(caminhoAbsoluto, descricao, id, nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArquivoDTO other = (ArquivoDTO) obj;
		return Objects.equals(caminhoAbsoluto, other.caminhoAbsoluto) && Objects.equals(descricao, other.descricao)
				&& Objects.equals(id, other.id) && Objects.equals(nome, other.nome);
	}
	
}