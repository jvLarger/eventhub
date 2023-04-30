package com.jlarger.eventhub.entities;

import java.time.LocalDateTime;
import java.util.Objects;

import com.jlarger.eventhub.entities.type.TipoNotificacao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "notificacao")
public class Notificacao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_origem", nullable=true)
    private Usuario usuarioOrigem;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_destino", nullable=false)
    private Usuario usuarioDestino;
	
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=false)
    private LocalDateTime dataNotificacao;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=true)
    private LocalDateTime dataLeitura;
    
    @Column(columnDefinition = "TEXT", nullable=true)
	private String descricao;
    
	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private TipoNotificacao tipo;
	
	public Notificacao() {
	}

	public Notificacao(Long id, Usuario usuarioOrigem, Usuario usuarioDestino, LocalDateTime dataNotificacao,
			LocalDateTime dataLeitura, String descricao, TipoNotificacao tipo) {
		super();
		this.id = id;
		this.usuarioOrigem = usuarioOrigem;
		this.usuarioDestino = usuarioDestino;
		this.dataNotificacao = dataNotificacao;
		this.dataLeitura = dataLeitura;
		this.descricao = descricao;
		this.tipo = tipo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuarioOrigem() {
		return usuarioOrigem;
	}

	public void setUsuarioOrigem(Usuario usuarioOrigem) {
		this.usuarioOrigem = usuarioOrigem;
	}

	public Usuario getUsuarioDestino() {
		return usuarioDestino;
	}

	public void setUsuarioDestino(Usuario usuarioDestino) {
		this.usuarioDestino = usuarioDestino;
	}

	public LocalDateTime getDataNotificacao() {
		return dataNotificacao;
	}

	public void setDataNotificacao(LocalDateTime dataNotificacao) {
		this.dataNotificacao = dataNotificacao;
	}

	public LocalDateTime getDataLeitura() {
		return dataLeitura;
	}

	public void setDataLeitura(LocalDateTime dataLeitura) {
		this.dataLeitura = dataLeitura;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoNotificacao getTipo() {
		return tipo;
	}

	public void setTipo(TipoNotificacao tipo) {
		this.tipo = tipo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataLeitura, dataNotificacao, descricao, id, tipo, usuarioDestino, usuarioOrigem);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Notificacao other = (Notificacao) obj;
		return Objects.equals(dataLeitura, other.dataLeitura) && Objects.equals(dataNotificacao, other.dataNotificacao)
				&& Objects.equals(descricao, other.descricao) && Objects.equals(id, other.id) && tipo == other.tipo
				&& Objects.equals(usuarioDestino, other.usuarioDestino)
				&& Objects.equals(usuarioOrigem, other.usuarioOrigem);
	}
	
}