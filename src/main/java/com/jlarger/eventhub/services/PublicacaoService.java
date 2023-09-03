package com.jlarger.eventhub.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.PublicacaoArquivoDTO;
import com.jlarger.eventhub.dto.PublicacaoComentarioDTO;
import com.jlarger.eventhub.dto.PublicacaoDTO;
import com.jlarger.eventhub.dto.UsuarioDTO;
import com.jlarger.eventhub.entities.Publicacao;
import com.jlarger.eventhub.entities.PublicacaoArquivo;
import com.jlarger.eventhub.entities.PublicacaoComentario;
import com.jlarger.eventhub.entities.Usuario;
import com.jlarger.eventhub.repositories.PublicacaoRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.ServiceLocator;

@Service
public class PublicacaoService {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PublicacaoArquivoService publicacaoArquivoService;
	
	@Autowired
	private PublicacaoCurtidaService publicacaoCurtidaService;
	
	@Autowired
	private PublicacaoComentarioService publicacaoComentarioService;
	
	@Autowired
	private PublicacaoRepository publicacaoRepository;
	
	@Transactional
	public PublicacaoDTO criarPublicacao(PublicacaoDTO dto) {
		
		validarCamposNovaPublicacao(dto);
		
		Usuario usuario = usuarioService.getUsuarioLogado();
		
		Publicacao publicacao = new Publicacao();
		publicacao.setUsuario(usuario);
		publicacao.setData(LocalDateTime.now());
		publicacao.setDescricao(dto.getDescricao().trim());
		
		publicacao = publicacaoRepository.save(publicacao);
		
		List<PublicacaoArquivo> listaPublicacaoArquivo = publicacaoArquivoService.criarPublicacaoArquivos(publicacao, dto.getArquivos());
		
		PublicacaoDTO publicacaoDTO = new PublicacaoDTO();
		publicacaoDTO.setId(publicacao.getId());
		publicacaoDTO.setUsuario(new UsuarioDTO(publicacao.getUsuario()));
		publicacaoDTO.setDescricao(publicacao.getDescricao());
		publicacaoDTO.setData(publicacao.getData());
		publicacaoDTO.setIsMinhaPublicacao(Boolean.TRUE);
		publicacaoDTO.setArquivos(listaPublicacaoArquivo.stream().map(x -> new PublicacaoArquivoDTO(x)).collect(Collectors.toList()));
		
		return publicacaoDTO;
	}

	private void validarCamposNovaPublicacao(PublicacaoDTO publicacaoDTO) {
		
		if (publicacaoDTO.getDescricao() == null || publicacaoDTO.getDescricao().trim().length() < 20) {
			throw new BusinessException("A publicação deve conter pelo menos 20 caracteres. Por favor, verifique!");
		}
		
		if (publicacaoDTO.getArquivos() == null || publicacaoDTO.getArquivos().isEmpty()) {
			throw new BusinessException("A publicação deve conter pelo menos uma imagem. Por favor, verifique!");
		}
		
	}

	@Transactional(readOnly = true)
	public PublicacaoDTO buscarPublicacao(Long id) {
		
		if (id == null) {
			throw new BusinessException("Publicação não informada!");
		}
		
		Publicacao publicacao = getPublicacao(id);
		
		List<PublicacaoComentario> listaPublicacaoComentario = publicacaoComentarioService.buscarComentariosPorPublicacao(publicacao.getId());
		List<PublicacaoArquivo> listaPublicacaoArquivo = publicacaoArquivoService.buscarArquivosPorPublicacao(publicacao.getId());
		Integer curtidas = publicacaoCurtidaService.buscarNumeroCurtidasPublicacao(publicacao.getId());
		
		PublicacaoDTO publicacaoDTO = new PublicacaoDTO();
		publicacaoDTO.setId(publicacao.getId());
		publicacaoDTO.setUsuario(new UsuarioDTO(publicacao.getUsuario()));
		publicacaoDTO.setData(publicacao.getData());
		publicacaoDTO.setDescricao(publicacao.getDescricao());
		publicacaoDTO.setIsMinhaPublicacao(publicacao.getUsuario().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) == 0);
		publicacaoDTO.setIsCurti(publicacaoCurtidaService.getIsCurtiPublicacao(publicacao.getId()));
		publicacaoDTO.setCurtidas(curtidas);
		publicacaoDTO.setComentarios(listaPublicacaoComentario.stream().map(x -> new PublicacaoComentarioDTO(x)).collect(Collectors.toList()));
		publicacaoDTO.setArquivos(listaPublicacaoArquivo.stream().map(x -> new PublicacaoArquivoDTO(x)).collect(Collectors.toList()));

