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
@Table(name = "evento_categoria")
public class EventoCategoria {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", nullable=true)
    private Evento evento;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable=true)
    private Categoria categoria;
	
	public EventoCategoria() {
	}

	public EventoCategoria(Long id, Evento evento, Categoria categoria) {
		super();
		this.id = id;
		this.evento = evento;
		this.categoria = categoria;
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

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@Override
	public int hashCode() {
		return Objects.hash(categoria, evento, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventoCategoria other = (EventoCategoria) obj;
		return Objects.equals(categoria, other.categoria) && Objects.equals(evento, other.evento)
				&& Objects.equals(id, other.id);
	}

}