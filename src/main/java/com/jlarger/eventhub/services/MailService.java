package com.jlarger.eventhub.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class MailService {

	@Autowired
	private SendGrid sendGrid;

	public void enviarEmail(String emailDestino, String titulo, String corpoEmail) {

		Mail mail = new Mail(
			new Email("jviclarger@gmail.com"), 
			titulo,
			new Email(emailDestino), 
			new Content("text/plain", corpoEmail)
		);
		
		mail.setReplyTo(new Email("jviclarger@gmail.com"));
		
		Request request = new Request();
		
		try {
			
			request.setMethod(Method.POST);

			request.setEndpoint("mail/send");

			request.setBody(mail.build());

			sendGrid.api(request);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}