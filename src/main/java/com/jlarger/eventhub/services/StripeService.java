package com.jlarger.eventhub.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;

@Service
public class StripeService {

	@Value("${stripe.key.secret}")
	private String API_SECET_KEY;
	
	private static final String CURRENCY = "BRL";
	private static final Logger log = LoggerFactory.getLogger(StripeService.class);
	
    public String createPayment(String token, Long amount) {
       
       Stripe.apiKey = API_SECET_KEY;

       try {
    	   PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
    	            .setAmount(amount)  
    	            .setPaymentMethod(token)
    	            .setCurrency(CURRENCY)
    	            .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.AUTOMATIC)
    	            .setConfirm(true)
    	            .setReturnUrl("https://eventhub.com.br/retorno")
    	            .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);
    	            
            return paymentIntent.getId();    
    	   
        } catch (Exception e) {
            log.error("Erro ao processar o pagamento: " + e.getMessage());
        	throw new BusinessException("Erro ao processar o pagamento: " + e.getMessage());
        }
    }

	public Boolean paymentSuccess(String indificadorPagamento) {
		
		Stripe.apiKey = API_SECET_KEY;
		
		Boolean isPagamentoBemSucedido = Boolean.FALSE;
		
		try {

			PaymentIntent paymentIntent = PaymentIntent.retrieve(indificadorPagamento);

			String paymentStatus = paymentIntent.getStatus();

			if ("succeeded".equals(paymentStatus)) {
				isPagamentoBemSucedido = Boolean.TRUE;
			}

		} catch (Exception e) {
			log.error("Erro ao processar o pagamento: " + e.getMessage());
			throw new BusinessException("Erro ao processar o pagamento: " + e.getMessage());
		}
		
		return isPagamentoBemSucedido;
	}

	public Boolean refund(Long valor, String identificadorTransacaoPagamento) {
	
		Stripe.apiKey = API_SECET_KEY;
		
		Boolean isSuccess = Boolean.FALSE;
		
		try {
			RefundCreateParams params = RefundCreateParams.builder()
					.setPaymentIntent(identificadorTransacaoPagamento)
					.setAmount(valor)
					.build();

			Refund refund = Refund.create(params);

			if ("succeeded".equals(refund.getStatus())) {
				isSuccess = Boolean.TRUE;
			}
			
		} catch (Exception e) {
			log.error("Erro ao processar o pagamento: " + e.getMessage());
			throw new BusinessException("Erro ao processar o pagamento: " + e.getMessage());
		}
		
		return isSuccess;
	}
	
}