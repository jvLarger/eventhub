package com.jlarger.eventhub.services;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.EventoArquivoDTO;
import com.jlarger.eventhub.dto.EventoDTO;
import com.jlarger.eventhub.dto.IngressoDTO;
import com.jlarger.eventhub.entities.Arquivo;
import com.jlarger.eventhub.entities.Evento;
import com.jlarger.eventhub.entities.EventoArquivo;
import com.jlarger.eventhub.entities.Ingresso;
import com.jlarger.eventhub.entities.Notificacao;
import com.jlarger.eventhub.entities.PublicacaoComentario;
import com.jlarger.eventhub.entities.Usuario;
import com.jlarger.eventhub.entities.type.TipoNotificacao;
import com.jlarger.eventhub.repositories.EventoRepository;
import com.jlarger.eventhub.repositories.IngressoRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.CpfCnpjValidate;
import com.jlarger.eventhub.utils.ServiceLocator;
import com.jlarger.eventhub.utils.Util;

@Service
public class IngressoService {
	
	@Autowired
	private PagamentoService pagamentoService;
	
	@Autowired
	private IngressoRepository ingressoRepository;

	@Autowired
	private EventoRepository eventoRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private FaturamentoService faturamentoService;
	
	@Autowired
	private EventoArquivoService eventoArquivoService;
	
	@Autowired
	private NotificacaoService notificacaoService;
	
	@Autowired
	private ArquivoService arquivoService;
	
	@Autowired
	private PasswordEncoder encoder;

	@Transactional
	public void excluirIngressosPorEvento(Long idEvento) {
		
		validarEventoInformado(idEvento);
		
		List<Ingresso> listaIngresso = buscarIngressosPorEvento(idEvento);
		
		for (Ingresso ingresso : listaIngresso) {
			
			Boolean isSucessoEstorno = Boolean.TRUE;
			
			if (ingresso.getIdentificadorTransacaoPagamento() != null) {
				isSucessoEstorno = pagamentoService.estornarPagamentoIngresso(ingresso);
			}
			
			if (isSucessoEstorno) {
				ingressoRepository.delete(ingresso);
			}
			
		}
		
	}
	
	private void validarEventoInformado(Long idEvento) {
		
		if (idEvento == null || idEvento.compareTo(0L) <= 0) {
			throw new BusinessException("Evento não informado!");
		}
		
	}
	
	@Transactional(readOnly = true)
	public List<Ingresso> buscarIngressosPorEvento(Long idEvento) {
		
		validarEventoInformado(idEvento);
		
		List<Ingresso> listaIngresso = ingressoRepository.buscarIngressosPorEvento(idEvento);
		
		return listaIngresso;
	}

	public Integer countIngressosPorEvento(Long idEvento) {
		
		validarEventoInformado(idEvento);
		
		Integer countIngressosPorEvento = ingressoRepository.countIngressosPorEvento(idEvento);
		
		return countIngressosPorEvento;
	}
	
	@Transactional(readOnly = true)
	public List<Ingresso> buscarUltimosIngressosVendidosDoEvento(Long idEvento) {
		Pageable pageable = PageRequest.of(0, 5);
        return ingressoRepository.findUltimosIngressosVendidosDoEvento(idEvento, pageable);
    }
	
	@Transactional
	public IngressoDTO comprarIngresso(IngressoDTO dto) {
		
		validarEventoInformado(dto.getEvento().getId());
		
		Evento evento = getEvento(dto.getEvento().getId());
		
		validarSeExisteIngressoDisponivel(evento);
		
		validarEventoVisivel(evento);
		
		validarCamposCompraIngresso(dto, evento);
		
		String indificadorPagamento = null;
		
		Boolean isPagamentoComSucesso = Boolean.TRUE;
		
		if (evento.getValor().compareTo(0.0) > 0) {
			
			isPagamentoComSucesso = Boolean.FALSE;
			
			indificadorPagamento = pagamentoService.realizarPagamentoCartao(dto.getPagamento(), evento.getValor());
			
			isPagamentoComSucesso = pagamentoService.verificarSePagamentoBemSucedido(indificadorPagamento);
			
		}
		
		IngressoDTO ingressoDTO = new IngressoDTO();
		
		if (isPagamentoComSucesso) {
			
			Ingresso ingresso = new Ingresso();
			ingresso.setEvento(evento);
			ingresso.setUsuario(usuarioService.getUsuarioLogado());
			ingresso.setNome(dto.getNome().trim());
			ingresso.setDocumentoPrincipal(Util.getSomenteNumeros(dto.getDocumentoPrincipal()));
			ingresso.setTelefone(Util.getSomenteNumeros(dto.getTelefone()));
			ingresso.setEmail(dto.getEmail().trim());
			ingresso.setDataComemorativa(dto.getDataComemorativa());
			ingresso.setValorTotalIngresso(evento.getValor());
			ingresso.setValorTaxa(pagamentoService.calcularValorTaxaIngresso(evento.getValor()));
			ingresso.setValorFaturamento(evento.getValor() - ingresso.getValorTaxa());
			ingresso.setIdentificadorTransacaoPagamento(indificadorPagamento);
			ingresso.setIdentificadorIngresso("Aguardando identificador");
			
			ingresso = ingressoRepository.save(ingresso);

			String identificadorIngresso = gerarIdentificadorIngresso(ingresso);
			
			Arquivo qrcode = arquivoService.gerarQrcode(identificadorIngresso);
			
			ingresso.setIdentificadorIngresso(identificadorIngresso);
			ingresso.setQrcode(qrcode);
			
			ingresso = ingressoRepository.save(ingresso);
			
			ingressoDTO = new IngressoDTO(ingresso);
			
			faturamentoService.atualizarValoresFaturamentoPorEvento(evento);
			
			notificarPedidoProcessadoComSucesso(evento);
		
		} else {
			notificarPedidoProcessadoComErro(evento);
		}
		
		return ingressoDTO;
	}
	
