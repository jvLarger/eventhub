package com.jlarger.eventhub.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.entities.PublicacaoComentario;
import com.jlarger.eventhub.repositories.PublicacaoComentarioRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;

@Service
public class PublicacaoComentarioService {
	
	@Autowired
	private PublicacaoComentarioRepository publicacaoComentarioRepository;
	
	@Transactional(readOnly = true)
	public List<PublicacaoComentario> buscarComentariosPorPublicacao(Long idPublicacao) {

		validarPublicacaoInformada(idPublicacao);

		List<PublicacaoComentario> listaPublicacaoComentario = publicacaoComentarioRepository.buscarComentariosPorPublicacao(idPublicacao);

		return listaPublicacaoComentario;
	}
	
	@Transactional
	public void excluirComentariosPorPublicacao(Long idPublicacao) {
		
		validarPublicacaoInformada(idPublicacao);
		
		List<PublicacaoComentario> listaPublicacaoComentario = publicacaoComentarioRepository.buscarComentariosPorPublicacao(idPublicacao);
		
		for (PublicacaoComentario publicacaoComentario : listaPublicacaoComentario) {
			publicacaoComentarioRepository.delete(publicacaoComentario);
		}
		
	}

	private void validarPublicacaoInformada(Long idPublicacao) {
		
		if (idPublicacao == null) {
			throw new BusinessException("Publicação não informada!");
		}
		
	}
	
}