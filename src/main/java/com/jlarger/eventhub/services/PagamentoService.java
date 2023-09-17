package com.jlarger.eventhub.services;

import org.springframework.stereotype.Service;

import com.jlarger.eventhub.dto.PagamentoDTO;
import com.jlarger.eventhub.entities.Ingresso;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.CpfCnpjValidate;
import com.jlarger.eventhub.utils.Util;

@Service
public class PagamentoService {

	public void estornarPagamentoIngresso(Ingresso ingresso) {
		// TODO implementar a regra de negócio para estornar um pagamento de ingresso no getway de pagamento
 	}

	public void validarCamposPagamentoCartao(PagamentoDTO pagamento) {
		
		if (pagamento.getNomeTitular() == null || pagamento.getNomeTitular().trim().isEmpty()) {
			throw new BusinessException("Nome do Titular não informado!");
		}
		
		if (pagamento.getCvv() == null || pagamento.getCvv().trim().isEmpty()) {
			throw new BusinessException("CVV não informado!");
		}
		
		if (pagamento.getValidade() == null || pagamento.getValidade().trim().isEmpty()) {
			throw new BusinessException("Validade não informado!");
		}
		
		if (pagamento.getDocumentoPrincipal() == null || pagamento.getDocumentoPrincipal().isEmpty()) {
			throw new BusinessException("Documento não informado!");
		}

		String documento = Util.getSomenteNumeros(pagamento.getDocumentoPrincipal());

		if (documento.length() != 11 && documento.length() != 14) {
			throw new BusinessException("CPF deve possui 11 dígitos e CNPJ 14. Por favor, verifique!");
		}
		
		if (documento.length() == 11 && !CpfCnpjValidate.isCpfValid(documento)) {
			throw new BusinessException("CPF inválido. Por favor, verifique!");
		}
		
		if (documento.length() == 14 && !CpfCnpjValidate.isCnpjValid(documento)) {
			throw new BusinessException("CNPJ inválido. Por favor, verifique!");
		}
		
	}

	public Double calcularValorTaxaIngresso(Double valor) {
		
		Double valorTaxa = (valor / 100.0) * 4.0;

		return valorTaxa;
	}

	public String realizarPagamentoCartao(PagamentoDTO pagamento, Double valor) {
		return "test";
	}

}