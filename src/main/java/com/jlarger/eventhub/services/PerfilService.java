package com.jlarger.eventhub.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.PerfilDTO;
import com.jlarger.eventhub.dto.PublicacaoResumidaDTO;
import com.jlarger.eventhub.dto.UsuarioComentarioDTO;
import com.jlarger.eventhub.dto.UsuarioDTO;
import com.jlarger.eventhub.entities.Amizade;
import com.jlarger.eventhub.entities.UsuarioComentario;
import com.jlarger.eventhub.repositories.AmizadeRepository;
import com.jlarger.eventhub.repositories.IngressoRepository;
import com.jlarger.eventhub.repositories.UsuarioComentarioRepository;
import com.jlarger.eventhub.utils.ServiceLocator;

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
	
	@Transactional(readOnly = true)
	public PerfilDTO buscarMeuPerfil() {
		return buscarPerfil(ServiceLocator.getUsuarioLogado().getId());
	}

	@Transactional(readOnly = true)
	public PerfilDTO buscarPerfil(Long id) {

		PerfilDTO perfilDTO = new PerfilDTO();
		perfilDTO.setUsuario(new UsuarioDTO(usuarioService.getUsuario(id)));
		perfilDTO.setNumeroAmigos(getNumeroAmigosDosUsuario(id));
		perfilDTO.setNumeroEventos(getNumeroEventosParticipadosDosUsuario(id));
		perfilDTO.setIsAmigo(getIsUsuarioLogadoAmigoDoUsuario(id));
		perfilDTO.setPublicacoes(new ArrayList<PublicacaoResumidaDTO>());
		perfilDTO.setComentarios(buscarComentariosPerfilUsuario(id));

		return perfilDTO;
	}

	private List<UsuarioComentarioDTO> buscarComentariosPerfilUsuario(Long id) {
		
		List<UsuarioComentario> listaComentarios = usuarioComentarioRepository.findAllByIdUsuaro(id);
	
		return listaComentarios.stream().map(x -> new UsuarioComentarioDTO(x)).collect(Collectors.toList());
	}

	private Boolean getIsUsuarioLogadoAmigoDoUsuario(Long id) {
		
		List<Amizade> listaAmizade = amizadeRepository.findAmizadeEntreUsuarios(id, ServiceLocator.getUsuarioLogado().getId());
		
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