package com.jlarger.eventhub.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.entities.PublicacaoCurtida;
import com.jlarger.eventhub.repositories.PublicacaoCurtidaRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.ServiceLocator;

@Service
public class PublicacaoCurtidaService {
	
	@Autowired
	private PublicacaoCurtidaRepository publicacaoCurtidaRepository;
	
	@Transactional(readOnly = true)
	public Boolean getIsCurtiPublicacao(Long idPublicacao) {

		validarPublicacaoInformada(idPublicacao);

		Optional<PublicacaoCurtida> optionalPublicacaoCurtida = publicacaoCurtidaRepository.buscarPublicacaoCurtidaPorUsuarioEPublicacao(idPublicacao, ServiceLocator.getUsuarioLogado().getId());

		return optionalPublicacaoCurtida.isPresent();
	}
	
	@Transactional(readOnly = true)
	public Integer buscarNumeroCurtidasPublicacao(Long idPublicacao) {
		
		validarPublicacaoInformada(idPublicacao);
		
		Integer curtidas = publicacaoCurtidaRepository.countByIdPublicacao(idPublicacao);
		
		return curtidas;
	}
	
	@Transactional
	public void excluirCurtidasPorPublicacao(Long idPublicacao) {
		
		validarPublicacaoInformada(idPublicacao);
		
		List<PublicacaoCurtida> listaPublicacaoCurtida = publicacaoCurtidaRepository.buscarCurtidasPorPublicacao(idPublicacao);
		
		for (PublicacaoCurtida publicacaoCurtida : listaPublicacaoCurtida) {
			publicacaoCurtidaRepository.delete(publicacaoCurtida);
		}
		
	}
	
	private void validarPublicacaoInformada(Long idPublicacao) {
		
		if (idPublicacao == null) {
			throw new BusinessException("Publicação não informada!");
		}
		
	}
}