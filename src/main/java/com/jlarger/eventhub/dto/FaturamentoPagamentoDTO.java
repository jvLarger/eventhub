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
	private String email;
	private String account;
	private Boolean payoutsEnabled;
	private String linkConnectStripe;
	private List<FaturamentoDTO> proximosFaturamentos = new ArrayList<FaturamentoDTO>();
	private List<FaturamentoDTO> faturamentosLiberados = new ArrayList<FaturamentoDTO>();
	private List<FaturamentoDTO> faturamentosPagos = new ArrayList<FaturamentoDTO>();

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

	public List<FaturamentoDTO> getProximosFaturamentos() {
		return proximosFaturamentos;
	}

	public void setProximosFaturamentos(List<FaturamentoDTO> proximosFaturamentos) {
		this.proximosFaturamentos = proximosFaturamentos;
	}

	public List<FaturamentoDTO> getFaturamentosLiberados() {
		return faturamentosLiberados;
	}

	public void setFaturamentosLiberados(List<FaturamentoDTO> faturamentosLiberados) {
		this.faturamentosLiberados = faturamentosLiberados;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Boolean getPayoutsEnabled() {
		return payoutsEnabled;
	}

	public void setPayoutsEnabled(Boolean payoutsEnabled) {
		this.payoutsEnabled = payoutsEnabled;
	}

	public String getLinkConnectStripe() {
		return linkConnectStripe;
	}

	public void setLinkConnectStripe(String linkConnectStripe) {
		this.linkConnectStripe = linkConnectStripe;
	}

	public List<FaturamentoDTO> getFaturamentosPagos() {
		return faturamentosPagos;
	}

	public void setFaturamentosPagos(List<FaturamentoDTO> faturamentosPagos) {
		this.faturamentosPagos = faturamentosPagos;
	}

}