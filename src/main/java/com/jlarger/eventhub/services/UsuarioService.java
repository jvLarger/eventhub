package com.jlarger.eventhub.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import com.jlarger.eventhub.entities.Amizade;
import com.jlarger.eventhub.entities.Arquivo;
import com.jlarger.eventhub.entities.Notificacao;
import com.jlarger.eventhub.entities.Usuario;
import com.jlarger.eventhub.entities.type.TipoNotificacao;
import com.jlarger.eventhub.repositories.AmizadeRepository;
import com.jlarger.eventhub.repositories.ArquivoRepository;
import com.jlarger.eventhub.repositories.NotificacaoRepository;
import com.jlarger.eventhub.repositories.UsuarioRepository;
import com.jlarger.eventhub.security.jwt.JwtUtils;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.CpfCnpjValidate;
import com.jlarger.eventhub.utils.ServiceLocator;
import com.jlarger.eventhub.utils.Util;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private ArquivoRepository arquivoRepository;
	
	@Autowired
	private NotificacaoRepository notificacaoRepository;
	
	@Autowired
	private AmizadeRepository amizadeRepository;
	
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
			throw new BusinessException(
					"O email ou nome de usuário informado já está sendo utilizado em outro cadastro. Por favor, verifique!");
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
			throw new BusinessException(
					"A senha informada deve conter pelo menos uma letra maiúscula. Por favor, verifique!");
		}

	}

	private void validarNomeUsuario(String nomeUsuario) {

		if (nomeUsuario == null || nomeUsuario.trim().isEmpty()) {
			throw new BusinessException("Nome de usuário não informado. Por favor, verifique!");
		}

		if (nomeUsuario.length() < 4) {
			throw new BusinessException(
					"O nome de usuário informado deve conter pelo menos 4 caracteres. Por favor, verifique!");
		}

	}

	@Transactional(readOnly = true)
	public UsuarioAutenticadoDTO login(UsuarioDTO dto) {

		Usuario entity = repository.findByEmail(dto.getEmail())
				.orElseThrow(() -> new BusinessException("Email ou senha inválidos. Por favor, verifique!"));

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(entity.getNomeUsuario(), dto.getSenha()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtUtils.generateJwtToken(authentication);

		UsuarioAutenticadoDTO usuarioAutenticadoDTO = new UsuarioAutenticadoDTO();
		usuarioAutenticadoDTO.setId(entity.getId());
		usuarioAutenticadoDTO.setEmail(entity.getEmail());
		usuarioAutenticadoDTO.setNomeUsuario(entity.getNomeUsuario());
		usuarioAutenticadoDTO.setNomeCompleto(entity.getNomeCompleto());
		usuarioAutenticadoDTO.setToken(jwt);
		usuarioAutenticadoDTO.setIdentificadorNotificacao(entity.getIdentificadorNotificacao());
		
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
	public UsuarioDTO buscarUsuarioLogado() {

		Usuario usuarioLogado = getUsuarioLogado();

		return new UsuarioDTO(usuarioLogado);
	}

	public Usuario getUsuarioLogado() {
		return getUsuario(ServiceLocator.getUsuarioLogado().getId());
	}

	public Usuario getUsuario(Long id) {

		Optional<Usuario> optionalUsuario = repository.findById(id);

		Usuario usuario = optionalUsuario.orElseThrow(() -> new BusinessException("Usuário não encontrado"));

		return usuario;
	}

	public void gerarNovaSenhaUsuario(UsuarioDTO usuarioDTO) {

		validarSenha(usuarioDTO.getSenha());

		Optional<Usuario> optionalUsuario = repository.findByEmail(usuarioDTO.getEmail());

		if (optionalUsuario.isEmpty()) {
			throw new BusinessException("Não foi possível localizar o usuário. Por favor, verifique!");
		}

		Usuario usuario = optionalUsuario.get();
		usuario.setSenha(encoder.encode(usuarioDTO.getSenha()));

		repository.save(usuario);

	}

	@Transactional
	public UsuarioDTO alterarInformacoesUsuario(UsuarioDTO dto) {

		validarInformacoesObrigatoriasAlteracaoInformadas(dto);

		validarTelefoneInformado(dto);

		validarDocumentoJaUtilizadoEmOutroCadastro(dto);

		Usuario usuario = getUsuarioLogado();
		usuario.setNomeCompleto(dto.getNomeCompleto());
		usuario.setDataComemorativa(dto.getDataComemorativa());
		usuario.setDocumentoPrincipal(Util.getSomenteNumeros(dto.getDocumentoPrincipal()));
		usuario.setTelefone(Util.getSomenteNumeros(dto.getTelefone()));

		repository.save(usuario);

		return new UsuarioDTO(usuario);
	}

	private void validarDocumentoJaUtilizadoEmOutroCadastro(UsuarioDTO dto) {

		if (dto.getDocumentoPrincipal() != null && !dto.getDocumentoPrincipal().trim().isEmpty()) {

			String documento = Util.getSomenteNumeros(dto.getDocumentoPrincipal());

			if (documento.length() != 11 && documento.length() != 14) {
				throw new BusinessException("CPF deve possui 11 dígitos e CNPJ 14. Por favor, verifique!");
			}
			
			if (documento.length() == 11 && !CpfCnpjValidate.isCpfValid(documento)) {
				throw new BusinessException("CPF inválido. Por favor, verifique!");
			}
			
			if (documento.length() == 14 && !CpfCnpjValidate.isCnpjValid(documento)) {
				throw new BusinessException("CNPJ inválido. Por favor, verifique!");
			}

			Optional<Usuario> optionalUsarioComEsseDocumento = repository
					.findByDocumentoPrincipalDifferentUsuario(documento, ServiceLocator.getUsuarioLogado().getId());

			if (optionalUsarioComEsseDocumento.isPresent()) {
				throw new BusinessException(
						"Já existe um usuário cadastrado com esse documento. Por favor, verifique!");
			}

		}

	}

	private void validarTelefoneInformado(UsuarioDTO dto) {

		if (dto.getTelefone() != null && !dto.getTelefone().trim().isEmpty()) {

			if (Util.getSomenteNumeros(dto.getTelefone()).length() != 11) {
				throw new BusinessException("O telefone e a área devem totalizar 11 dígitos. Por favor, verifique!");
			}

		}

	}

	private void validarInformacoesObrigatoriasAlteracaoInformadas(UsuarioDTO dto) {

		if (dto == null || dto.getNomeCompleto() == null) {
			throw new BusinessException("Nome Completo não informado!");
		}

	}

	public UsuarioDTO alterarFotoUsuario(ArquivoDTO dto) {

		Usuario usuario = getUsuarioLogado();

		if (dto.getId() == null) {
			usuario.setFoto(null);
		} else {
			usuario.setFoto(getArquivo(dto.getId()));
		}

		repository.save(usuario);

		return new UsuarioDTO(usuario);
	}

	private Arquivo getArquivo(Long id) {

		Optional<Arquivo> optionalArquivo = arquivoRepository.findById(id);

		if (optionalArquivo.isEmpty()) {
			throw new BusinessException("Arquivo não encontrado!");
		}

		return optionalArquivo.get();
	}
	
	@Transactional(readOnly = true)
	public Page<UsuarioDTO> buscarUsuariosPaginadosOrdenados(String nomeCompleto, Pageable pageable) {

		Page<Usuario> usuariosPage = repository.buscarUsuariosPaginadosOrdenados(nomeCompleto, getUsuarioLogado(), pageable);
		
		HashMap<Long, Long> mapaAmigosUsuarioLogado = getMapaAmigosUsuarioLogado();
		HashMap<Long, Long> mapaUsuariosComSolicitacaoPendente = getMapaUsuariosComSolicitacaoPendente(usuariosPage.getContent());

		List<UsuarioDTO> usuariosDTO = new ArrayList<UsuarioDTO>();
        
		for (Usuario usuario : usuariosPage) {
            UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
            usuarioDTO.setIsAmigo(mapaAmigosUsuarioLogado.containsKey(usuario.getId()));
            usuarioDTO.setIsSolicitacaoAmizadePendente(mapaUsuariosComSolicitacaoPendente.containsKey(usuario.getId()));
            usuariosDTO.add(usuarioDTO);
        }

        return new PageImpl<>(usuariosDTO, pageable, usuariosPage.getTotalElements());
	}

	private HashMap<Long, Long> getMapaUsuariosComSolicitacaoPendente(List<Usuario> usuarios) {
		
		HashMap<Long, Long> mapaUsuariosComSolicitacaoPendente = new HashMap<Long, Long>();
		
		List<Long> listaIds = new ArrayList<Long>();
		
		for (Usuario usuario : usuarios) {
			listaIds.add(usuario.getId());
		}
		
		if(listaIds.size() > 0) {
			
			List<Notificacao> listaNotificacao = notificacaoRepository.buscarNotificacoesPendentesEntreUsuariosEUsuarioLogado(ServiceLocator.getUsuarioLogado().getId(), listaIds, TipoNotificacao.SOLICITACAO_AMIZADE);
			
			for (Notificacao notificacao : listaNotificacao) {
				
				if (notificacao.getUsuarioOrigem().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0) {
					mapaUsuariosComSolicitacaoPendente.put(notificacao.getUsuarioOrigem().getId(), notificacao.getUsuarioOrigem().getId());
				} else {
					mapaUsuariosComSolicitacaoPendente.put(notificacao.getUsuarioDestino().getId(), notificacao.getUsuarioDestino().getId());
				}
				
			}
			
		}
		
		return mapaUsuariosComSolicitacaoPendente;
	}

	private HashMap<Long, Long> getMapaAmigosUsuarioLogado() {
		
		List<Amizade> listaAmizade = amizadeRepository.findAmizadeUsuario(ServiceLocator.getUsuarioLogado().getId());
		
		HashMap<Long, Long> mapaAmigosUsuarioLogado = new HashMap<Long, Long>();
		
		for (Amizade amizade : listaAmizade) {
			
			if (amizade.getAmigo().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0) {
				mapaAmigosUsuarioLogado.put(amizade.getAmigo().getId(), amizade.getAmigo().getId());
			} else {
				mapaAmigosUsuarioLogado.put(amizade.getUsuario().getId(), amizade.getUsuario().getId());
			}
			
		}
		
		return mapaAmigosUsuarioLogado;
	}
	
	@Transactional
	public UsuarioDTO alteraIdentificadorUsuario(UsuarioAutenticadoDTO dto) {
		
		Usuario usuario = getUsuarioLogado();
		usuario.setIdentificadorNotificacao(dto.getIdentificadorNotificacao());
		
		repository.save(usuario);

		return new UsuarioDTO(usuario);
	}
	
	@Transactional
	public void atualizarIdentificadorContaBancariaUsuarioLogado(String identificadorContaBancaria) {
		
		Usuario usuario = getUsuarioLogado();
		usuario.setIdentificadorContaBancaria(identificadorContaBancaria);
		
		repository.save(usuario);
		
	}
}