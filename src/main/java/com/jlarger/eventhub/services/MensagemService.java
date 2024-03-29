package com.jlarger.eventhub.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.EventoArquivoDTO;
import com.jlarger.eventhub.dto.EventoDTO;
import com.jlarger.eventhub.dto.MensagemDTO;
import com.jlarger.eventhub.dto.SalaBatePapoDTO;
import com.jlarger.eventhub.dto.UsuarioDTO;
import com.jlarger.eventhub.entities.Amizade;
import com.jlarger.eventhub.entities.EventoArquivo;
import com.jlarger.eventhub.entities.Mensagem;
import com.jlarger.eventhub.entities.Usuario;
import com.jlarger.eventhub.repositories.MensagemRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.ServiceLocator;

@Service
public class MensagemService {
	
	@Autowired
	private AmizadeService amizadeService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private EventoArquivoService eventoArquivoService;
	
	@Autowired
	private MensagemRepository mensagemRepository;
	
	@Transactional(readOnly = true)
	public List<SalaBatePapoDTO> buscarSalasBatePapo() {
		
		List<Amizade> listaAmizades = amizadeService.buscarAmizadesComUsuarioLogado();
		
		List<SalaBatePapoDTO> listaSalaBatePapoDTO = new ArrayList<SalaBatePapoDTO>();
		
		if (listaAmizades != null && listaAmizades.size() > 0) {
			
			List<Long> listaIdUsuarioOrigem = getListaIdUsuarioOrigem(listaAmizades);
			
			HashMap<Long, Integer> mapaTotalMensagensNaoLidasPorUsuarios = getMapaTotalMensagensNaoLidasPorUsuarios(listaIdUsuarioOrigem);
			
			for (Amizade amizade : listaAmizades) {
				listaSalaBatePapoDTO.add(popularSalaBatePapoDTO(amizade, mapaTotalMensagensNaoLidasPorUsuarios));
			}
			
		}
		
		ordenarSalasBatePapo(listaSalaBatePapoDTO);
		
		return listaSalaBatePapoDTO;
	}

	private void ordenarSalasBatePapo(List<SalaBatePapoDTO> listaSalaBatePapoDTO) {
		
		Collections.sort(listaSalaBatePapoDTO, new Comparator<SalaBatePapoDTO>() {
		
			public int compare(SalaBatePapoDTO s1, SalaBatePapoDTO s2) {
				
				int comp = s1.getMensagensNaoLidas().compareTo(s2.getMensagensNaoLidas()) * -1;
				
				if (comp == 0) {
					comp = s1.getUsuario().getNomeCompleto().compareTo(s2.getUsuario().getNomeCompleto());
				}
				
				return comp;
			}
		});
		
	}

	@Transactional(readOnly = true)
	private HashMap<Long, Integer> getMapaTotalMensagensNaoLidasPorUsuarios(List<Long> listaIdUsuarioOrigem) {
		
		List<Mensagem> listaMensagem = mensagemRepository.buscarMensagensNaoLidasSalas(ServiceLocator.getUsuarioLogado().getId(), listaIdUsuarioOrigem);
		
		HashMap<Long, Integer> mapaTotalMensagensNaoLidasPorUsuarios = new HashMap<Long, Integer>();
				
		for (Mensagem mensagem : listaMensagem) {
			
			if (!mapaTotalMensagensNaoLidasPorUsuarios.containsKey(mensagem.getUsuarioOrigem().getId())) {
				mapaTotalMensagensNaoLidasPorUsuarios.put(mensagem.getUsuarioOrigem().getId(), 0);
			}
			
			Integer nrTotalMensagensNaoLidas = mapaTotalMensagensNaoLidasPorUsuarios.get(mensagem.getUsuarioOrigem().getId());
			nrTotalMensagensNaoLidas++;
			
			mapaTotalMensagensNaoLidasPorUsuarios.put(mensagem.getUsuarioOrigem().getId(), nrTotalMensagensNaoLidas);
		}
		
		return mapaTotalMensagensNaoLidasPorUsuarios;
	}

	private List<Long> getListaIdUsuarioOrigem(List<Amizade> listaAmizades) {
		
		List<Long> listaIdUsuarioOrigem = new ArrayList<Long>();
		
		for (Amizade amizade : listaAmizades) {
			
			if (amizade.getUsuario().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0) {
				listaIdUsuarioOrigem.add(amizade.getUsuario().getId());
			} else {
				listaIdUsuarioOrigem.add(amizade.getAmigo().getId());
			}
			
		}
		
		return listaIdUsuarioOrigem;
	}

	private SalaBatePapoDTO popularSalaBatePapoDTO(Amizade amizade, HashMap<Long, Integer> mapaTotalMensagensNaoLidasPorUsuarios) {

		SalaBatePapoDTO salaBatePapoDTO = new SalaBatePapoDTO();
		
		Usuario usuarioOrigem = null;
		
		if (amizade.getUsuario().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0) {
			usuarioOrigem = amizade.getUsuario();
		} else {
			usuarioOrigem = amizade.getAmigo();
		}
		
		salaBatePapoDTO.setUsuario(new UsuarioDTO(usuarioOrigem));
		
		if (mapaTotalMensagensNaoLidasPorUsuarios.containsKey(usuarioOrigem.getId())) {
			salaBatePapoDTO.setMensagensNaoLidas(mapaTotalMensagensNaoLidasPorUsuarios.get(usuarioOrigem.getId()));
		} else {
			salaBatePapoDTO.setMensagensNaoLidas(0);
		}
		
		Pageable pageable = PageRequest.of(0, 1);
		
		List<Mensagem> listaMensagem = mensagemRepository.buscarUltimaMensagemEntreUsuarios(amizade.getUsuario().getId(), amizade.getAmigo().getId(), pageable);
		
		if (listaMensagem.size() > 0) {
			salaBatePapoDTO.setUltimaMensagem(new MensagemDTO(listaMensagem.get(0)));
		}
		
		return salaBatePapoDTO;
	}
	
