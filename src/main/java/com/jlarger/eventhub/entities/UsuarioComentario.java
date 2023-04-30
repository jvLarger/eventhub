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
@Table(name = "usuario_comentario")
public class UsuarioComentario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable=false)
    private Usuario usuario;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_origem", nullable=false)
    private Usuario usuarioOrigem;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=false)
    private LocalDateTime dataComentario;
	
	@Column(columnDefinition = "TEXT", nullable=true, length = 1000)
	private String comentario;
	
	public UsuarioComentario() {
	}

	public UsuarioComentario(Long id, Usuario usuario, Usuario usuarioOrigem, LocalDateTime dataComentario,
			String comentario) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.usuarioOrigem = usuarioOrigem;
		this.dataComentario = dataComentario;
		this.comentario = comentario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuarioOrigem() {
		return usuarioOrigem;
	}

	public void setUsuarioOrigem(Usuario usuarioOrigem) {
		this.usuarioOrigem = usuarioOrigem;
	}

	public LocalDateTime getDataComentario() {
		return dataComentario;
	}

	public void setDataComentario(LocalDateTime dataComentario) {
		this.dataComentario = dataComentario;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	@Override
	public int hashCode() {
		return Objects.hash(comentario, dataComentario, id, usuario, usuarioOrigem);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioComentario other = (UsuarioComentario) obj;
		return Objects.equals(comentario, other.comentario) && Objects.equals(dataComentario, other.dataComentario)
				&& Objects.equals(id, other.id) && Objects.equals(usuario, other.usuario)
				&& Objects.equals(usuarioOrigem, other.usuarioOrigem);
	}
	
}