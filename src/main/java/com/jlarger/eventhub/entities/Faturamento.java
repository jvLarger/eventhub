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
@Table(name = "faturamento")
public class Faturamento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", nullable=false)
    private Evento evento;
	
	@Column(nullable=false)
	private Double valorTotalIngressos;
	
	@Column(nullable=false)
	private Double valorTotalTaxas;
	
	@Column(nullable=false)
	private Double valorTotalFaturamento;
	
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=false)
    private LocalDateTime dataLiberacao;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=true)
    private LocalDateTime dataPagamento;
	
    public Faturamento() {
	}

	public Faturamento(Long id, Evento evento, Double valorTotalIngressos, Double valorTotalTaxas,
			Double valorTotalFaturamento, LocalDateTime dataLiberacao, LocalDateTime dataPagamento) {
		super();
		this.id = id;
		this.evento = evento;
		this.valorTotalIngressos = valorTotalIngressos;
		this.valorTotalTaxas = valorTotalTaxas;
		this.valorTotalFaturamento = valorTotalFaturamento;
		this.dataLiberacao = dataLiberacao;
		this.dataPagamento = dataPagamento;
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

	public Double getValorTotalIngressos() {
		return valorTotalIngressos;
	}

	public void setValorTotalIngressos(Double valorTotalIngressos) {
		this.valorTotalIngressos = valorTotalIngressos;
	}

	public Double getValorTotalTaxas() {
		return valorTotalTaxas;
	}

	public void setValorTotalTaxas(Double valorTotalTaxas) {
		this.valorTotalTaxas = valorTotalTaxas;
	}

	public Double getValorTotalFaturamento() {
		return valorTotalFaturamento;
	}

	public void setValorTotalFaturamento(Double valorTotalFaturamento) {
		this.valorTotalFaturamento = valorTotalFaturamento;
	}

	public LocalDateTime getDataLiberacao() {
		return dataLiberacao;
	}

	public void setDataLiberacao(LocalDateTime dataLiberacao) {
		this.dataLiberacao = dataLiberacao;
	}

	public LocalDateTime getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDateTime dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataLiberacao, dataPagamento, evento, id, valorTotalFaturamento, valorTotalIngressos,
				valorTotalTaxas);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Faturamento other = (Faturamento) obj;
		return Objects.equals(dataLiberacao, other.dataLiberacao) && Objects.equals(dataPagamento, other.dataPagamento)
				&& Objects.equals(evento, other.evento) && Objects.equals(id, other.id)
				&& Objects.equals(valorTotalFaturamento, other.valorTotalFaturamento)
				&& Objects.equals(valorTotalIngressos, other.valorTotalIngressos)
				&& Objects.equals(valorTotalTaxas, other.valorTotalTaxas);
	}
    
}