	@Transactional
	public MensagemDTO enviarMensagem(Long id, MensagemDTO dto) {
		
		validarIdUsuarioInformado(id);
		
		validarCamposEnvioMensagem(dto);
		
		Usuario usuarioOrigem = usuarioService.getUsuarioLogado();
		
		Usuario usuarioDestino = usuarioService.getUsuario(id);

		Mensagem mensagem = new Mensagem();
		mensagem.setUsuarioOrigem(usuarioOrigem);
		mensagem.setUsuarioDestino(usuarioDestino);
		mensagem.setDataMensagem(LocalDateTime.now());
		mensagem.setDescricao(dto.getDescricao());
		
		mensagem = mensagemRepository.save(mensagem);
		
		/*
		if (usuarioDestino.getIdentificadorNotificacao() != null ) {
			pushNotificationService.sendPushNotificationToToken(new PushNotificationRequest("Event Hub", "Você recebeu uma nova mensagem", "", usuarioDestino.getIdentificadorNotificacao()));
		}
		*/
		
		return new MensagemDTO(mensagem);
	}

	private void validarCamposEnvioMensagem(MensagemDTO dto) {
		
		if (dto == null || dto.getDescricao().trim().isEmpty()) {
			throw new BusinessException("Mensagem não informada!");
		}
		
	}
	
	@Transactional
	public List<MensagemDTO> buscarMensagens(Long idUsuario) {
		
		validarIdUsuarioInformado(idUsuario);
		
		List<Mensagem> listaMensagem = mensagemRepository.buscarMensagensEntreUsuarios(ServiceLocator.getUsuarioLogado().getId(), idUsuario);
		
		marcarMensagensComoLidas(listaMensagem);
		
		List<MensagemDTO> listaMensagemDTO = popularMensagens(listaMensagem);
		
		return listaMensagemDTO;
	}
	
	@Transactional
	private void marcarMensagensComoLidas(List<Mensagem> listaMensagem) {
		
		for (Mensagem mensagem : listaMensagem) {
			
			if (mensagem.getDataLeitura() == null && mensagem.getUsuarioDestino().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) == 0) {
				mensagem.setDataLeitura(LocalDateTime.now());
				mensagemRepository.save(mensagem);
			}
			
		}
		
	}

	private void validarIdUsuarioInformado(Long id) {
		
		if (id == null || id.compareTo(0L) <= 0) {
			throw new BusinessException("Usuário destino não informado!");
		}
		
	}
	
	@Transactional
	public List<MensagemDTO> buscarNovasMensagens(Long idUsuario) {
		
		validarIdUsuarioInformado(idUsuario);
		
		List<Mensagem> listaMensagem = mensagemRepository.buscarNovasMensagensEntreUsuarios(ServiceLocator.getUsuarioLogado().getId(), idUsuario);
		
		marcarMensagensComoLidas(listaMensagem);
		
		List<MensagemDTO> listaMensagemDTO = popularMensagens(listaMensagem);
		
		return listaMensagemDTO;
	}
	
	private List<MensagemDTO> popularMensagens(List<Mensagem> listaMensagem) {
				
		List<Long> listaIdEvento = getListaIdEventos(listaMensagem);
		
		Map<Long, ArrayList<EventoArquivo>> mapaArquivosPorEventos = eventoArquivoService.getMapaArquivosPorEventos(listaIdEvento);
		
		List<MensagemDTO> listaMensagemDTO = new ArrayList<MensagemDTO>();

		for (Mensagem mensagem : listaMensagem) {
			
			MensagemDTO mensagemDTO = new MensagemDTO(mensagem);
			
			if (mensagem.getEvento() != null) {
				
				EventoDTO eventoDTO = new EventoDTO(mensagem.getEvento());
				
				if (mapaArquivosPorEventos.containsKey(mensagem.getEvento().getId())) {
					eventoDTO.setArquivos(mapaArquivosPorEventos.get(mensagem.getEvento().getId()).stream().map(x -> new EventoArquivoDTO(x)).collect(Collectors.toList()));
				}
				
				mensagemDTO.setEvento(eventoDTO);
			}
			
			listaMensagemDTO.add(mensagemDTO);
			
		}
		
		return listaMensagemDTO;
	}

	private List<Long> getListaIdEventos(List<Mensagem> listaMensagem) {
		
		List<Long> listaIdEvento = new ArrayList<Long>();
		
		for (Mensagem mensagem : listaMensagem) {
			
			if (mensagem.getEvento() != null) {
				listaIdEvento.add(mensagem.getEvento().getId());
			}
			
		}
		
		return listaIdEvento;
	}

	@Transactional(readOnly = true)
	public Integer getNumeroMensagensNaoLidas() {
		
		Integer countMensagensNaoLidasUsuario = mensagemRepository.countMensagensNaoLidasUsuario(ServiceLocator.getUsuarioLogado().getId());
		
		return countMensagensNaoLidasUsuario;
	}
	
	@Transactional
	public void enviarMensagemCompartilhamentoEvento(Mensagem mensagem) {
		mensagemRepository.save(mensagem);
	}

}
