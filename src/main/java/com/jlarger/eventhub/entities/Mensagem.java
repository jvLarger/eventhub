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
@Table(name = "mensagem")
public class Mensagem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_origem", nullable=false)
    private Usuario usuarioOrigem;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_destino", nullable=false)
    private Usuario usuarioDestino;
	
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=false)
    private LocalDateTime dataMensagem;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=true)
    private LocalDateTime dataLeitura;
    
    @Column(columnDefinition = "TEXT", nullable=true, length = 1000)
	private String descricao;
    
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", nullable=true)
    private Evento evento;
	
	public Mensagem() {
	}

	public Mensagem(Long id, Usuario usuarioOrigem, Usuario usuarioDestino, LocalDateTime dataMensagem,
			LocalDateTime dataLeitura, String descricao, Evento evento) {
		super();
		this.id = id;
		this.usuarioOrigem = usuarioOrigem;
		this.usuarioDestino = usuarioDestino;
		this.dataMensagem = dataMensagem;
		this.dataLeitura = dataLeitura;
		this.descricao = descricao;
		this.evento = evento;
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

	public LocalDateTime getDataMensagem() {
		return dataMensagem;
	}

	public void setDataMensagem(LocalDateTime dataMensagem) {
		this.dataMensagem = dataMensagem;
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

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataLeitura, dataMensagem, descricao, evento, id, usuarioDestino, usuarioOrigem);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mensagem other = (Mensagem) obj;
		return Objects.equals(dataLeitura, other.dataLeitura) && Objects.equals(dataMensagem, other.dataMensagem)
				&& Objects.equals(descricao, other.descricao) && Objects.equals(evento, other.evento)
				&& Objects.equals(id, other.id) && Objects.equals(usuarioDestino, other.usuarioDestino)
				&& Objects.equals(usuarioOrigem, other.usuarioOrigem);
	}

}