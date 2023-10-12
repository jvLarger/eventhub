package com.jlarger.eventhub.services;

import java.time.LocalDateTime;
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
import com.jlarger.eventhub.dto.FaturamentoDTO;
import com.jlarger.eventhub.dto.FeedEventosDTO;
import com.jlarger.eventhub.dto.GeoCoding;
import com.jlarger.eventhub.dto.IndicadoresEventoDTO;
import com.jlarger.eventhub.dto.IngressoDTO;
import com.jlarger.eventhub.entities.Amizade;
import com.jlarger.eventhub.entities.Evento;
import com.jlarger.eventhub.entities.EventoArquivo;
import com.jlarger.eventhub.entities.EventoCategoria;
import com.jlarger.eventhub.entities.Ingresso;
import com.jlarger.eventhub.entities.Mensagem;
import com.jlarger.eventhub.entities.Usuario;
import com.jlarger.eventhub.repositories.EventoRepository;
import com.jlarger.eventhub.repositories.IngressoRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.ServiceLocator;
import com.jlarger.eventhub.utils.Util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

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
	private MensagemService mensagemService;
	
	@Autowired
	private AmizadeService amizadeService;
	
	@Autowired
	private GeolocalizacaoService geolocalizacaoService;
	
	@Autowired
	private IngressoRepository ingressoRepository;
	
	@Autowired
	private EventoRepository eventoRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@Transactional
	public EventoDTO criarEvento(EventoDTO dto) {
		
		validarInformacoesEvento(dto);
		
		Evento evento = popularCamposEvento(dto);
		evento.setNumeroVisualizacoes(0);
		
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
		evento.setNumeroMaximoIngressos(dto.getNumeroMaximoIngressos());
		evento.setVisivel(dto.getVisivel());
		
		popularCoordenadasEvento(evento);
		
		return evento;
	}

	private void popularCoordenadasEvento(Evento evento) {
		
		StringBuilder endereco = new StringBuilder();
		
		endereco.append(evento.getLogradouro());
		endereco.append(",");
		endereco.append(evento.getNumero());
		endereco.append(" - ");
		endereco.append(evento.getBairro());
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
		
		if (eventoDTO.getNumeroMaximoIngressos() == null || eventoDTO.getNumeroMaximoIngressos().compareTo(0) <= 0) {
			throw new BusinessException("O número máximo de ingressos deve ser maior do que 0!");
		}
		
		if (eventoDTO.getVisivel() == null) {
			throw new BusinessException("Indicador se o evento está visível não informado!");
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
		evento.setNumeroVisualizacoes(eventoAntigo.getNumeroVisualizacoes());
		
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
			throw new BusinessException("Somente o dono do evento pode realizar essa ação!");
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
	
	private void validarIdUsuarioInformado(Long idUsuario) {
		
		if (idUsuario == null || idUsuario.compareTo(0L) <= 0) {
			throw new BusinessException("Usuário não informado!");
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
		eventoDTO.setIngressosVendidos(ingressoService.countIngressosPorEvento(idEvento));
		eventoDTO.setUltimosIngressoVendidos(ingressoService.buscarUltimosIngressosVendidosDoEvento(idEvento).stream().map(x -> new IngressoDTO(x)).collect(Collectors.toList()));
		eventoDTO.setDemonstreiInteresse(eventoInteresseService.isDemonstreiInteresseEvento(idEvento));
		
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
	
	@Transactional(readOnly = true)
	public IndicadoresEventoDTO buscarIndicadoresEvento(Long idEvento) {
		
		validarIdEventoInformado(idEvento);
		
		Evento evento = getEvento(idEvento);
		
		validarDonoEvento(evento);
		
		EventoDTO eventoDTO = buscarEvento(idEvento);
		FaturamentoDTO faturamentoDTO = new FaturamentoDTO(faturamentoService.getFaturamentoPorEvento(evento));
		
		IndicadoresEventoDTO indicadoresEventoDTO = new IndicadoresEventoDTO();
		indicadoresEventoDTO.setEvento(eventoDTO);
		indicadoresEventoDTO.setFaturamento(faturamentoDTO);
		indicadoresEventoDTO.setIngressosVendidos(ingressoService.countIngressosPorEvento(idEvento));
		
		return indicadoresEventoDTO;
	}
	
	@Transactional(readOnly = true)
	public List<IngressoDTO> buscarParticipantesDoEvento(Long idEvento) {
		
		validarIdEventoInformado(idEvento);
		
		Evento evento = getEvento(idEvento);
		
		validarDonoEvento(evento);
		
		List<Ingresso> listaIngresso = ingressoService.buscarIngressosPorEvento(idEvento);
		
		return listaIngresso.stream().map(x -> new IngressoDTO(x)).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<EventoDTO> buscarEventos(String nome, Double latitude, Double longitude, Double raio, String categorias, Date data, int pageNumber, int pageSize, Double valorInicial, Double valorFinal) {
		
		String sql = "SELECT e.* FROM test.evento e ";
		
		if (categorias != null && !categorias.trim().isEmpty()) {
			sql += "INNER JOIN test.evento_categoria ec ON ec.id_evento = e.id ";
		}
				
		sql += "WHERE earth_distance(ll_to_earth(:latitude, :longitude), ll_to_earth(e.latitude, e.longitude)) <= :raioKm * 1000 ";
		sql += "AND e.restrito = false ";
		sql += "AND e.visivel = true ";
		sql += "AND (e.valor >= :valorInicial AND e.valor <= :valorFinal) ";
		sql += "AND ((e.data > CURRENT_DATE) OR (e.data = CURRENT_DATE AND e.hora_inicio >= CURRENT_TIME)) ";
		
		if (categorias != null && !categorias.trim().isEmpty()) {
			sql += "AND ec.id_categoria IN (:listaIdCategoria) ";
		}
		
		if (nome != null && !nome.trim().isEmpty()) {
			sql += "AND e.nome ILIKE :nome ";
		}
		
		if (data != null) {
			sql += "AND e.data = :data ";
		}
		
		sql += "ORDER BY e.data ASC, e.hora_inicio ASC ";

		sql += "LIMIT :pageSize OFFSET :offset ";

		Query query = entityManager.createNativeQuery(sql, Evento.class);
		query.setParameter("latitude", latitude);
		query.setParameter("longitude", longitude);
		query.setParameter("raioKm", raio);
        query.setParameter("pageSize", pageSize);
        query.setParameter("offset", pageNumber * pageSize);
        query.setParameter("valorInicial", valorInicial);
        query.setParameter("valorFinal", valorFinal);
        
        if (nome != null && !nome.trim().isEmpty()) {
        	query.setParameter("nome", nome + "%");
        }
        
        if (categorias != null && !categorias.trim().isEmpty()) {
    	  query.setParameter("listaIdCategoria", getListaIdCategoria(categorias));
		}
        
        if (data != null) {
        	query.setParameter("data", data);
		}
        
		List<Evento> listaEvento = query.getResultList();
		
		List<EventoDTO> listaEventoDTO = popularListaEventosDTO(listaEvento);
		
		return listaEventoDTO;
	}
	
	@Transactional(readOnly = true)
	private List<EventoDTO> popularListaEventosDTO(List<Evento> listaEvento) {
	
		List<EventoDTO> listaEventoDTO = new ArrayList<EventoDTO>();
		
		List<Long> listaIdEvento = getListaIdEvento(listaEvento);
		
		HashMap<Long, ArrayList<EventoArquivo>> mapaArquivosPorEvento = eventoArquivoService.getMapaArquivosPorEventos(listaIdEvento);
		HashMap<Long, Long> mapaEventosQueDemonstreiInteresse = eventoInteresseService.getMapaMapaEventosQueDemonstreiInteresse(listaIdEvento);
		HashMap<Long, ArrayList<EventoCategoria>> mapaCategoriasPorEvento =  eventoCategoriaService.getMapaCategoriasPorEventos(listaIdEvento);
		
		for (Evento evento : listaEvento) {
			
			EventoDTO eventoDTO = new EventoDTO(evento);
			eventoDTO.setDemonstreiInteresse(mapaEventosQueDemonstreiInteresse.containsKey(evento.getId()));
			
			if (mapaArquivosPorEvento.containsKey(evento.getId())) {
				eventoDTO.setArquivos(mapaArquivosPorEvento.get(evento.getId()).stream().map(x -> new EventoArquivoDTO(x)).collect(Collectors.toList()));
			}
			
			if (mapaCategoriasPorEvento.containsKey(evento.getId())) {
				eventoDTO.setCategorias(mapaCategoriasPorEvento.get(evento.getId()).stream().map(x -> new EventoCategoriaDTO(x)).collect(Collectors.toList()));
			}
			
			listaEventoDTO.add(eventoDTO);
		}

		return listaEventoDTO;
	}

	private List<Long> getListaIdCategoria(String categorias) {
		
		List<Long> listaIdCategoria = new ArrayList<Long>();
		
		String[] ids = categorias.split(",");
		
		for (String id : ids) {
			listaIdCategoria.add(Long.parseLong(id));
		}
		
		return listaIdCategoria;
	}
	
	@Transactional
	public void registrarVisualizacaoEvento(Long idEvento) {
		
		validarIdEventoInformado(idEvento);
		
		Evento evento = getEvento(idEvento);
		evento.setNumeroVisualizacoes(evento.getNumeroVisualizacoes() + 1);
		
		eventoRepository.save(evento);
	}
	
	@Transactional
	public void compratilharEvento(Long idEvento, Long idUsuario) {

		validarIdEventoInformado(idEvento);
		
		validarIdUsuarioInformado(idUsuario);
		
		Evento evento = getEvento(idEvento);
		
		Usuario usuarioDestino = usuarioService.getUsuario(idUsuario);
		
		Mensagem mensagem = popularMensagemCompartilhamentoEvento(evento, usuarioDestino);
		
		mensagemService.enviarMensagemCompartilhamentoEvento(mensagem);
	}

	private Mensagem popularMensagemCompartilhamentoEvento(Evento evento, Usuario usuarioDestino) {
		
		Mensagem mensagem = new Mensagem();
		mensagem.setUsuarioOrigem(usuarioService.getUsuarioLogado());
		mensagem.setUsuarioDestino(usuarioDestino);
		mensagem.setDataMensagem(LocalDateTime.now());
		mensagem.setDescricao("Compartilhou o evento " + evento.getNome() + " com você!");
		mensagem.setEvento(evento);
		
		return mensagem;
	}
	
	@Transactional(readOnly = true)
	public FeedEventosDTO buscarFeedEventos(Double latitude, Double longitude) {
		
		validarLatitudeELongitudeInformadas(latitude, longitude);
		
		List<Evento> listaEventosQueMeusAmigosGostaram = buscarEventosQueMeusAmigosGostaram();

		List<Evento> listaEventosPopulares = buscarEventosPopulares(latitude, longitude, 10);
		
		FeedEventosDTO feedEventosDTO = new FeedEventosDTO();
		feedEventosDTO.setEventosQueMeusAmigosGostaram(popularListaEventosDTO(listaEventosQueMeusAmigosGostaram));
		feedEventosDTO.setEventosPopulares(popularListaEventosDTO(listaEventosPopulares));
		
		return feedEventosDTO;
	}
	
	@Transactional(readOnly = true)
	private List<Evento> buscarEventosQueMeusAmigosGostaram() {
		
		List<Amizade> listaAmizade = amizadeService.buscarAmizadesComUsuarioLogado();
		
		List<Long> listaIdUsuarioAmigos = getListaIdUsuarioAmigos(listaAmizade);
		
		String jpql = "";
		jpql += "SELECT e FROM Evento e ";
		jpql += "WHERE e.id IN (";
		jpql += "	SELECT ei.evento.id FROM EventoInteresse ei WHERE ei.usuario.id IN(:listaIdUsuario) ";
		jpql += ") ";
		jpql += "AND e.restrito = false ";
		jpql += "AND e.visivel = true ";
		jpql += "AND ((e.data > CURRENT_DATE) OR (e.data = CURRENT_DATE AND e.horaInicio >= CURRENT_TIME)) ";
		jpql += "ORDER BY (SELECT COUNT(ei2) FROM EventoInteresse ei2 WHERE ei2.evento.id = e.id) DESC";
		
		TypedQuery<Evento> query = entityManager.createQuery(jpql, Evento.class);
		query.setParameter("listaIdUsuario", listaIdUsuarioAmigos);

		List<Evento> listaEvento = query.getResultList();
		
		return listaEvento;
	}

	private List<Long> getListaIdUsuarioAmigos(List<Amizade> listaAmizade) {
		
		List<Long> listaIdUsuarioAmigos = new ArrayList<Long>();
		
		for (Amizade amizade : listaAmizade) {
			
			if (amizade.getUsuario().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0) {
				listaIdUsuarioAmigos.add(amizade.getUsuario().getId());
			} else {
				listaIdUsuarioAmigos.add(amizade.getAmigo().getId());
			}
			
		}
		
		return listaIdUsuarioAmigos;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	private List<Evento> buscarEventosPopulares(Double latitude, Double longitude, Integer limit) {
		
		String sql = "SELECT e.* FROM test.evento e ";
		sql += "WHERE earth_distance(ll_to_earth(:latitude, :longitude), ll_to_earth(e.latitude, e.longitude)) <= 50 * 1000 ";
		sql += "AND e.restrito = false ";
		sql += "AND e.visivel = true ";
		sql += "AND ((e.data > CURRENT_DATE) OR (e.data = CURRENT_DATE AND e.hora_inicio >= CURRENT_TIME)) ";
		sql += "ORDER BY e.numero_visualizacoes DESC, e.data ASC, e.hora_inicio ASC ";
		
		if (limit != null) {
			sql += "LIMIT :limit";
		}
		
		Query query = entityManager.createNativeQuery(sql, Evento.class);
		query.setParameter("latitude", latitude);
		query.setParameter("longitude", longitude);
		
		if (limit != null) {
			query.setParameter("limit", limit);
		}
		
		List<Evento> listaEvento = query.getResultList();
		
		return listaEvento;
	}

	private void validarLatitudeELongitudeInformadas(Double latitude, Double longitude) {
		
		if (latitude == null) {
			throw new BusinessException("Latitude não informada!");
		}
		
		if (longitude == null) {
			throw new BusinessException("Longitude não informada!");
		}
		
	}
	
	@Transactional(readOnly = true)
	public List<EventoDTO> buscarEventosMapaCalor(Double latitude, Double longitude) {
		
		List<Evento> listaEvento = buscarEventosPopulares(latitude, longitude, null);
		
		List<EventoDTO> listaEventoDTO = popularListaEventosDTO(listaEvento);
		
		classificarEventosPorPopularidade(listaEventoDTO);
		
		return listaEventoDTO;
	}

	private void classificarEventosPorPopularidade(List<EventoDTO> listaEventoDTO) {
		
		int totalEventos = listaEventoDTO.size();
		int grupos = listaEventoDTO.size() > 4 ? 4 : listaEventoDTO.size();
		int eventosPorGrupo = totalEventos / grupos;

		int grupoAtual = 1;

		for (EventoDTO evento : listaEventoDTO) {
			
			evento.setGrupoRelevancia(grupoAtual);
			
		//	System.out.println("Nome: " + evento.getNome() + ", Visualizações: " + evento.getNumeroVisualizacoes() + ", Grupo: " + grupoAtual);
			
			if (grupoAtual < grupos && listaEventoDTO.indexOf(evento) % eventosPorGrupo == eventosPorGrupo - 1) {
				grupoAtual++;
			}
		}
	
	}

}