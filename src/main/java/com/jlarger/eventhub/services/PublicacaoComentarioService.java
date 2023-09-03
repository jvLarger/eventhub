package com.jlarger.eventhub.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.PublicacaoComentarioDTO;
import com.jlarger.eventhub.entities.Publicacao;
import com.jlarger.eventhub.entities.PublicacaoComentario;
import com.jlarger.eventhub.entities.Usuario;
import com.jlarger.eventhub.repositories.PublicacaoComentarioRepository;
import com.jlarger.eventhub.repositories.PublicacaoRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.ServiceLocator;

@Service
public class PublicacaoComentarioService {
	
	@Autowired
	private PublicacaoComentarioRepository publicacaoComentarioRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PublicacaoRepository publicacaoRepository;
	
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
	
	@Transactional
	public PublicacaoComentarioDTO criarPublicacaoComentario(PublicacaoComentarioDTO dto, Long idPublicacao) {
		
		validarPublicacaoInformada(idPublicacao);
		
		validarCamposObrigatorios(dto);
		
		Usuario usuarioLogado = usuarioService.getUsuarioLogado();
		
		Publicacao publicacao = getPublicacao(idPublicacao);
		
		PublicacaoComentario publicacaoComentario = new PublicacaoComentario();
		publicacaoComentario.setPublicacao(publicacao);
		publicacaoComentario.setData(LocalDateTime.now());
		publicacaoComentario.setDescricao(dto.getDescricao().trim());
		publicacaoComentario.setUsuario(usuarioLogado);
		
		publicacaoComentario = publicacaoComentarioRepository.save(publicacaoComentario);
		
		return new PublicacaoComentarioDTO(publicacaoComentario);
	}
	
	private void validarCamposObrigatorios(PublicacaoComentarioDTO dto) {
		
		if (dto.getDescricao() == null || dto.getDescricao().trim().length() < 10 ) {
			throw new BusinessException("O comentário deve possuir pelo menos 10 caracteres!");
		} 
		
	}

	@Transactional
	public void excluirPublicacaoComentario(Long idPublicacaoComentario) {
		
		if (idPublicacaoComentario == null) {
			throw new BusinessException("Comentário não informado!");
		}
		
		PublicacaoComentario publicacaoComentario = getPublicacaoComentario(idPublicacaoComentario);
		
		if (publicacaoComentario.getUsuario().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0 && publicacaoComentario.getPublicacao().getUsuario().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0 ) {
			throw new BusinessException("Somente o dono da publicação ou do comentário pode realizar sua exclusão!");
		}
		
		publicacaoComentarioRepository.delete(publicacaoComentario);
	}
	
	@Transactional(readOnly = true)
	private PublicacaoComentario getPublicacaoComentario(Long idPublicacaoComentario) {
	
		Optional<PublicacaoComentario> optionalPublicacaoComentario = publicacaoComentarioRepository.findById(idPublicacaoComentario);

		PublicacaoComentario publicacaoComentario = optionalPublicacaoComentario.orElseThrow(() -> new BusinessException("Comentário não encontrado"));

		return publicacaoComentario;
	}
	
	@Transactional(readOnly = true)
	public Publicacao getPublicacao(Long id) {
		
		Optional<Publicacao> optionalPublicacao = publicacaoRepository.findById(id);

		Publicacao publicacao = optionalPublicacao.orElseThrow(() -> new BusinessException("Publicação não encontrado"));

		return publicacao;
	}
	
	@Transactional(readOnly = true)
	public HashMap<Long, ArrayList<PublicacaoComentario>> getMapaComentariosPorListaPublicacao(List<Long> listaIdPublicacao) {
		
		validarPeloMenosUmaPublicacaoInformada(listaIdPublicacao);

		List<PublicacaoComentario> listaPublicacaoComentario = publicacaoComentarioRepository.buscarComentariosPorListaPublicacao(listaIdPublicacao);
		
		HashMap<Long, ArrayList<PublicacaoComentario>> mapaComentarios = new HashMap<Long, ArrayList<PublicacaoComentario>>();
		
		for (PublicacaoComentario publicacaoComentario : listaPublicacaoComentario) {
			
			if (!mapaComentarios.containsKey(publicacaoComentario.getPublicacao().getId())) {
				mapaComentarios.put(publicacaoComentario.getPublicacao().getId(), new ArrayList<PublicacaoComentario>());
			}
			
			mapaComentarios.get(publicacaoComentario.getPublicacao().getId()).add(publicacaoComentario);
		}
		
		return mapaComentarios;
	}
	
	private void validarPeloMenosUmaPublicacaoInformada(List<Long> listaIdPublicacao) {
		
		if (listaIdPublicacao == null || listaIdPublicacao.isEmpty()) {
			throw new BusinessException("Nenhuma publicação informada!");
		}
		
	}
	
}