package com.jlarger.eventhub.entities;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "publicacao_comentario")
public class PublicacaoComentario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_publicacao", nullable=true)
    private Publicacao publicacao;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable=false)
    private Usuario usuario;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=false)
    private LocalDateTime data;
	
	@Column(columnDefinition = "TEXT", nullable=true, length = 1000)
	private String descricao;
	
	public PublicacaoComentario() {
	}

	public PublicacaoComentario(Long id, Publicacao publicacao, Usuario usuario, LocalDateTime data, String descricao) {
		super();
		this.id = id;
		this.publicacao = publicacao;
		this.usuario = usuario;
		this.data = data;
		this.descricao = descricao;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public int hashCode() {
		return Objects.hash(data, descricao, id, publicacao, usuario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PublicacaoComentario other = (PublicacaoComentario) obj;
		return Objects.equals(data, other.data) && Objects.equals(descricao, other.descricao)
				&& Objects.equals(id, other.id) && Objects.equals(publicacao, other.publicacao)
				&& Objects.equals(usuario, other.usuario);
	}

}