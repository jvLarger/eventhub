package com.jlarger.eventhub.entities;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "publicacao_arquivo")
public class PublicacaoArquivo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_publicacao", nullable=false)
    private Publicacao publicacao;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_arquivo", nullable=false)
    private Arquivo arquivo;
	
	public PublicacaoArquivo() {
	}

	public PublicacaoArquivo(Long id, Publicacao publicacao, Arquivo arquivo) {
		super();
		this.id = id;
		this.publicacao = publicacao;
		this.arquivo = arquivo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Publicacao getPublicacao() {
		return publicacao;
	}

	public void setPublicacao(Publicacao publicacao) {
		this.publicacao = publicacao;
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(arquivo, id, publicacao);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PublicacaoArquivo other = (PublicacaoArquivo) obj;
		return Objects.equals(arquivo, other.arquivo) && Objects.equals(id, other.id)
				&& Objects.equals(publicacao, other.publicacao);
	}
	
}
