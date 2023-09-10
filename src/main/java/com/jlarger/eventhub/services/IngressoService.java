package com.jlarger.eventhub.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.entities.Ingresso;
import com.jlarger.eventhub.repositories.IngressoRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;

@Service
public class IngressoService {
	
	@Autowired
	private PagamentoService pagamentoService;
	
	@Autowired
	private IngressoRepository ingressoRepository;
	
	@Transactional
	public void excluirIngressosPorEvento(Long idEvento) {
		
		validarEventoInformado(idEvento);
		
		List<Ingresso> listaIngresso = buscarIngressosPorEvento(idEvento);
		
		for (Ingresso ingresso : listaIngresso) {
			
			pagamentoService.estornarPagamentoIngresso(ingresso);
			
			ingressoRepository.delete(ingresso);
			
		}
		
	}
	
	private void validarEventoInformado(Long idEvento) {
		
		if (idEvento == null || idEvento.compareTo(0L) <= 0) {
			throw new BusinessException("Evento não informado!");
		}
		
	}
	
	@Transactional(readOnly = true)
	public List<Ingresso> buscarIngressosPorEvento(Long idEvento) {
		
		validarEventoInformado(idEvento);
		
		List<Ingresso> listaIngresso = ingressoRepository.buscarIngressosPorEvento(idEvento);
		
		return listaIngresso;
	}

	public Integer countIngressosPorEvento(Long idEvento) {
		
		validarEventoInformado(idEvento);
		
		 Integer countIngressosPorEvento = ingressoRepository.countIngressosPorEvento(idEvento);
		
		return countIngressosPorEvento;
	}
	
}