	private String gerarIdentificadorIngresso(Ingresso ingresso) {
		return encoder.encode(ingresso.toString());
	}

	@Transactional
	private void notificarPedidoProcessadoComErro(Evento evento) {
		
		Notificacao notificacao = popularNotificacaoPedidoProcessadoComErro(evento);
		
		notificacaoService.enviarNotificacao(notificacao);
	}
	
	@Transactional
	private void notificarPedidoProcessadoComSucesso(Evento evento) {
		
		Notificacao notificacao = popularNotificacaoPedidoProcessadoComSucesso(evento);
		
		notificacaoService.enviarNotificacao(notificacao);
	}
	
	private Notificacao popularNotificacaoPedidoProcessadoComErro(Evento evento) {
		
		Usuario usuarioLogado = usuarioService.getUsuarioLogado();
		
		Notificacao notificacao = new Notificacao();
		notificacao.setDataNotificacao(LocalDateTime.now());
		notificacao.setDescricao("Não foi possível concluir a compra do seu ingresso para o evento '" + evento.getNome() + "'. Verifique os dados para pagamento durante a próxima tentativa.");
		notificacao.setUsuarioDestino(usuarioLogado);
		notificacao.setTipo(TipoNotificacao.COMPRA_INGRESSO_ERRO);
		
		return notificacao;
	}
	
	private Notificacao popularNotificacaoPedidoProcessadoComSucesso(Evento evento) {
		
		Usuario usuarioLogado = usuarioService.getUsuarioLogado();
		
		Notificacao notificacao = new Notificacao();
		notificacao.setDataNotificacao(LocalDateTime.now());
		notificacao.setDescricao("A compra do seu ingresso para o evento '" + evento.getNome() + "' foi processada com sucesso!");
		notificacao.setUsuarioDestino(usuarioLogado);
		notificacao.setTipo(TipoNotificacao.COMPRA_INGRESSO_SUCESSO);
		
		return notificacao;
	}

	private void validarEventoVisivel(Evento evento) {
		
		if (!evento.getVisivel()) {
			throw new BusinessException("Não é permitido a venda de ingressos para esse evento neste momento!");	
		}
		
	}

	private void validarSeExisteIngressoDisponivel(Evento evento) {
		
		Integer ingressosVendidos = ingressoRepository.countIngressosPorEvento(evento.getId());
		
		if (ingressosVendidos.compareTo(evento.getNumeroMaximoIngressos()) >= 0) {
			throw new BusinessException("O número máximo de ingressos já foi vendido!");
		}
		
	}

	@Transactional(readOnly = true)
	private Evento getEvento(Long idEvento) {
		
		validarEventoInformado(idEvento);
		
		Optional<Evento> optionalEvento = eventoRepository.findById(idEvento);

		Evento evento = optionalEvento.orElseThrow(() -> new BusinessException("Evento não encontrada"));

		return evento;
	}

