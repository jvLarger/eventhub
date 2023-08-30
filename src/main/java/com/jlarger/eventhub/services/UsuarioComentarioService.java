package com.jlarger.eventhub.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.PushNotificationRequest;
import com.jlarger.eventhub.dto.UsuarioComentarioDTO;
import com.jlarger.eventhub.entities.Notificacao;
import com.jlarger.eventhub.entities.Usuario;
import com.jlarger.eventhub.entities.UsuarioComentario;
import com.jlarger.eventhub.entities.type.TipoNotificacao;
import com.jlarger.eventhub.repositories.UsuarioComentarioRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.ServiceLocator;

@Service
public class UsuarioComentarioService {
	
	@Autowired
	private UsuarioComentarioRepository usuarioComentarioRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private NotificacaoService notificacaoService;
	
	@Autowired
	private PushNotificationService pushNotificationService;
	
	@Transactional
	public void enviarSolicitacaoComentario(Long id, UsuarioComentarioDTO dto) {
		
		validarComentarioInformado(dto);
		
		Usuario usuarioOrigem = usuarioService.getUsuarioLogado();
		
		Usuario usuarioDestino = usuarioService.getUsuario(id);
		
		Notificacao notificacao = popularNotificacaoSolicitacaoComentario(usuarioOrigem, usuarioDestino, dto);
		
		notificacaoService.enviarNotificacao(notificacao);

		if (usuarioDestino.getIdentificadorNotificacao() != null) {
			pushNotificationService.sendPushNotificationToToken(new PushNotificationRequest("Event Hub", usuarioOrigem.getNomeCompleto() + " deseja comentar em seu perfil.", "", usuarioDestino.getIdentificadorNotificacao()));
		}
	}

	private void validarComentarioInformado(UsuarioComentarioDTO dto) {
		
		if (dto == null || dto.getComentario() == null || dto.getComentario().trim().isEmpty()) {
			throw new BusinessException("Comentário não informado!");
		}
		
		if (dto.getComentario().length() <= 5) {
			throw new BusinessException("O comentário deve possuir pelo menos 5 caracteres!");
		}
		
	}

	private Notificacao popularNotificacaoSolicitacaoComentario(Usuario usuarioOrigem, Usuario usuarioDestino, UsuarioComentarioDTO dto) {
		
		Notificacao notificacao = new Notificacao();
		notificacao.setDataNotificacao(LocalDateTime.now());
		notificacao.setDescricao(dto.getComentario());
		notificacao.setUsuarioOrigem(usuarioOrigem);
		notificacao.setUsuarioDestino(usuarioDestino);
		notificacao.setTipo(TipoNotificacao.SOLICITACAO_COMENTARIO_PUBLICO);
		
		return notificacao;
	}
	
	@Transactional
	public void aceitarSolicitacaoComentario(Long idNotificacao) {
		
		Notificacao notificacao = notificacaoService.getNotificacao(idNotificacao);
		
		if (notificacao.getUsuarioDestino().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0) {
			throw new BusinessException("Apenas o dono do convite pode aceitar.");
		}
		
		notificacaoService.marcarComoLida(idNotificacao);
		
		UsuarioComentario usuarioComentario = new UsuarioComentario();
		usuarioComentario.setUsuario(notificacao.getUsuarioDestino());
		usuarioComentario.setUsuarioOrigem(notificacao.getUsuarioOrigem());
		usuarioComentario.setComentario(notificacao.getDescricao());
		usuarioComentario.setDataComentario(notificacao.getDataNotificacao());
		
		usuarioComentarioRepository.save(usuarioComentario);
	}
	
	@Transactional
	public void removerComentario(Long id) {
		
		UsuarioComentario usuarioComentario = getUsuarioComentario(id);
		
		if (usuarioComentario.getUsuario().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0 && usuarioComentario.getUsuarioOrigem().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0) {
			throw new BusinessException("Apenas o dono do comentário ou do perfil podem remover.");
		}
		
		usuarioComentarioRepository.delete(usuarioComentario);
	}
	
	public UsuarioComentario getUsuarioComentario(Long id) {

		Optional<UsuarioComentario> optionalUsuarioComentario = usuarioComentarioRepository.findById(id);

		UsuarioComentario usuarioComentario = optionalUsuarioComentario.orElseThrow(() -> new BusinessException("Comentário não encontrado"));

		return usuarioComentario;
	}

}