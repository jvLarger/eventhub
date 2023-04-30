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
@Table(name = "token")
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable=false)
    private Usuario usuario;
	
	@Column(nullable=false, length = 5)
	private Integer codigo;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=false)
    private LocalDateTime dataExpiracao;
	
	public Token() {
	}

	public Token(Long id, Usuario usuario, Integer codigo, LocalDateTime dataExpiracao) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.codigo = codigo;
		this.dataExpiracao = dataExpiracao;
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

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public LocalDateTime getDataExpiracao() {
		return dataExpiracao;
	}

	public void setDataExpiracao(LocalDateTime dataExpiracao) {
		this.dataExpiracao = dataExpiracao;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo, dataExpiracao, id, usuario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		return Objects.equals(codigo, other.codigo) && Objects.equals(dataExpiracao, other.dataExpiracao)
				&& Objects.equals(id, other.id) && Objects.equals(usuario, other.usuario);
	}
	
}