	private void validarCamposCompraIngresso(IngressoDTO ingressoDTO, Evento evento) {
		
		if (ingressoDTO.getDataComemorativa() == null) {
			throw new BusinessException("Data comemorativa não informada!");
		}
		
		if (ingressoDTO.getNome() == null || ingressoDTO.getNome().trim().isEmpty()) {
			throw new BusinessException("Data comemorativa não informada!");
		}
		
		if (ingressoDTO.getDocumentoPrincipal() == null || ingressoDTO.getDocumentoPrincipal().isEmpty()) {
			throw new BusinessException("Documento não informado!");
		}

		String documento = Util.getSomenteNumeros(ingressoDTO.getDocumentoPrincipal());

		if (documento.length() != 11 && documento.length() != 14) {
			throw new BusinessException("CPF deve possui 11 dígitos e CNPJ 14. Por favor, verifique!");
		}
		
		if (documento.length() == 11 && !CpfCnpjValidate.isCpfValid(documento)) {
			throw new BusinessException("CPF do titular inválido. Por favor, verifique!");
		}
		
		if (documento.length() == 14 && !CpfCnpjValidate.isCnpjValid(documento)) {
			throw new BusinessException("CNPJ do titular inválido. Por favor, verifique!");
		}
		
		if (ingressoDTO.getTelefone() == null || ingressoDTO.getTelefone().isEmpty()) {
			throw new BusinessException("Telefone não informado!");
		}
		
		if (Util.getSomenteNumeros(ingressoDTO.getTelefone()).length() != 11) {
			throw new BusinessException("O telefone e a área devem totalizar 11 dígitos. Por favor, verifique!");
		}

		if (ingressoDTO.getEmail() == null || ingressoDTO.getEmail().trim().isEmpty()) {
			throw new BusinessException("Email não informado. Por favor, verifique!");
		}

		if (!Util.isEmailValido(ingressoDTO.getEmail())) {
			throw new BusinessException("O email informado não é válido. Por favor, verifique!");
		}
		
		if (evento.getValor().compareTo(0.0) > 0) {
			pagamentoService.validarCamposPagamentoCartao(ingressoDTO.getPagamento());
		}
		
	}
	
	@Transactional(readOnly = true)
	public List<IngressoDTO> buscarIngressosPendentes() {
		
		List<Ingresso> listaIngressos = ingressoRepository.buscarIngressosPendentes(ServiceLocator.getUsuarioLogado().getId(), new Date(), LocalTime.now());
		
		List<IngressoDTO> listaIngressoDTO = popularIngressosCompleto(listaIngressos);
		
		return listaIngressoDTO;
	}
	
	@Transactional(readOnly = true)
	public List<IngressoDTO> buscarIngressosConcluidos() {
		
		List<Ingresso> listaIngressos = ingressoRepository.buscarIngressosConcluidos(ServiceLocator.getUsuarioLogado().getId(), new Date(), LocalTime.now());
		
		List<IngressoDTO> listaIngressoDTO = popularIngressosCompleto(listaIngressos);
		
		return listaIngressoDTO;
	}

	@Transactional(readOnly = true)
	private List<IngressoDTO> popularIngressosCompleto(List<Ingresso> listaIngressos) {
		
		List<IngressoDTO> listaIngressoDTO = new ArrayList<IngressoDTO>();
		
		for (Ingresso ingresso : listaIngressos) {
			
			IngressoDTO ingressoDTO = new IngressoDTO(ingresso);
			ingressoDTO.setEvento(popularEventoDTO(ingresso.getEvento()));
			
			listaIngressoDTO.add(ingressoDTO);
		}
		
		return listaIngressoDTO;
	}

	private EventoDTO popularEventoDTO(Evento evento) {
		
		List<EventoArquivo> listaEventoArquivo = eventoArquivoService.buscarArquviosPorEvento(evento.getId());
		
		EventoDTO eventoDTO = new EventoDTO(evento);
		eventoDTO.setArquivos(listaEventoArquivo.stream().map(x -> new EventoArquivoDTO(x)).collect(Collectors.toList()));
		
		return eventoDTO;
	}
	
	@Transactional(readOnly = true)
	public IngressoDTO validarIngresso(String identificadorIngresso) {
		
		validarIdentificadorIngressoInformado(identificadorIngresso);
		
		Ingresso ingresso = buscarIngressoPorIdentificadorIngresso(identificadorIngresso);
		
		validarSeSouDonoDoEvento(ingresso);
		
		validarSeOIngressoJaFoiUtilizado(ingresso);
		
		validarSeOEventoJaEstaConcluido(ingresso);
		
		validarSeOEventoJaIniciou(ingresso);
		
		IngressoDTO ingressoDTO = new IngressoDTO(ingresso, ingresso.getEvento());
		ingressoDTO.setEvento(popularEventoDTO(ingresso.getEvento()));
		
		return ingressoDTO;
	}
	
	private void validarSeOEventoJaIniciou(Ingresso ingresso) {
		
		if (Util.comprarDatasSemHora(ingresso.getEvento().getData(), new Date()) != 0) {
			throw new BusinessException("Esse evento não irá ocorrer hoje.");
		}
		
	/*	if (ingresso.getEvento().getHoraInicio().compareTo(LocalTime.now()) < 0) {
			throw new BusinessException("Esse evento ainda não iniciou.");
		}
	*/	
	}

	private void validarSeOEventoJaEstaConcluido(Ingresso ingresso) {
		
		if (Util.comprarDatasSemHora(ingresso.getEvento().getData(), new Date()) < 0) {
			throw new BusinessException("Esse evento já está concluído.");
		}
		
	}

