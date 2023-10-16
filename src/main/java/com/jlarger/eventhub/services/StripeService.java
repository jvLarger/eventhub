package com.jlarger.eventhub.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.stripe.Stripe;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.model.Transfer;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.TransferCreateParams;

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

	public Account createExternalAccount() {
		
		Stripe.apiKey = API_SECET_KEY;
		
		try {
			
			AccountCreateParams params = AccountCreateParams.builder()
						.setType(AccountCreateParams.Type.STANDARD)
						.build();

			Account account = Account.create(params);

			return account;
		} catch (Exception e) {
			log.error("Erro ao criar uam conta externa: " + e.getMessage());
			throw new BusinessException("Erro ao criar uam conta externa: " + e.getMessage());
		}
		
	}
	
	public AccountLink createLinkExternalAccount(String accountId) {
		
		Stripe.apiKey = API_SECET_KEY;
		
		try {
			
			AccountLinkCreateParams params =
					  AccountLinkCreateParams.builder()
					    .setAccount(accountId)
					    .setRefreshUrl("https://eventhub.com")
					    .setReturnUrl("https://eventhub.com")
					    .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
					    .build();

			AccountLink accountLink = AccountLink.create(params);

			return accountLink;
			
		} catch (Exception e) {
			log.error("Erro ao criar o link para uma conta externa: " + e.getMessage());
			throw new BusinessException("Erro ao criar o link para uma conta externa: " + e.getMessage());
		}
		
	}

	public Account getExternalAccount(String accountId) {
		
		Stripe.apiKey = API_SECET_KEY;
		
		try {
			
			Account account = Account.retrieve(accountId);

			return account;
		} catch (Exception e) {
			log.error("Erro ao buscar conta externa: " + e.getMessage());
			throw new BusinessException("Erro ao buscar conta externa: " + e.getMessage());
		}
	}

	public Transfer createTransfer(String accountId, Long valor, String paymentId) {
		
		Stripe.apiKey = API_SECET_KEY;
		
		try {
			
			PaymentIntent paymentIntent = retrivePayment(paymentId);

			TransferCreateParams params = TransferCreateParams.builder()
				    .setAmount(valor)  
				    .setCurrency(CURRENCY)  
				    .setSourceTransaction(paymentIntent.getLatestCharge())
				    .setDestination(accountId)  
				    .build();
			
		    Transfer transfer = Transfer.create(params);
		    	return transfer;
		} catch (Exception e) {
			log.error("Erro ao criar uma transferência: " + e.getMessage());
			throw new BusinessException("Erro ao criar uma transferência: " + e.getMessage());
		}
		
	}

	public PaymentIntent retrivePayment(String paymentId) {
		
		Stripe.apiKey = API_SECET_KEY;
		
		try {
			
			PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentId);

			return paymentIntent;
			
		} catch (Exception e) {
			log.error("Erro ao buscar pagamento: " + e.getMessage());
			throw new BusinessException("Erro ao buscar pagamento: " + e.getMessage());
		}
	}
	
}