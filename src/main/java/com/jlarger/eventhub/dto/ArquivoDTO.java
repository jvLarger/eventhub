package com.jlarger.eventhub.dto;

import java.io.Serializable;
import java.util.Objects;

import com.jlarger.eventhub.entities.Arquivo;

public class ArquivoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nome;
	private String nomeAbsoluto;
	private String descricao;
	private String base64;

	public ArquivoDTO() {
	}

	public ArquivoDTO(Long id, String nome, String nomeAbsoluto, String descricao, String base64) {
		this.id = id;
		this.nome = nome;
		this.nomeAbsoluto = nomeAbsoluto;
		this.descricao = descricao;
	}
	
	public ArquivoDTO(Arquivo arquivo) {
		this.id = arquivo.getId();
		this.nome = arquivo.getNome();
		this.nomeAbsoluto = arquivo.getNomeAbsoluto();
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

	public String getNomeAbsoluto() {
		return nomeAbsoluto;
	}

	public void setNomeAbsoluto(String nomeAbsoluto) {
		this.nomeAbsoluto = nomeAbsoluto;
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
	
	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	@Override
	public int hashCode() {
		return Objects.hash(descricao, id, nome, nomeAbsoluto);
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
		return Objects.equals(descricao, other.descricao) && Objects.equals(id, other.id)
				&& Objects.equals(nome, other.nome) && Objects.equals(nomeAbsoluto, other.nomeAbsoluto);
	}

}