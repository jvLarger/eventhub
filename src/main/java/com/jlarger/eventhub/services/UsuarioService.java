package com.jlarger.eventhub.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.ArquivoDTO;
import com.jlarger.eventhub.dto.UsuarioAutenticadoDTO;
import com.jlarger.eventhub.dto.UsuarioDTO;
import com.jlarger.eventhub.entities.Usuario;
import com.jlarger.eventhub.repositories.UsuarioRepository;
import com.jlarger.eventhub.security.jwt.JwtUtils;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.ServiceLocator;
import com.jlarger.eventhub.utils.Util;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtils jwtUtils;

	public UsuarioAutenticadoDTO novoUsuario(UsuarioDTO dto) {
		
		validarInformacoesNovoUsuario(dto);
		
		validarUsuarioJaExistePorEmailOuNomeUsuario(dto);
		
		Usuario usuario = new Usuario();
		usuario.setNomeCompleto(dto.getNomeCompleto());
		usuario.setNomeUsuario(dto.getNomeUsuario());
		usuario.setEmail(dto.getEmail());
		usuario.setSenha(encoder.encode(dto.getSenha()));
		
		usuario = repository.save(usuario);
		
		UsuarioAutenticadoDTO usuarioAutenticadoDTO = login(dto);
		
		return usuarioAutenticadoDTO;
	}

	private void validarUsuarioJaExistePorEmailOuNomeUsuario(UsuarioDTO dto) {
		
		Boolean existsByNomeUsuario = repository.existsByNomeUsuario(dto.getNomeUsuario());
		
		Boolean existsByEmail = repository.existsByEmail(dto.getEmail());
		
		if (existsByEmail || existsByNomeUsuario) {
			throw new BusinessException("O email ou nome de usuário informado já está sendo utilizado em outro cadastro. Por favor, verifique!");
		}
		
	}

	private void validarInformacoesNovoUsuario(UsuarioDTO dto) {
		
		if (dto.getNomeCompleto() == null || dto.getNomeCompleto().trim().isEmpty()) {
			throw new BusinessException("Nome completo não informado. Por favor, verifique!");
		}
		
		validarEmail(dto.getEmail());
		
		validarSenha(dto.getSenha());
		
		validarNomeUsuario(dto.getNomeUsuario());
		
	}

	private void validarEmail(String email) {
		
		if (email == null || email.trim().isEmpty()) {
			throw new BusinessException("Email não informado. Por favor, verifique!");
		}
		
		if (!Util.isEmailValido(email)) {
			throw new BusinessException("O email informado não é válido. Por favor, verifique!");
		}
		
	}

	private void validarSenha(String senha) {
		
		if (senha == null || senha.trim().isEmpty()) {
			throw new BusinessException("Senha não informada. Por favor, verifique!");
		}
		
		if (senha.length() < 8) {
			throw new BusinessException("A senha informada deve conter pelo menos 8 caracteres. Por favor, verifique!");
		}
		
		if (!Util.isExisteNumeroNoTexto(senha)) {
			throw new BusinessException("A senha informada deve conter pelo menos um número. Por favor, verifique!");
		}
		
		if (!Util.isExisteLetraMaiusculaNoTexto(senha)) {
			throw new BusinessException("A senha informada deve conter pelo menos uma letra maiúscula. Por favor, verifique!");
		}
		
	}

	private void validarNomeUsuario(String nomeUsuario) {
		
		if (nomeUsuario == null || nomeUsuario.trim().isEmpty()) {
			throw new BusinessException("Nome de usuário não informado. Por favor, verifique!");
		}
		
		if (nomeUsuario.length() < 4) {
			throw new BusinessException("O nome de usuário informado deve conter pelo menos 4 caracteres. Por favor, verifique!");
		}
		
	}
	
	@Transactional(readOnly = true)
	public UsuarioAutenticadoDTO login(UsuarioDTO dto) {
		
		Usuario entity = repository.findByEmail(dto.getEmail()).orElseThrow(() -> new BusinessException("Email ou senha inválidos. Por favor, verifique!"));
		
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(entity.getNomeUsuario(), dto.getSenha()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = jwtUtils.generateJwtToken(authentication);
				
		UsuarioAutenticadoDTO usuarioAutenticadoDTO = new UsuarioAutenticadoDTO();
		usuarioAutenticadoDTO.setId(entity.getId());
		usuarioAutenticadoDTO.setEmail(entity.getEmail());
		usuarioAutenticadoDTO.setNomeUsuario(entity.getNomeUsuario());
		usuarioAutenticadoDTO.setNomeCompleto(entity.getNomeCompleto());
		usuarioAutenticadoDTO.setToken(jwt);
		
		if (entity.getFoto() != null) {
			usuarioAutenticadoDTO.setFoto(new ArquivoDTO(entity.getFoto()));
		}
		
		return usuarioAutenticadoDTO;
	}

	public Boolean isTokenValido(UsuarioAutenticadoDTO dto) {
		
		Boolean isTokenValido = jwtUtils.validateJwtToken(dto.getToken());
		
		return isTokenValido;
	}
	
	@Transactional(readOnly = true)
	public UsuarioDTO getUsuarioLogado() {
		
		Optional<Usuario> obj = repository.findById(ServiceLocator.getUsuarioLogado().getId());
		
		Usuario entity = obj.orElseThrow(() -> new BusinessException("Entidade não encontrada"));
		
		return new UsuarioDTO(entity);
	}
	
}