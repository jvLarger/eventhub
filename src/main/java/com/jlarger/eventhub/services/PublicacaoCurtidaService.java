package com.jlarger.eventhub.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.UsuarioDTO;
import com.jlarger.eventhub.entities.Publicacao;
import com.jlarger.eventhub.entities.PublicacaoCurtida;
import com.jlarger.eventhub.entities.Usuario;
import com.jlarger.eventhub.repositories.PublicacaoCurtidaRepository;
import com.jlarger.eventhub.repositories.PublicacaoRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.ServiceLocator;

@Service
public class PublicacaoCurtidaService {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PublicacaoCurtidaRepository publicacaoCurtidaRepository;
	
	@Autowired
	private PublicacaoRepository publicacaoRepository;
	
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
	
	@Transactional
	public void curtirPublicacao(Long idPublicacao) {
		
		validarPublicacaoInformada(idPublicacao);
		
		validarSeJaPublicacaoJaFoiCurtida(idPublicacao);
		
		Usuario usuarioLogado = usuarioService.getUsuarioLogado();
		
		Publicacao publicacao = getPublicacao(idPublicacao);
		
		PublicacaoCurtida publicacaoCurtida = new PublicacaoCurtida();
		publicacaoCurtida.setUsuario(usuarioLogado);
		publicacaoCurtida.setPublicacao(publicacao);
		
		publicacaoCurtidaRepository.save(publicacaoCurtida);
	}

	private void validarSeJaPublicacaoJaFoiCurtida(Long idPublicacao) {
		
		Optional<PublicacaoCurtida> optionalPublicacaoCurtida = publicacaoCurtidaRepository.buscarPublicacaoCurtidaPorUsuarioEPublicacao(idPublicacao, ServiceLocator.getUsuarioLogado().getId());
		
		if (optionalPublicacaoCurtida.isPresent()) {
			throw new BusinessException("Publicação já está curtida!");
		}

	}
	
	@Transactional
	public void descurtirPublicacao(Long idPublicacao) {
		
		validarPublicacaoInformada(idPublicacao);
		
		Optional<PublicacaoCurtida> optionalPublicacaoCurtida = publicacaoCurtidaRepository.buscarPublicacaoCurtidaPorUsuarioEPublicacao(idPublicacao, ServiceLocator.getUsuarioLogado().getId());
		
		if (optionalPublicacaoCurtida.isEmpty()) {
			throw new BusinessException("Publicação não está curtida!");
		}
		
		PublicacaoCurtida publicacaoCurtida = optionalPublicacaoCurtida.get();
		
		publicacaoCurtidaRepository.delete(publicacaoCurtida);
	}
	
	@Transactional(readOnly = true)
	public Publicacao getPublicacao(Long id) {
		
		Optional<Publicacao> optionalPublicacao = publicacaoRepository.findById(id);

		Publicacao publicacao = optionalPublicacao.orElseThrow(() -> new BusinessException("Publicação não encontrado"));

		return publicacao;
	}
	
	@Transactional(readOnly = true)
	public List<UsuarioDTO> buscarCurtidasPorPublicacao(Long idPublicacao) {
		
		validarPublicacaoInformada(idPublicacao);
		
		List<PublicacaoCurtida> listaPublicacaoCurtida = publicacaoCurtidaRepository.buscarCurtidasPorPublicacao(idPublicacao);
		
		List<UsuarioDTO> listaUsuariosDTO = new ArrayList<UsuarioDTO>();
		
		for (PublicacaoCurtida publicacaoCurtida : listaPublicacaoCurtida) {
			listaUsuariosDTO.add(new UsuarioDTO(publicacaoCurtida.getUsuario()));
		}
		
		return listaUsuariosDTO;
	}
}