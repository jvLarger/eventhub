package com.jlarger.eventhub.dto;

import java.io.Serializable;

import com.jlarger.eventhub.entities.PublicacaoArquivo;

public class PublicacaArquivoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
    private ArquivoDTO arquivo;
	
    public PublicacaArquivoDTO() {
    }

	public PublicacaArquivoDTO(Long id, ArquivoDTO arquivo) {
		this.id = id;
		this.arquivo = arquivo;
	}
    
	public PublicacaArquivoDTO(PublicacaoArquivo publicacaoArquivo) {
		this.id = publicacaoArquivo.getId();
		this.arquivo = new ArquivoDTO(publicacaoArquivo.getArquivo());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ArquivoDTO getArquivo() {
		return arquivo;
	}

	public void setArquivo(ArquivoDTO arquivo) {
		this.arquivo = arquivo;
	}
	
}
