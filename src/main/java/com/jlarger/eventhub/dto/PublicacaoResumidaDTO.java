package com.jlarger.eventhub.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PublicacaoResumidaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String descricao;
	private LocalDateTime data;
	private ArquivoDTO fotoPrincipal;
	
	public PublicacaoResumidaDTO() {
	}
	
	public PublicacaoResumidaDTO(Long id, String descricao, LocalDateTime data, ArquivoDTO fotoPrincipal) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.data = data;
		this.fotoPrincipal = fotoPrincipal;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public LocalDateTime getData() {
		return data;
	}
	public void setData(LocalDateTime data) {
		this.data = data;
	}
	public ArquivoDTO getFotoPrincipal() {
		return fotoPrincipal;
	}
	public void setFotoPrincipal(ArquivoDTO fotoPrincipal) {
		this.fotoPrincipal = fotoPrincipal;
	}
}