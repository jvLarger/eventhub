package com.jlarger.eventhub.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.ArquivoDTO;
import com.jlarger.eventhub.dto.PerfilDTO;
import com.jlarger.eventhub.dto.PublicacaoResumidaDTO;
import com.jlarger.eventhub.dto.UsuarioComentarioDTO;
import com.jlarger.eventhub.dto.UsuarioDTO;
import com.jlarger.eventhub.entities.Amizade;
import com.jlarger.eventhub.entities.Publicacao;
import com.jlarger.eventhub.entities.PublicacaoArquivo;
import com.jlarger.eventhub.entities.Usuario;
import com.jlarger.eventhub.entities.UsuarioComentario;
import com.jlarger.eventhub.repositories.AmizadeRepository;
import com.jlarger.eventhub.repositories.IngressoRepository;
import com.jlarger.eventhub.repositories.PublicacaoRepository;
import com.jlarger.eventhub.repositories.UsuarioComentarioRepository;
import com.jlarger.eventhub.utils.ServiceLocator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Service
public class PerfilService {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private IngressoRepository ingressoRepository;

	@Autowired
	private AmizadeRepository amizadeRepository;

	@Autowired
	private UsuarioComentarioRepository usuarioComentarioRepository;

	@Autowired
	private PublicacaoRepository publicacaoRepository;

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private AmizadeService amizadeService;
	
	@Transactional(readOnly = true)
	public PerfilDTO buscarMeuPerfil() {
		return buscarPerfil(ServiceLocator.getUsuarioLogado().getId());
	}

	@Transactional(readOnly = true)
	public PerfilDTO buscarPerfil(Long id) {
		
		Usuario usuarioOrigem = usuarioService.getUsuarioLogado();
		
		Usuario usuarioDestino = usuarioService.getUsuario(id);
		
		PerfilDTO perfilDTO = new PerfilDTO();
		perfilDTO.setUsuario(new UsuarioDTO(usuarioService.getUsuario(id)));
		perfilDTO.setNumeroAmigos(getNumeroAmigosDosUsuario(id));
		perfilDTO.setNumeroEventos(getNumeroEventosParticipadosDosUsuario(id));
		perfilDTO.setIsSolicitacaoAmizadePendente(amizadeService.getTentativaPendente(usuarioOrigem, usuarioDestino).size() > 0);
		perfilDTO.setIsAmigo(getIsUsuarioLogadoAmigoDoUsuario(id));
		perfilDTO.setPublicacoes(buscarPublicacoesUsuario(id));
		perfilDTO.setComentarios(buscarComentariosPerfilUsuario(id));

		return perfilDTO;
	}

	private List<PublicacaoResumidaDTO> buscarPublicacoesUsuario(Long id) {

		List<Publicacao> listaPublicacao = publicacaoRepository.findAllByIdUsuaro(id);
		
		List<PublicacaoResumidaDTO> listaPublicacaoDTO = new ArrayList<PublicacaoResumidaDTO>();
		
		if (listaPublicacao.size() > 0) {
			
			HashMap<Long, PublicacaoArquivo> mapaArquivoPrincipalPorPublicacao = getMapaArquivoPrincipalPorPublicacao(listaPublicacao);
			
			for (Publicacao publicacao : listaPublicacao) {
				
				PublicacaoResumidaDTO publicacaoResumidaDTO = new PublicacaoResumidaDTO();
				publicacaoResumidaDTO.setId(publicacao.getId());
				publicacaoResumidaDTO.setData(publicacao.getData());
				publicacaoResumidaDTO.setDescricao(publicacao.getDescricao());
				publicacaoResumidaDTO.setFotoPrincipal(new ArquivoDTO(mapaArquivoPrincipalPorPublicacao.get(publicacao.getId()).getArquivo()));
				
				listaPublicacaoDTO.add(publicacaoResumidaDTO);
			}
			
		}
		
		ordenarPublicacoes(listaPublicacaoDTO);
		
		return listaPublicacaoDTO;
	}

