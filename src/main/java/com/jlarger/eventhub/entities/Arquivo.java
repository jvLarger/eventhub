package com.jlarger.eventhub.entities;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "arquivo")
public class Arquivo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	@Column(nullable = false, length = 255)
	private String nome;
	
	@Column(nullable = false, length = 255)
	private String nomeAbsoluto;
	
	@Column(nullable = true, length = 255)
	private String descricao;
	
	public Arquivo() {
	}

	public Arquivo(Long id, String nomeAbsoluto, String nome, String descricao) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.nomeAbsoluto = nomeAbsoluto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeAbsoluto() {
		return nomeAbsoluto;
	}

	public void setNomeAbsoluto(String nomeAbsoluto) {
		this.nomeAbsoluto = nomeAbsoluto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
		Arquivo other = (Arquivo) obj;
		return Objects.equals(descricao, other.descricao) && Objects.equals(id, other.id)
				&& Objects.equals(nome, other.nome) && Objects.equals(nomeAbsoluto, other.nomeAbsoluto);
	}
	
}