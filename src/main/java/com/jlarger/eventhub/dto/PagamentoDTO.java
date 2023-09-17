package com.jlarger.eventhub.dto;

import java.io.Serializable;

public class PagamentoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String numero;
	private String nomeTitular;
	private String documentoPrincipal;
	private String validade;
	private String cvv;
	private String tipoCartao;
	
	public PagamentoDTO() {
	}

	public PagamentoDTO(String numero, String nomeTitular, String documentoPrincipal, String validade, String cvv, String tipoCartao) {
		this.numero = numero;
		this.nomeTitular = nomeTitular;
		this.documentoPrincipal = documentoPrincipal;
		this.validade = validade;
		this.cvv = cvv;
		this.tipoCartao = tipoCartao;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getNomeTitular() {
		return nomeTitular;
	}

	public void setNomeTitular(String nomeTitular) {
		this.nomeTitular = nomeTitular;
	}

	public String getDocumentoPrincipal() {
		return documentoPrincipal;
	}

	public void setDocumentoPrincipal(String documentoPrincipal) {
		this.documentoPrincipal = documentoPrincipal;
	}

	public String getValidade() {
		return validade;
	}

	public void setValidade(String validade) {
		this.validade = validade;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getTipoCartao() {
		return tipoCartao;
	}

	public void setTipoCartao(String tipoCartao) {
		this.tipoCartao = tipoCartao;
	}
}