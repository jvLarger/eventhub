package com.jlarger.eventhub.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.PublicacaArquivoDTO;
import com.jlarger.eventhub.dto.PublicacaoDTO;
import com.jlarger.eventhub.entities.Arquivo;
import com.jlarger.eventhub.entities.Publicacao;
import com.jlarger.eventhub.entities.PublicacaoArquivo;
import com.jlarger.eventhub.entities.Usuario;
import com.jlarger.eventhub.repositories.PublicacaoArquivoRepository;
import com.jlarger.eventhub.repositories.PublicacaoRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;

@Service
public class PublicacaoService {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ArquivoService arquivoService;
	
	@Autowired
	private PublicacaoRepository publicacaoRepository;
	
	@Autowired
	private PublicacaoArquivoRepository publicacaoArquivoRepository;
	
	@Transactional
	public PublicacaoDTO criarPublicacao(PublicacaoDTO dto) {
		
		validarCamposNovaPublicacao(dto);
		
		Usuario usuario = usuarioService.getUsuarioLogado();
		
		Publicacao publicacao = new Publicacao();
		publicacao.setUsuario(usuario);
		publicacao.setData(LocalDateTime.now());
		publicacao.setDescricao(dto.getDescricao().trim());
		
		publicacao = publicacaoRepository.save(publicacao);
		
		List<PublicacaoArquivo> listaPublicacaoArquivo = criarPublicacaoArquivos(publicacao, dto.getListaArquivos());
		
		PublicacaoDTO publicacaoDTO = new PublicacaoDTO();
		publicacaoDTO.setListaArquivos(listaPublicacaoArquivo.stream().map(x -> new PublicacaArquivoDTO(x)).collect(Collectors.toList()));
		
		return publicacaoDTO;
	}

	private List<PublicacaoArquivo> criarPublicacaoArquivos(Publicacao publicacao, List<PublicacaArquivoDTO> listaArquivos) {
	
		List<PublicacaoArquivo> listaPublicacaoArquivo = new ArrayList<PublicacaoArquivo>();
		
		for (PublicacaArquivoDTO publicacaArquivoDTO : listaArquivos) {
			
			Arquivo arquivo = arquivoService.getArquivo(publicacaArquivoDTO.getArquivo().getId());
			
			PublicacaoArquivo publicacaoArquivo = new PublicacaoArquivo();
			publicacaoArquivo.setPublicacao(publicacao);
			publicacaoArquivo.setArquivo(arquivo);
			
			publicacaoArquivo = publicacaoArquivoRepository.save(publicacaoArquivo);
			
			listaPublicacaoArquivo.add(publicacaoArquivo);
		}
		
		return listaPublicacaoArquivo;
	}

	private void validarCamposNovaPublicacao(PublicacaoDTO publicacaoDTO) {
		
		if (publicacaoDTO.getDescricao() == null || publicacaoDTO.getDescricao().trim().length() < 20) {
			throw new BusinessException("A publicação deve conter pelo menos 20 caracteres. Por favor, verifique!");
		}
		
		if (publicacaoDTO.getListaArquivos() == null || publicacaoDTO.getListaArquivos().isEmpty()) {
			throw new BusinessException("A publicação deve conter pelo menos uma imagem. Por favor, verifique!");
		}
		
	}

}