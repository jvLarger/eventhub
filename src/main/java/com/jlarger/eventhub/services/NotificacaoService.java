package com.jlarger.eventhub.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.NotificacaoDTO;
import com.jlarger.eventhub.entities.Notificacao;
import com.jlarger.eventhub.repositories.NotificacaoRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.ServiceLocator;

@Service
public class NotificacaoService {
	
	@Autowired
	private NotificacaoRepository notificacaoRepository;
	
	@Transactional
	public Notificacao enviarNotificacao(Notificacao notificacao) {
		
		notificacao = notificacaoRepository.save(notificacao);
		
		return notificacao;
	}
	
	@Transactional
	public void marcarComoLida(Long id) {
		
		Notificacao notificacao = getNotificacao(id);
		
		if (notificacao.getUsuarioDestino().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0) {
			throw new BusinessException("Apenas o dono da notificação pode realizar sua leitura.");
		}
		
		notificacao.setDataLeitura(LocalDateTime.now());
		
		notificacaoRepository.save(notificacao);
	}

	public Notificacao getNotificacao(Long id) {
		
		Optional<Notificacao> optionalNotificacao = notificacaoRepository.findById(id);

		Notificacao notificacao = optionalNotificacao.orElseThrow(() -> new BusinessException("Notificação não encontrada"));

		return notificacao;
	}
	
	@Transactional(readOnly = true)
	public List<NotificacaoDTO> buscarNotificacoesPendentes() {
		
		List<Notificacao> listaNotificacoes = notificacaoRepository.buscarNotificacoesPendentes(ServiceLocator.getUsuarioLogado().getId());
		
		return listaNotificacoes.stream().map(x -> new NotificacaoDTO(x)).collect(Collectors.toList());
	}

}