		return publicacaoDTO;
	}

	@Transactional(readOnly = true)
	public Publicacao getPublicacao(Long id) {
		
		Optional<Publicacao> optionalPublicacao = publicacaoRepository.findById(id);

		Publicacao publicacao = optionalPublicacao.orElseThrow(() -> new BusinessException("Publicação não encontrado"));

		return publicacao;
	}
	
	@Transactional
	public void excluirPublicacao(Long id) {
		
		if (id == null) {
			throw new BusinessException("Publicação não informada!");
		}
		
		Publicacao publicacao = getPublicacao(id);
		
		if (publicacao.getUsuario().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0) {
			throw new BusinessException("Somente o dono da publicação pode realizar sua exclusão!");
		}
		
		publicacaoArquivoService.excluirArquivosPorPublicacao(id);
		
		publicacaoCurtidaService.excluirCurtidasPorPublicacao(id);
		
		publicacaoComentarioService.excluirComentariosPorPublicacao(id);
		
		publicacaoRepository.delete(publicacao);
	}
	
	@Transactional(readOnly = true)
	public Page<PublicacaoDTO> buscarFeed(Pageable pageable) {
		
		Page<Publicacao> pagePublicacao = publicacaoRepository.findPaginatedPublicacoesForUserAndFriends(ServiceLocator.getUsuarioLogado().getId(), pageable);
		
		List<PublicacaoDTO> listaPublicacaoDTO = new ArrayList<PublicacaoDTO>();

		List<Long> listaIdPublicacao = getListaIdPublicacao(pagePublicacao);
		
		if (listaIdPublicacao.size() > 0) {
			
			HashMap<Long, ArrayList<PublicacaoArquivo>> mapaArquivosPorPublicacao = publicacaoArquivoService.getMapaArquivosPorListaPublicacao(listaIdPublicacao);
			HashMap<Long, ArrayList<PublicacaoComentario>> mapaComentariosPorPublicacao = publicacaoComentarioService.getMapaComentariosPorListaPublicacao(listaIdPublicacao);
			HashMap<Long, Integer> mapaCurtidasPorPublicacao = publicacaoCurtidaService.getMapaTotalCurtidasPorListaPublicacao(listaIdPublicacao);
			HashMap<Long, Long> mapaPublicacoesQueEuCurti = publicacaoCurtidaService.getMapaPublicacoesQueEuCurtiPorListaPublicacao(listaIdPublicacao);

			for (Publicacao publicacao : pagePublicacao) {
				
				PublicacaoDTO publicacaoDTO = new PublicacaoDTO();
				publicacaoDTO.setId(publicacao.getId());
				publicacaoDTO.setUsuario(new UsuarioDTO(publicacao.getUsuario()));
				publicacaoDTO.setDescricao(publicacao.getDescricao());
				publicacaoDTO.setData(publicacao.getData());
				publicacaoDTO.setIsMinhaPublicacao(publicacao.getUsuario().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) == 0);
				publicacaoDTO.setIsCurti(mapaPublicacoesQueEuCurti.containsKey(publicacao.getId()));
				
				if (mapaCurtidasPorPublicacao.containsKey(publicacao.getId())) {
					publicacaoDTO.setCurtidas(mapaCurtidasPorPublicacao.get(publicacao.getId()));			
				} else {
					publicacaoDTO.setCurtidas(0);				
				}
				
				if (mapaArquivosPorPublicacao.containsKey(publicacao.getId())) {
					publicacaoDTO.setArquivos(mapaArquivosPorPublicacao.get(publicacao.getId()).stream().map(x -> new PublicacaoArquivoDTO(x)).collect(Collectors.toList()));			
				} else {
					publicacaoDTO.setArquivos(new ArrayList<PublicacaoArquivoDTO>());				
				}
				
				if (mapaComentariosPorPublicacao.containsKey(publicacao.getId())) {
					publicacaoDTO.setComentarios(mapaComentariosPorPublicacao.get(publicacao.getId()).stream().map(x -> new PublicacaoComentarioDTO(x)).collect(Collectors.toList()));			
				} else {
					publicacaoDTO.setComentarios(new ArrayList<PublicacaoComentarioDTO>());				
				}
				
				
	            listaPublicacaoDTO.add(publicacaoDTO);
	        }
			
		}
		
        return new PageImpl<>(listaPublicacaoDTO, pageable, pagePublicacao.getTotalElements());
	}

	private List<Long> getListaIdPublicacao(Page<Publicacao> pagePublicacao) {
		
		List<Long> listaIdPublicacao = new ArrayList<Long>();
		
		for (Publicacao publicacao : pagePublicacao) {
			listaIdPublicacao.add(publicacao.getId());
		}
		
		return listaIdPublicacao;
	}

}