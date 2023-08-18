package com.jlarger.eventhub.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.jlarger.eventhub.dto.TokenDTO;
import com.jlarger.eventhub.entities.Token;
import com.jlarger.eventhub.entities.Usuario;
import com.jlarger.eventhub.repositories.TokenRepository;
import com.jlarger.eventhub.repositories.UsuarioRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.Util;

@Service
public class TokenService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Autowired
	private MailService mailService;
	
	public TokenDTO enviarCodigoRecuperacao(TokenDTO tokenDTO) {

		validarEmailInformado(tokenDTO);

		Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(tokenDTO.getUsuario().getEmail());
		
		TokenDTO tokenEnviado = new TokenDTO();
		
		if (optionalUsuario.isPresent()) {
			
			Usuario usuario = optionalUsuario.get();
			
			Token token = new Token();
			token.setUsuario(usuario);
			token.setCodigo(new Random().nextInt(1, 9999));
			token.setDataExpiracao(getDataExpiracao());
			
			token = tokenRepository.save(token);

			mailService.enviarEmail(usuario.getEmail(), "Event Hub - Recuperação de Senha", getBodyEmailRecuperacao(usuario, token));
			
			tokenEnviado.setDataExpiracao(token.getDataExpiracao()); 
			
		}
		
		return tokenEnviado;
	}

	private String getBodyEmailRecuperacao(Usuario usuario, Token token) {
		
		String corpo = "Olá " + usuario.getNomeCompleto() + "!<br>Abaixo o código que deverá ser utilizado para redefinir a senha de sua conta: " + Util.leftPad(token.getCodigo().toString(), 4, '0');
		
		return corpo;
	}

	private LocalDateTime getDataExpiracao() {
		
		LocalDateTime dataExpiracao = LocalDateTime.now();
		dataExpiracao = dataExpiracao.plusMinutes(10);
		
		return dataExpiracao;
	}

	private void validarEmailInformado(TokenDTO tokenDTO) {

		if (tokenDTO.getUsuario() == null || tokenDTO.getUsuario().getEmail() == null) {
			throw new BusinessException("E-mail deve ser informado. Por favor, verifique!");
		}

	}
	public TokenDTO validarCodigoRecuperacao(Integer codigo, String email) {
		
		validarCodigoEEmailInformados(codigo, email);
		
		Optional<Token> optionalToken = tokenRepository.findByEmailAndCodigo(email, codigo);
		
		if (optionalToken.isEmpty()) {
			throw new BusinessException("Código inválido. Por favor, verifique!");
		}
		
		Token token = optionalToken.get();
		
		validarDataExpiracao(token);
		
		return new TokenDTO(token);
	}

	private void validarDataExpiracao(Token token) {
		
		if (token.getDataExpiracao().compareTo(LocalDateTime.now()) < 0 ) {
			throw new BusinessException("Código expirado. Por favor, verifique!");
		}
		
	}

	private void validarCodigoEEmailInformados(Integer codigo, String email) {
		
		if (codigo == null) {
			throw new BusinessException("Código não informado. Por favor, verifique!");
		}
		
		if (email == null || email.trim().isEmpty()) {
			throw new BusinessException("E-mail não informado. Por favor, verifique!");
		}
		
	}

}
