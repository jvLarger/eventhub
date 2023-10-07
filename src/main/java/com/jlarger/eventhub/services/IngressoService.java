package com.jlarger.eventhub.services;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.EventoArquivoDTO;
import com.jlarger.eventhub.dto.EventoDTO;
import com.jlarger.eventhub.dto.IngressoDTO;
import com.jlarger.eventhub.entities.Evento;
import com.jlarger.eventhub.entities.EventoArquivo;
import com.jlarger.eventhub.entities.Ingresso;
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
	
	@Transactional
	public void excluirIngressosPorEvento(Long idEvento) {
		
		validarEventoInformado(idEvento);
		
		List<Ingresso> listaIngresso = buscarIngressosPorEvento(idEvento);
		
		for (Ingresso ingresso : listaIngresso) {
			
			pagamentoService.estornarPagamentoIngresso(ingresso);
			
			ingressoRepository.delete(ingresso);
			
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
		
		String indificadorPagamento = pagamentoService.realizarPagamentoCartao(dto.getPagamento(), evento.getValor());
		
		ingresso.setIdentificadorTransacaoPagamento(indificadorPagamento);
		
		ingresso = ingressoRepository.save(ingresso);
		
		faturamentoService.atualizarValoresFaturamentoPorEvento(evento);
		
		return new IngressoDTO(ingresso);
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
		
		if (evento.getValor().compareTo(0.0) <= 0) {
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
	
}