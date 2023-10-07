package com.jlarger.eventhub.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

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

}