package com.jlarger.eventhub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.PagamentoDTO;
import com.jlarger.eventhub.entities.Ingresso;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Transfer;

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
		
		Double valorTaxa = ((valor / 100.0) * 5.0) + 0.50;

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
	
	@Transactional
	public Account createExternalAccount() {
		
		Account account = stripeService.createExternalAccount();
		
		return account;
	}

	public AccountLink createLinkExternalAccount(String accountId) {
		
		AccountLink accountLink = stripeService.createLinkExternalAccount(accountId);
		
		return accountLink;
	}

	public Account getExternalAccount(String accountId) {
		
		Account account = stripeService.getExternalAccount(accountId);
		
		return account;
	}
	/*
	 * Vai ter que ser uma transferencia por ingresso :/
	 */
	public Transfer createTransfer(String accountId, Double valor, String paymentId) {
		
		long valorEmCentavos = (long)(valor * 100.0);
		
		Transfer transfer = stripeService.createTransfer(accountId, valorEmCentavos, paymentId);
		
		return transfer;
	}

	public PaymentIntent retrievePayment(String paymentId) {
		
		PaymentIntent paymentIntent = stripeService.retrivePayment(paymentId);
		
		return paymentIntent;
	}
	
	public void deleteAccount(String accountId) {
		
		stripeService.deleteAccount(accountId);
		
	}

}