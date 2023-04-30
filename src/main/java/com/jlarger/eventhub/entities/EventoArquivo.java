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
@Table(name = "evento_arquivo")
public class EventoArquivo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", nullable=true)
    private Evento evento;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_arquivo", nullable=true)
    private Arquivo arquivo;
	
	public EventoArquivo() {
	}

	public EventoArquivo(Long id, Evento evento, Arquivo arquivo) {
		super();
		this.id = id;
		this.evento = evento;
		this.arquivo = arquivo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(arquivo, evento, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventoArquivo other = (EventoArquivo) obj;
		return Objects.equals(arquivo, other.arquivo) && Objects.equals(evento, other.evento)
				&& Objects.equals(id, other.id);
	}
	
}