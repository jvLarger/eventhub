package com.jlarger.eventhub.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.jlarger.eventhub.entities.Evento;
import com.jlarger.eventhub.entities.Faturamento;

public class FaturamentoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
    private EventoDTO evento;
	private Double valorTotalIngressos;
	private Double valorTotalTaxas;
	private Double valorTotalFaturamento;
    private LocalDateTime dataLiberacao;
    private LocalDateTime dataPagamento;
    
    public FaturamentoDTO() {
    }
    
	public FaturamentoDTO(Long id, EventoDTO evento, Double valorTotalIngressos, Double valorTotalTaxas, Double valorTotalFaturamento, LocalDateTime dataLiberacao, LocalDateTime dataPagamento) {
		this.id = id;
		this.evento = evento;
		this.valorTotalIngressos = valorTotalIngressos;
		this.valorTotalTaxas = valorTotalTaxas;
		this.valorTotalFaturamento = valorTotalFaturamento;
		this.dataLiberacao = dataLiberacao;
		this.dataPagamento = dataPagamento;
	}

	public FaturamentoDTO(Faturamento faturamento) {
		this.id = faturamento.getId();
		this.valorTotalIngressos = faturamento.getValorTotalIngressos();
		this.valorTotalTaxas = faturamento.getValorTotalTaxas();
		this.valorTotalFaturamento = faturamento.getValorTotalFaturamento();
		this.dataLiberacao = faturamento.getDataLiberacao();
		this.dataPagamento = faturamento.getDataPagamento();
	}
	
	public FaturamentoDTO(Faturamento faturamento, Evento evento) {
		this(faturamento);
		this.evento = new EventoDTO(evento);
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EventoDTO getEvento() {
		return evento;
	}

	public void setEvento(EventoDTO evento) {
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
    
}