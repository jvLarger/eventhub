package com.jlarger.eventhub.dto;

import java.io.Serializable;
import java.util.List;

public class FeedEventosDTO  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<EventoDTO> eventosQueMeusAmigosGostaram;
	private List<EventoDTO> eventosPopulares;
	
	public FeedEventosDTO() {
	}

	public FeedEventosDTO(List<EventoDTO> eventosQueMeusAmigosGostaram, List<EventoDTO> eventosPopulares) {
		super();
		this.eventosQueMeusAmigosGostaram = eventosQueMeusAmigosGostaram;
		this.eventosPopulares = eventosPopulares;
	}

	public List<EventoDTO> getEventosQueMeusAmigosGostaram() {
		return eventosQueMeusAmigosGostaram;
	}

	public void setEventosQueMeusAmigosGostaram(List<EventoDTO> eventosQueMeusAmigosGostaram) {
		this.eventosQueMeusAmigosGostaram = eventosQueMeusAmigosGostaram;
	}

	public List<EventoDTO> getEventosPopulares() {
		return eventosPopulares;
	}

	public void setEventosPopulares(List<EventoDTO> eventosPopulares) {
		this.eventosPopulares = eventosPopulares;
	}
}	