package com.jlarger.eventhub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlarger.eventhub.dto.PagamentoDTO;
import com.jlarger.eventhub.entities.Ingresso;
import com.jlarger.eventhub.services.exceptions.BusinessException;

@Service
public class PagamentoService {
	
	@Autowired
	private StripeService stripeService;
	
	public Boolean estornarPagamentoIngresso(Ingresso ingresso) {
		
		long valorEmCentavos = (long)(ingresso.getValorTotalIngresso() * 100.0);
		
		Boolean isSuccess = stripeService.refund(valorEmCentavos, ingresso.getIdentificadorTransacaoPagamento());
		
		return isSuccess;
 	}

	public void validarCamposPagamentoCartao(PagamentoDTO pagamento) {
		
		if (pagamento.getToken() == null) {
			throw new BusinessException("Token inválido. Por favor, verifique!");
		}
		
	}

	public Double calcularValorTaxaIngresso(Double valor) {
		
		Double valorTaxa = ((valor / 100.0) * 4.5) + 0.39;

		return valorTaxa;
	}

	public String realizarPagamentoCartao(PagamentoDTO pagamento, Double valor) {
		
		long valorEmCentavos = (long)(valor * 100.0);
		
		String paymentIntentId = stripeService.createPayment(pagamento.getToken(), valorEmCentavos);
		
		return paymentIntentId;
	}

	public Boolean verificarSePagamentoBemSucedido(String indificadorPagamento) {
		
		Boolean isPagamentoBemSucedido = stripeService.paymentSuccess(indificadorPagamento);
		
		return isPagamentoBemSucedido;
	}

}