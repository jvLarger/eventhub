package com.jlarger.eventhub.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FaturamentoPagamentoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Double valorTotalIngressos;
	private Double valorTotalTaxas;
	private Double valorTotalFaturado;
	private Double valorTotalIngressosFuturo;
	private Double valorTotalTaxasFuturo;
	private Double valorTotalFaturadoFuturo;
	private String numeroContaBancaria;
	private String numeroBanco;
	private String nomeTitular;
	private List<FaturamentoDTO> proximosFaturamentos = new ArrayList<FaturamentoDTO>();
	
	public FaturamentoPagamentoDTO() {
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

	public Double getValorTotalFaturado() {
		return valorTotalFaturado;
	}

	public void setValorTotalFaturado(Double valorTotalFaturado) {
		this.valorTotalFaturado = valorTotalFaturado;
	}

	public Double getValorTotalIngressosFuturo() {
		return valorTotalIngressosFuturo;
	}

	public void setValorTotalIngressosFuturo(Double valorTotalIngressosFuturo) {
		this.valorTotalIngressosFuturo = valorTotalIngressosFuturo;
	}

	public Double getValorTotalTaxasFuturo() {
		return valorTotalTaxasFuturo;
	}

	public void setValorTotalTaxasFuturo(Double valorTotalTaxasFuturo) {
		this.valorTotalTaxasFuturo = valorTotalTaxasFuturo;
	}

	public Double getValorTotalFaturadoFuturo() {
		return valorTotalFaturadoFuturo;
	}

	public void setValorTotalFaturadoFuturo(Double valorTotalFaturadoFuturo) {
		this.valorTotalFaturadoFuturo = valorTotalFaturadoFuturo;
	}

	public String getNumeroContaBancaria() {
		return numeroContaBancaria;
	}

	public void setNumeroContaBancaria(String numeroContaBancaria) {
		this.numeroContaBancaria = numeroContaBancaria;
	}

	public String getNumeroBanco() {
		return numeroBanco;
	}

	public void setNumeroBanco(String numeroBanco) {
		this.numeroBanco = numeroBanco;
	}

	public String getNomeTitular() {
		return nomeTitular;
	}

	public void setNomeTitular(String nomeTitular) {
		this.nomeTitular = nomeTitular;
	}

	public List<FaturamentoDTO> getProximosFaturamentos() {
		return proximosFaturamentos;
	}

	public void setProximosFaturamentos(List<FaturamentoDTO> proximosFaturamentos) {
		this.proximosFaturamentos = proximosFaturamentos;
	}
	
}