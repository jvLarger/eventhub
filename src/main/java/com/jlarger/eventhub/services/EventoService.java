package com.jlarger.eventhub.services;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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
import com.jlarger.eventhub.entities.Ingresso;
import com.jlarger.eventhub.repositories.EventoRepository;
import com.jlarger.eventhub.repositories.IngressoRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.ServiceLocator;
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
	private EventoInteresseService eventoInteresseService;
	
	@Autowired
	private IngressoService ingressoService;
	
	@Autowired
	private GeolocalizacaoService geolocalizacaoService;
	
	@Autowired
	private IngressoRepository ingressoRepository;
	
	@Autowired
	private EventoRepository eventoRepository;
	
	@Transactional
	public EventoDTO criarEvento(EventoDTO dto) {
		
		validarInformacoesEvento(dto);
		
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

	private void validarInformacoesEvento(EventoDTO eventoDTO) {
		
		if (eventoDTO.getNome() == null || eventoDTO.getNome().trim().isEmpty()) {
			throw new BusinessException("O nome do evento deve ser informado!");
		}
		
		if (eventoDTO.getData() == null) {
			throw new BusinessException("A data de início do evento não foi informada!");
		}
		
		if (Util.comprarDatasSemHora(eventoDTO.getData(), new Date()) < 0) {
			throw new BusinessException("A data de início do evento deve ser superior a data atual!");
		} else if (Util.comprarDatasSemHora(eventoDTO.getData(), new Date()) == 0 && eventoDTO.getHoraInicio().compareTo(LocalTime.now()) <= 0) {
			throw new BusinessException("A hora de início do evento deve ser superior a data atual!");
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
	
	@Transactional
	public EventoDTO alterarEvento(Long idEvento, EventoDTO dto) {
		
		validarIdEventoInformado(idEvento);
		
		validarInformacoesEvento(dto);
		
		Evento eventoAntigo = getEvento(idEvento);
		
		validarDonoEvento(eventoAntigo);
		
		validarSeEventoJaFoiConcluido(eventoAntigo);
		
		validarAlteracaoDataTerminoComIngressosJaVendidos(eventoAntigo, dto);
		
		Evento evento = popularCamposEvento(dto);
		evento.setId(idEvento);
		
		evento = eventoRepository.save(evento);
		
		List<EventoCategoria> listaEventoCategoria = eventoCategoriaService.atualizarCategoriasVinculadasAUmEvento(evento, dto.getCategorias());

		List<EventoArquivo> listaEventoArquivo = eventoArquivoService.atualizarArquivosVinculadosAUmEvento(evento, dto.getArquivos());
		
		faturamentoService.tratarEAtualizarDataLiberacaoPagamento(evento);
		
		EventoDTO eventoDTO = new EventoDTO(evento);
		eventoDTO.setArquivos(listaEventoArquivo.stream().map(x -> new EventoArquivoDTO(x)).collect(Collectors.toList()));
		eventoDTO.setCategorias(listaEventoCategoria.stream().map(x -> new EventoCategoriaDTO(x)).collect(Collectors.toList()));
		
		return eventoDTO;
	}
	
	private void validarDonoEvento(Evento eventoAntigo) {
		
		if (eventoAntigo.getUsuario().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0 ) {
			throw new BusinessException("Somente o dono do evento pode altera-lo!");
		}
		
	}

	private void validarSeEventoJaFoiConcluido(Evento eventoAntigo) {
		
		if (eventoAntigo.getData().compareTo(new Date()) < 0 ) {
			throw new BusinessException("Evento já está concluído!");
		}
		
	}

	@Transactional(readOnly = true)
	private Evento getEvento(Long idEvento) {
		
		validarIdEventoInformado(idEvento);
		
		Optional<Evento> optionalEvento = eventoRepository.findById(idEvento);

		Evento evento = optionalEvento.orElseThrow(() -> new BusinessException("Evento não encontrada"));

		return evento;
	}

	private void validarAlteracaoDataTerminoComIngressosJaVendidos(Evento eventoAntigo, EventoDTO eventoDTO) {
		
		if (eventoAntigo.getHoraInicio().compareTo(eventoDTO.getHoraInicio()) != 0 || eventoAntigo.getData().compareTo(eventoDTO.getData()) != 0) {
			
			List<Ingresso> listaIngresso = ingressoRepository.buscarIngressosPorEvento(eventoAntigo.getId());
			
			if (listaIngresso.size() > 0) {
				throw new BusinessException("Não é possível aletrar a data de inicío de eventos que já possuem ingressos vendidos!");
			}
			
		}
		
	}

	private void validarIdEventoInformado(Long idEvento) {
		
		if (idEvento == null || idEvento.compareTo(0L) <= 0) {
			throw new BusinessException("Evento não informado!");
		}
		
	}
	
	@Transactional
	public void excluirEvento(Long idEvento) {

		validarIdEventoInformado(idEvento);
		
		Evento evento = getEvento(idEvento);
		
		validarDonoEvento(evento);
		
		validarSeEventoJaFoiConcluido(evento);
		
		eventoArquivoService.excluirArquivosPorEvento(idEvento);
		
		eventoCategoriaService.excluirCategoriasPorEvento(idEvento);
		
		eventoInteresseService.excluirInteressesPorEvento(idEvento);
		
		ingressoService.excluirIngressosPorEvento(idEvento);
		
		faturamentoService.excluirFaturamentoPorEvento(idEvento);
		
		eventoRepository.delete(evento);
	}
	
	@Transactional(readOnly = true)
	public EventoDTO buscarEvento(Long idEvento) {
		
		validarIdEventoInformado(idEvento);
		
		Evento evento = getEvento(idEvento);
		
		List<EventoArquivo> listaEventoArquivo = eventoArquivoService.buscarArquviosPorEvento(idEvento);
		
		List<EventoCategoria> listaEventoCategoria = eventoCategoriaService.buscarCategoriasPorEvento(idEvento);

		EventoDTO eventoDTO = new EventoDTO(evento);
		eventoDTO.setArquivos(listaEventoArquivo.stream().map(x -> new EventoArquivoDTO(x)).collect(Collectors.toList()));
		eventoDTO.setCategorias(listaEventoCategoria.stream().map(x -> new EventoCategoriaDTO(x)).collect(Collectors.toList()));
		
		return eventoDTO;
	}
	
	@Transactional(readOnly = true)
	public List<EventoDTO> buscarMeusEventosPendentes() {
		
		List<Evento> listaEvento = eventoRepository.buscarEventosPedentesDoUsuario(ServiceLocator.getUsuarioLogado().getId(), new Date(), LocalTime.now());
		
		List<Long> listaIdEvento = getListaIdEvento(listaEvento);
		
		HashMap<Long, ArrayList<EventoArquivo>> mapaArquivosPorEvento = eventoArquivoService.getMapaArquivosPorEventos(listaIdEvento);
		HashMap<Long, ArrayList<EventoCategoria>> mapaCategoriasPorEvento = eventoCategoriaService.getMapaCategoriasPorEventos(listaIdEvento);

		List<EventoDTO> listaEventoDTO = new ArrayList<EventoDTO>();
		
		if (listaIdEvento.size() > 0) {
			
			for (Evento evento : listaEvento) {
				
				EventoDTO eventoDTO = new EventoDTO(evento);

				if (mapaArquivosPorEvento.containsKey(evento.getId())) {
					eventoDTO.setArquivos(mapaArquivosPorEvento.get(evento.getId()).stream().map(x -> new EventoArquivoDTO(x)).collect(Collectors.toList()));
				}
				
				if (mapaCategoriasPorEvento.containsKey(evento.getId())) {
					eventoDTO.setCategorias(mapaCategoriasPorEvento.get(evento.getId()).stream().map(x -> new EventoCategoriaDTO(x)).collect(Collectors.toList()));
				}
				
				listaEventoDTO.add(eventoDTO);
			}
			
		}
		
		return listaEventoDTO;
	}
	
	private List<Long> getListaIdEvento(List<Evento> listaEvento) {
		
		List<Long> listaIdEvento = new ArrayList<Long>();
		
		for (Evento evento : listaEvento) {
			listaIdEvento.add(evento.getId());
		}
		
		return listaIdEvento;
	}

	@Transactional(readOnly = true)
	public List<EventoDTO> buscarMeusEventosConcluidos() {
		
		List<Evento> listaEvento = eventoRepository.buscarMeusEventosConcluidos(ServiceLocator.getUsuarioLogado().getId(), new Date(), LocalTime.now());
		
		List<Long> listaIdEvento = getListaIdEvento(listaEvento);
		
		HashMap<Long, ArrayList<EventoArquivo>> mapaArquivosPorEvento = eventoArquivoService.getMapaArquivosPorEventos(listaIdEvento);
		HashMap<Long, ArrayList<EventoCategoria>> mapaCategoriasPorEvento = eventoCategoriaService.getMapaCategoriasPorEventos(listaIdEvento);

		List<EventoDTO> listaEventoDTO = new ArrayList<EventoDTO>();
		
		if (listaIdEvento.size() > 0) {
			
			for (Evento evento : listaEvento) {
				
				EventoDTO eventoDTO = new EventoDTO(evento);

				if (mapaArquivosPorEvento.containsKey(evento.getId())) {
					eventoDTO.setArquivos(mapaArquivosPorEvento.get(evento.getId()).stream().map(x -> new EventoArquivoDTO(x)).collect(Collectors.toList()));
				}
				
				if (mapaCategoriasPorEvento.containsKey(evento.getId())) {
					eventoDTO.setCategorias(mapaCategoriasPorEvento.get(evento.getId()).stream().map(x -> new EventoCategoriaDTO(x)).collect(Collectors.toList()));
				}
				
				listaEventoDTO.add(eventoDTO);
			}
			
		}
		
		return listaEventoDTO;
	}

}