	private void validarSeOIngressoJaFoiUtilizado(Ingresso ingresso) {
		
		if (ingresso.getDataUtilizacao() != null) {
			throw new BusinessException("Esse ingresso já foi utilizado anteriormente.");
		}
		
	}

	private void validarSeSouDonoDoEvento(Ingresso ingresso) {
		
		if (ingresso.getEvento().getUsuario().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0) {
			throw new BusinessException("Esse ingresso não pertence a um evento organizado por você.");
		}
		
	}

	@Transactional(readOnly = true)
	private Ingresso buscarIngressoPorIdentificadorIngresso(String identificadorIngresso) {
		
		Optional<Ingresso> optionalIngresso = ingressoRepository.buscarIngressoPorIdentificador(identificadorIngresso);
		
		if (optionalIngresso.isEmpty()) {
			throw new BusinessException("Nenhum ingresso localizado através desse identificador!");
		}
		
		return optionalIngresso.get();
	}

	private void validarIdentificadorIngressoInformado(String identificadorIngresso) {
		
		if (identificadorIngresso == null || identificadorIngresso.trim().isEmpty()) {
			throw new BusinessException("Identificador do ingresso não informado!");
		}
		
		if (identificadorIngresso.trim().equals("Aguardando identificador")) {
			throw new BusinessException("Identificador do ingresso não é válido!");
		}
		
	}
	
	@Transactional
	public void utilizarIngresso(Long id) {
		
		validarIdIngressoInformado(id);
		
		Ingresso ingresso = getIngresso(id);
		
		validarSeSouDonoDoEvento(ingresso);
		
		validarSeOIngressoJaFoiUtilizado(ingresso);
		
		ingresso.setDataUtilizacao(LocalDateTime.now());
		
		ingressoRepository.save(ingresso);
		
	}
	
	@Transactional(readOnly = true)
	private Ingresso getIngresso(Long id) {
		
		Optional<Ingresso> optionalIngresso = ingressoRepository.findById(id);
		
		if (optionalIngresso.isEmpty()) {
			throw new BusinessException("Ingresso não encontrado!");
		}
		
		return optionalIngresso.get();
	}

	private void validarIdIngressoInformado(Long id) {
		
		if (id == null || id.compareTo(0L) <= 0) {
			throw new BusinessException("Ingresso não informado!");
		}
		
	}
	
	@Transactional
	public void venderIngressos() {
		
		List<Evento> listaEventos = eventoRepository.findAll();
		
		List<Usuario> listaUsuario = usuarioService.findAll();
		
		for (Evento evento : listaEventos) {
			
			List<Usuario> listaEmbaralhada = shuffleList(listaUsuario);
			
			int numeroIngressos = new Random().nextInt(1, 20);
			
			for (int i = 0; i < numeroIngressos; i++) {
				
				Ingresso ingresso = new Ingresso();
				ingresso.setEvento(evento);
				ingresso.setUsuario(listaEmbaralhada.get(i));
				ingresso.setNome(listaEmbaralhada.get(i).getNomeCompleto().trim());
				ingresso.setDocumentoPrincipal(Util.getSomenteNumeros(listaEmbaralhada.get(i).getDocumentoPrincipal()));
				ingresso.setTelefone(Util.getSomenteNumeros(listaEmbaralhada.get(i).getTelefone()));
				ingresso.setEmail(listaEmbaralhada.get(i).getEmail().trim());
				ingresso.setDataComemorativa(listaEmbaralhada.get(i).getDataComemorativa());
				ingresso.setValorTotalIngresso(evento.getValor());
				ingresso.setValorTaxa(pagamentoService.calcularValorTaxaIngresso(evento.getValor()));
				ingresso.setValorFaturamento(evento.getValor() - ingresso.getValorTaxa());
				ingresso.setIdentificadorTransacaoPagamento(null);
				ingresso.setIdentificadorIngresso("Aguardando identificador");
				
				ingresso = ingressoRepository.save(ingresso);

				String identificadorIngresso = gerarIdentificadorIngresso(ingresso);
				
				Arquivo qrcode = arquivoService.gerarQrcode(identificadorIngresso);
				
				ingresso.setIdentificadorIngresso(identificadorIngresso);
				ingresso.setQrcode(qrcode);
				
				ingresso = ingressoRepository.save(ingresso);
				
			}
			
			faturamentoService.atualizarValoresFaturamentoPorEvento(evento);
			
		}
		
	}
	
	
	private List<Usuario> shuffleList(List<Usuario> listaUsuario) {
		
		List<Usuario> list = new ArrayList<>();
		list.addAll(listaUsuario);
		
		Collections.shuffle(listaUsuario);

		return listaUsuario;
	}
}