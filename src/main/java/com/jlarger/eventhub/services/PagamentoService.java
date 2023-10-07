package com.jlarger.eventhub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlarger.eventhub.dto.PagamentoDTO;
import com.jlarger.eventhub.entities.Ingresso;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.CpfCnpjValidate;
import com.jlarger.eventhub.utils.Util;

@Service
public class PagamentoService {
	
	@Autowired
	private StripeService stripeService;
	
	public void estornarPagamentoIngresso(Ingresso ingresso) {
		// TODO implementar a regra de negócio para estornar um pagamento de ingresso no getway de pagamento
 	}

	public void validarCamposPagamentoCartao(PagamentoDTO pagamento) {
		
		if (pagamento.getToken() == null) {
			throw new BusinessException("Token inválido. Por favor, verifique!");
		}
		
	}

	public Double calcularValorTaxaIngresso(Double valor) {
		
		Double valorTaxa = (valor / 100.0) * 4.0;

		return valorTaxa;
	}

	public String realizarPagamentoCartao(PagamentoDTO pagamento, Double valor) {
		
		long valorEmCentavos = (long)(valor * 100.0);
		
		String paymentIntentId = stripeService.createPayment(pagamento.getToken(), valorEmCentavos);
		
		return paymentIntentId;
	}

}