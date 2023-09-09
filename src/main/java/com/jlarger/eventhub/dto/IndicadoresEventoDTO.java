package com.jlarger.eventhub.dto;

import java.io.Serializable;

public class IndicadoresEventoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private EventoDTO evento;
	private FaturamentoDTO faturamento;
	
	public IndicadoresEventoDTO() {
	}

	public EventoDTO getEvento() {
		return evento;
	}

	public void setEvento(EventoDTO evento) {
		this.evento = evento;
	}

	public FaturamentoDTO getFaturamento() {
		return faturamento;
	}

	public void setFaturamento(FaturamentoDTO faturamento) {
		this.faturamento = faturamento;
	}
	
}