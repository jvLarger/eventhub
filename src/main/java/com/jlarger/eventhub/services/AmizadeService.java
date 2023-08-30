package com.jlarger.eventhub.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.PushNotificationRequest;
import com.jlarger.eventhub.entities.Amizade;
import com.jlarger.eventhub.entities.Notificacao;
import com.jlarger.eventhub.entities.Usuario;
import com.jlarger.eventhub.entities.type.TipoNotificacao;
import com.jlarger.eventhub.repositories.AmizadeRepository;
import com.jlarger.eventhub.repositories.NotificacaoRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.ServiceLocator;

@Service
public class AmizadeService {
	
	@Autowired
	private NotificacaoService notificacaoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private AmizadeRepository amizadeRepository;
	
	@Autowired 
	private NotificacaoRepository notificacaoRepository;
	
	@Autowired 
	private PushNotificationService pushNotificationService;
	
	@Transactional
	public void enviarSolicitacaoAmizade(Long id) {
		
		Usuario usuarioOrigem = usuarioService.getUsuarioLogado();
		
		Usuario usuarioDestino = usuarioService.getUsuario(id);
		
		validarSeJaSaoAmigos(usuarioOrigem, usuarioDestino);
		
		validarTentativaPendente(usuarioOrigem, usuarioDestino);
		
		validarNumeroDeTentativasPermitidas(usuarioOrigem, usuarioDestino);
		
		Notificacao notificacao = popularNotificacaoSolicitacaoAmizade(usuarioOrigem, usuarioDestino);
		
		notificacaoService.enviarNotificacao(notificacao);
		
		if (usuarioDestino.getIdentificadorNotificacao() != null) {
			pushNotificationService.sendPushNotificationToToken(new PushNotificationRequest("Event Hub", usuarioOrigem.getNomeCompleto() + " deseja ser seu amigo.", "", usuarioDestino.getIdentificadorNotificacao()));
		}
		
	}

	private Notificacao popularNotificacaoSolicitacaoAmizade(Usuario usuarioOrigem, Usuario usuarioDestino) {
		
		Notificacao notificacao = new Notificacao();
		notificacao.setDataNotificacao(LocalDateTime.now());
		notificacao.setDescricao(usuarioOrigem.getNomeCompleto() + " deseja ser seu amigo");
		notificacao.setUsuarioOrigem(usuarioOrigem);
		notificacao.setUsuarioDestino(usuarioDestino);
		notificacao.setTipo(TipoNotificacao.SOLICITACAO_AMIZADE);
		
		return notificacao;
	}

	private void validarNumeroDeTentativasPermitidas(Usuario usuarioOrigem, Usuario usuarioDestino) {
		
		List<Notificacao> listaNotificacao = notificacaoRepository.buscarNotificacoesLidasEntreUsuarios(usuarioOrigem.getId(), usuarioDestino.getId(), TipoNotificacao.SOLICITACAO_AMIZADE);
		
		if (listaNotificacao != null && listaNotificacao.size() >= 3) {
			throw new BusinessException("O número máximo de tentativas foi atingido.");
		}
		
	}

	private void validarTentativaPendente(Usuario usuarioOrigem, Usuario usuarioDestino) {
		
		List<Notificacao> listaNotificacao = getTentativaPendente(usuarioOrigem, usuarioDestino);
		
		if (listaNotificacao != null && listaNotificacao.size() > 0) {
			throw new BusinessException("Já existe uma solicitação de amizade pendente entre vocês. Por favor, aguarde a resposta.");
		}
		
	}
	
	public List<Notificacao> getTentativaPendente(Usuario usuarioOrigem, Usuario usuarioDestino) {
		
		List<Notificacao> listaNotificacao = notificacaoRepository.buscarNotificacoesPendentesEntreUsuarios(usuarioOrigem.getId(), usuarioDestino.getId(), TipoNotificacao.SOLICITACAO_AMIZADE);
		
		return listaNotificacao;
	}

	private void validarSeJaSaoAmigos(Usuario usuarioOrigem, Usuario usuarioDestino) {
		
		List<Amizade> listaAmizade = amizadeRepository.findAmizadeEntreUsuarios(usuarioOrigem.getId(), usuarioDestino.getId());
		
		if (listaAmizade != null && listaAmizade.size() > 0) {
			throw new BusinessException("Você já é amigo da pessoa " + usuarioDestino.getNomeCompleto() + "!");
		}
		
	}

	@Transactional
	public void removerAmizade(Long id) {
		
		Usuario usuarioOrigem = usuarioService.getUsuarioLogado();
		
		Usuario usuarioDestino = usuarioService.getUsuario(id);
		
		List<Amizade> listaAmizade = amizadeRepository.findAmizadeEntreUsuarios(usuarioOrigem.getId(), usuarioDestino.getId());
		
		for (Amizade amizade : listaAmizade) {
			amizadeRepository.delete(amizade);
		}
		
	}
	
	@Transactional
	public void aceitarSolicitacaoAmizade(Long idNotificacao) {
		
		Notificacao notificacao = notificacaoService.getNotificacao(idNotificacao);
		
		if (notificacao.getUsuarioDestino().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0) {
			throw new BusinessException("Apenas o dono do convite pode aceitar.");
		}
		
		notificacaoService.marcarComoLida(idNotificacao);
		
		Amizade amizade = new Amizade();
		amizade.setAmigo(notificacao.getUsuarioDestino());
		amizade.setUsuario(notificacao.getUsuarioOrigem());
		
		amizadeRepository.save(amizade);
	}
	
	
	
}
