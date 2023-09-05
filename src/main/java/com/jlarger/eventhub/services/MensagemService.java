package com.jlarger.eventhub.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.SalaBatePapoDTO;
import com.jlarger.eventhub.dto.UsuarioDTO;
import com.jlarger.eventhub.entities.Amizade;
import com.jlarger.eventhub.entities.Mensagem;
import com.jlarger.eventhub.entities.Usuario;
import com.jlarger.eventhub.repositories.MensagemRepository;
import com.jlarger.eventhub.utils.ServiceLocator;

@Service
public class MensagemService {
	
	@Autowired
	private AmizadeService amizadeService;
	
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
		
		return salaBatePapoDTO;
	}

}
