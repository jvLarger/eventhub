package com.jlarger.eventhub.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.EventoArquivoDTO;
import com.jlarger.eventhub.dto.EventoCategoriaDTO;
import com.jlarger.eventhub.dto.EventoDTO;
import com.jlarger.eventhub.dto.GeoCoding;
import com.jlarger.eventhub.entities.Evento;
import com.jlarger.eventhub.entities.EventoArquivo;
import com.jlarger.eventhub.entities.EventoCategoria;
import com.jlarger.eventhub.repositories.EventoRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.Util;

@Service
public class EventoService {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private FaturamentoService faturamentoService;
	
	@Autowired
	private EventoCategoriaService eventoCategoriaService;
	
	@Autowired
	private EventoArquivoService eventoArquivoService;
	
	@Autowired
	private GeolocalizacaoService geolocalizacaoService;
	
	@Autowired
	private EventoRepository eventoRepository;
	
	@Transactional
	public EventoDTO criarEvento(EventoDTO dto) {
		
		validarInformacoesNovoEvento(dto);
		
		Evento evento = popularCamposEvento(dto);
	
		evento = eventoRepository.save(evento);
		
		List<EventoCategoria> listaEventoCategoria = eventoCategoriaService.vincularCategoriasAUmEvento(evento, dto.getCategorias());

		List<EventoArquivo> listaEventoArquivo = eventoArquivoService.vincularArquivosAUmEvento(evento, dto.getArquivos());
		
		faturamentoService.criarFaturamentoInicialParaEvento(evento);
		
		EventoDTO eventoDTO = new EventoDTO(evento);
		eventoDTO.setArquivos(listaEventoArquivo.stream().map(x -> new EventoArquivoDTO(x)).collect(Collectors.toList()));
		eventoDTO.setCategorias(listaEventoCategoria.stream().map(x -> new EventoCategoriaDTO(x)).collect(Collectors.toList()));
		
		return eventoDTO;
	}

	private Evento popularCamposEvento(EventoDTO dto) {
	
		Evento evento = new Evento();
		evento.setId(dto.getId());
		evento.setBairro(dto.getBairro().trim());
		evento.setCep(Util.getSomenteNumeros(dto.getCep().trim()));
		evento.setCidade(dto.getCidade().trim());
		evento.setComplemento(dto.getComplemento());
		evento.setData(dto.getData());
		evento.setDescricao(dto.getDescricao().trim());
		evento.setEstado(dto.getEstado().trim());
		evento.setHoraInicio(dto.getHoraInicio());
		evento.setLogradouro(dto.getLogradouro().trim());
		evento.setNome(dto.getNome().trim());
		evento.setNumero(dto.getNumero().trim());
		evento.setRestrito(dto.getRestrito());
		evento.setValor(dto.getValor());
		evento.setUsuario(usuarioService.getUsuarioLogado());
		
		popularCoordenadasEvento(evento);
		
		return evento;
	}

	private void popularCoordenadasEvento(Evento evento) {
		
		StringBuilder endereco = new StringBuilder();
		endereco.append(evento.getLogradouro());
		endereco.append(",");
		endereco.append(evento.getNumero());
		endereco.append(",");
		endereco.append(evento.getCidade());
		endereco.append(",");
		endereco.append(evento.getEstado());
		
		GeoCoding geoCoding = geolocalizacaoService.buscarGeolocalizacao(endereco.toString());
		
		evento.setLatitude(geoCoding.getGeoCodingResults()[0].getGeometry().getLocation().getLat());
		evento.setLongitude(geoCoding.getGeoCodingResults()[0].getGeometry().getLocation().getLng());
	}

	private void validarInformacoesNovoEvento(EventoDTO eventoDTO) {
		
		if (eventoDTO.getNome() == null || eventoDTO.getNome().trim().isEmpty()) {
			throw new BusinessException("O nome do evento deve ser informado!");
		}
		
		if (eventoDTO.getData() == null) {
			throw new BusinessException("A data de início do evento não foi informada!");
		}
		
		if (eventoDTO.getData().compareTo(new Date()) <= 0) {
			throw new BusinessException("A data de início do evento deve ser superior a data atual!");
		}
		
		if (eventoDTO.getHoraInicio() == null) {
			throw new BusinessException("A hora de início do evento não foi informada!");
		}
		
		if (eventoDTO.getValor() == null) {
			throw new BusinessException("O valor do ingresso não foi informado!");
		}
		
		if (eventoDTO.getValor().compareTo(0.0) < 0) {
			throw new BusinessException("O valor do ingresso não pode ser negativo!");
		}
		
		if (eventoDTO.getDescricao() == null || eventoDTO.getDescricao().trim().isEmpty()) {
			throw new BusinessException("A descrição do evento não foi informada!");
		}
		
		if (eventoDTO.getDescricao().trim().length() < 20) {
			throw new BusinessException("A descrição do evento deve possuir pelo menos 20 caracteres!");
		}
		
		if (eventoDTO.getCep() == null) {
			throw new BusinessException("O CEP não foi informado!");
		}
		
		if (Util.getSomenteNumeros(eventoDTO.getCep()).length() != 8) {
			throw new BusinessException("O CEP deve possuir 8 dígitos!");
		}
		
		if (eventoDTO.getCidade() == null || eventoDTO.getCidade().trim().isEmpty()) {
			throw new BusinessException("A cidade do evento deve ser informado!");
		}
		
		if (eventoDTO.getLogradouro() == null || eventoDTO.getLogradouro().trim().isEmpty()) {
			throw new BusinessException("O logradouro do evento deve ser informado!");
		}
		
		if (eventoDTO.getBairro() == null || eventoDTO.getBairro().trim().isEmpty()) {
			throw new BusinessException("O bairro do evento deve ser informado!");
		}
		
		if (eventoDTO.getNumero() == null || eventoDTO.getNumero().trim().isEmpty()) {
			throw new BusinessException("O número do evento deve ser informado!");
		}
		
		if (eventoDTO.getCategorias() == null || eventoDTO.getCategorias().isEmpty()) {
			throw new BusinessException("Pelo menos uma categoria deve ser informada!");
		}
		
		if (eventoDTO.getArquivos() == null || eventoDTO.getArquivos().isEmpty()) {
			throw new BusinessException("Pelo menos uma foto deve ser adicionada!");
		}
		
	}

}