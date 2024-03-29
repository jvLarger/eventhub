package com.jlarger.eventhub.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.PublicacaoArquivoDTO;
import com.jlarger.eventhub.entities.Arquivo;
import com.jlarger.eventhub.entities.Publicacao;
import com.jlarger.eventhub.entities.PublicacaoArquivo;
import com.jlarger.eventhub.repositories.PublicacaoArquivoRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;

@Service
public class PublicacaoArquivoService {
	
	@Autowired
	private ArquivoService arquivoService;
	
	@Autowired
	private PublicacaoArquivoRepository publicacaoArquivoRepository;
	
	@Transactional
	public List<PublicacaoArquivo> criarPublicacaoArquivos(Publicacao publicacao, List<PublicacaoArquivoDTO> listaArquivos) {
		
		List<PublicacaoArquivo> listaPublicacaoArquivo = new ArrayList<PublicacaoArquivo>();
		
		for (PublicacaoArquivoDTO publicacaArquivoDTO : listaArquivos) {
			
			Arquivo arquivo = arquivoService.getArquivo(publicacaArquivoDTO.getArquivo().getId());
			
			PublicacaoArquivo publicacaoArquivo = new PublicacaoArquivo();
			publicacaoArquivo.setPublicacao(publicacao);
			publicacaoArquivo.setArquivo(arquivo);
			
			publicacaoArquivo = publicacaoArquivoRepository.save(publicacaoArquivo);
			
			listaPublicacaoArquivo.add(publicacaoArquivo);
		}
		
		return listaPublicacaoArquivo;
	}
	
	@Transactional(readOnly = true)
	public List<PublicacaoArquivo> buscarArquivosPorPublicacao(Long idPublicacao) {
		
		validarPublicacaoInformada(idPublicacao);
		
		List<PublicacaoArquivo>  listaPublicacaoArquivo = publicacaoArquivoRepository.buscarArquivosPorPublicacao(idPublicacao);
		
		return listaPublicacaoArquivo;
	}
	
	@Transactional
	public void excluirArquivosPorPublicacao(Long idPublicacao) {
		
		validarPublicacaoInformada(idPublicacao);
		
		List<PublicacaoArquivo>  listaPublicacaoArquivo = publicacaoArquivoRepository.buscarArquivosPorPublicacao(idPublicacao);
		
		for (PublicacaoArquivo publicacaoArquivo : listaPublicacaoArquivo) {

			Arquivo arquivo = publicacaoArquivo.getArquivo();
			
			publicacaoArquivoRepository.delete(publicacaoArquivo);
			
			arquivoService.excluirArquivo(arquivo);
		}
		
	}
	
	private void validarPublicacaoInformada(Long idPublicacao) {
		
		if (idPublicacao == null) {
			throw new BusinessException("Publicação não informada!");
		}
		
	}
	
	@Transactional(readOnly = true)
	public HashMap<Long, ArrayList<PublicacaoArquivo>> getMapaArquivosPorListaPublicacao(List<Long> listaIdPublicacao) {
		
		validarPeloMenosUmaPublicacaoInformada(listaIdPublicacao);
		
		List<PublicacaoArquivo> listaPublicacaoArquivo = publicacaoArquivoRepository.buscarArquivosPorListaPublicacao(listaIdPublicacao);
		
		HashMap<Long, ArrayList<PublicacaoArquivo>> mapaArquivos = new HashMap<Long, ArrayList<PublicacaoArquivo>>();
		
		for (PublicacaoArquivo publicacaoArquivo : listaPublicacaoArquivo) {
			
			if (!mapaArquivos.containsKey(publicacaoArquivo.getPublicacao().getId())) {
				mapaArquivos.put(publicacaoArquivo.getPublicacao().getId(), new ArrayList<PublicacaoArquivo>());
			}
			
			mapaArquivos.get(publicacaoArquivo.getPublicacao().getId()).add(publicacaoArquivo);
		}
		
		return mapaArquivos;
	}

	private void validarPeloMenosUmaPublicacaoInformada(List<Long> listaIdPublicacao) {
		
		if (listaIdPublicacao == null || listaIdPublicacao.isEmpty()) {
			throw new BusinessException("Nenhuma publicação informada!");
		}
		
	}
}