	private void ordenarPublicacoes(List<PublicacaoResumidaDTO> listaPublicacaoDTO) {
		
		java.util.Collections.sort(listaPublicacaoDTO, (o1, o2) -> {
			return o1.getData().compareTo(o2.getData()) * -1;
		});
		
	}

	private HashMap<Long, PublicacaoArquivo> getMapaArquivoPrincipalPorPublicacao(List<Publicacao> listaPublicacao) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<PublicacaoArquivo> query = criteriaBuilder.createQuery(PublicacaoArquivo.class);

		Root<PublicacaoArquivo> root = query.from(PublicacaoArquivo.class);
		
		Subquery<Long> subquery = query.subquery(Long.class);
		
		Root<PublicacaoArquivo> subqueryRoot = subquery.from(PublicacaoArquivo.class);

		subquery.select(criteriaBuilder.min(subqueryRoot.get("id"))).where(criteriaBuilder.equal(subqueryRoot.get("publicacao"), root.get("publicacao")));

		Expression<Long> subqueryExpression = subquery.getSelection();
		
		Predicate inPredicate = root.get("id").in(subqueryExpression);

		List<Long> idPublicacoes = getListaIdPublicacao(listaPublicacao); 
		
		Predicate idPublicacaoPredicate = root.get("publicacao").get("id").in(idPublicacoes);

		query.where(criteriaBuilder.and(inPredicate, idPublicacaoPredicate));

		List<PublicacaoArquivo> resultado = entityManager.createQuery(query).getResultList();
		
		HashMap<Long, PublicacaoArquivo> mapaArquivoPrincipalPorPublicacao = new HashMap<Long, PublicacaoArquivo>();
		
		for (PublicacaoArquivo publicacaoArquivo : resultado) {
			mapaArquivoPrincipalPorPublicacao.put(publicacaoArquivo.getPublicacao().getId(), publicacaoArquivo);
		}
		
		return mapaArquivoPrincipalPorPublicacao;
	}

	private List<Long> getListaIdPublicacao(List<Publicacao> listaPublicacao) {
		
		List<Long> listaIdPublicacao = new ArrayList<Long>();
		
		for (Publicacao publicacao : listaPublicacao) {
			listaIdPublicacao.add(publicacao.getId());
		}
		
		return listaIdPublicacao;
	}

	private List<UsuarioComentarioDTO> buscarComentariosPerfilUsuario(Long id) {

		List<UsuarioComentario> listaComentarios = usuarioComentarioRepository.findAllByIdUsuaro(id);
		
		List<UsuarioComentarioDTO> listaComentariosDTO = listaComentarios.stream().map(x -> new UsuarioComentarioDTO(x)).collect(Collectors.toList());
		
		ordenarComentarios(listaComentariosDTO);
		
		return listaComentariosDTO;
	}

	private void ordenarComentarios(List<UsuarioComentarioDTO> listaComentariosDTO) {
		
		java.util.Collections.sort(listaComentariosDTO, (o1, o2) -> {
			return o1.getDataComentario().compareTo(o2.getDataComentario()) * -1;
		});
		
	}

	private Boolean getIsUsuarioLogadoAmigoDoUsuario(Long id) {

		List<Amizade> listaAmizade = amizadeRepository.findAmizadeEntreUsuarios(id,
				ServiceLocator.getUsuarioLogado().getId());

		return listaAmizade.size() > 0;
	}

	private Integer getNumeroEventosParticipadosDosUsuario(Long id) {
		return ingressoRepository.countByIdUsuario(id);
	}

	private Integer getNumeroAmigosDosUsuario(Long id) {

		Integer nrAmizadesOrigem = amizadeRepository.countByIdUsuarioOrigem(id);

		Integer nrAmizadesDestino = amizadeRepository.countByIdUsuarioDestino(id);

		return nrAmizadesOrigem + nrAmizadesDestino;
	}

}