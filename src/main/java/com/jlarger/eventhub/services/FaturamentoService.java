package com.jlarger.eventhub.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.FaturamentoDTO;
import com.jlarger.eventhub.dto.FaturamentoPagamentoDTO;
import com.jlarger.eventhub.entities.Evento;
import com.jlarger.eventhub.entities.Faturamento;
import com.jlarger.eventhub.entities.Ingresso;
import com.jlarger.eventhub.entities.Usuario;
import com.jlarger.eventhub.repositories.FaturamentoRepository;
import com.jlarger.eventhub.repositories.IngressoRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.ServiceLocator;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;

@Service
public class FaturamentoService {
	
	@Autowired
	private FaturamentoRepository faturamentoRepository;
	
	@Autowired
	private IngressoRepository ingressoRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PagamentoService pagamentoService;
	
	@Transactional
	public Faturamento criarFaturamentoInicialParaEvento(Evento evento) {
		
		Faturamento faturamento = new Faturamento();
		faturamento.setDataLiberacao(calcularDataLiberacao(evento));
		faturamento.setValorTotalFaturamento(0.0);
		faturamento.setValorTotalIngressos(0.0);
		faturamento.setValorTotalTaxas(0.0);
		faturamento.setEvento(evento);
		
		faturamento = faturamentoRepository.save(faturamento);
		
		return faturamento;
	}
	
	private LocalDateTime calcularDataLiberacao(Evento evento) {
		
		LocalDate localDate = new java.util.Date(evento.getData().getTime()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
		LocalDateTime localDateTime = localDate.atTime(evento.getHoraInicio());

		LocalDateTime dataLiberacao = localDateTime.plusDays(10);
		
		return dataLiberacao;
	}
	
	@Transactional
	public Faturamento tratarEAtualizarDataLiberacaoPagamento(Evento evento) {
		
		Faturamento faturamento = getFaturamentoPorEvento(evento);
		
		LocalDateTime dataLiberacao = calcularDataLiberacao(evento);
		
		if (dataLiberacao.compareTo(faturamento.getDataLiberacao()) != 0) {
			faturamento.setDataLiberacao(dataLiberacao);
			faturamento = faturamentoRepository.save(faturamento);
		}
		
		return faturamento;
	}

	public Faturamento getFaturamentoPorEvento(Evento evento) {
		
		Optional<Faturamento> optionalFaturamento = faturamentoRepository.buscarFaturamentoPorEvento(evento.getId());
		
		Faturamento faturamento = optionalFaturamento.orElseThrow(() -> new BusinessException("Faturamento não encontrado"));

		return faturamento;
	}
	
	@Transactional
	public void excluirFaturamentoPorEvento(Long idEvento) {
		
		validarEventoInformado(idEvento);
		
		Optional<Faturamento> optionalFaturamento = faturamentoRepository.buscarFaturamentoPorEvento(idEvento);
		
		if (optionalFaturamento.isPresent()) {
			
			Faturamento faturamento = optionalFaturamento.get();
			
			faturamentoRepository.delete(faturamento);
			
		}
		
	}
	
	private void validarEventoInformado(Long idEvento) {
		
		if (idEvento == null || idEvento.compareTo(0L) <= 0) {
			throw new BusinessException("Evento não informado!");
		}
		
	}
	
	@Transactional
	public void atualizarValoresFaturamentoPorEvento(Evento evento) {
		
		Faturamento faturamento = getFaturamentoPorEvento(evento);
		
		List<Ingresso> listaIngresso = ingressoRepository.buscarIngressosPorEvento(evento.getId());
		
		Double valorTotalIngressos = 0.0;
		Double valorTotalTaxas = 0.0;
		Double valorTotalFaturamento = 0.0;

		for (Ingresso ingresso : listaIngresso) {
			valorTotalIngressos += ingresso.getValorTotalIngresso();
			valorTotalTaxas += ingresso.getValorTaxa();
			valorTotalFaturamento += ingresso.getValorFaturamento();
		}
		
		faturamento.setValorTotalIngressos(valorTotalIngressos);
		faturamento.setValorTotalTaxas(valorTotalTaxas);
		faturamento.setValorTotalFaturamento(valorTotalFaturamento);
		
		faturamentoRepository.save(faturamento);
	}

	@Transactional
	public FaturamentoPagamentoDTO buscarFaturamentos() {
		
		List<Faturamento> listaFaturamentos = faturamentoRepository.buscarFaturamentosPendentes(ServiceLocator.getUsuarioLogado().getId());
		List<Faturamento> faturamentosPagos = faturamentoRepository.buscarFaturamentosPagos(ServiceLocator.getUsuarioLogado().getId());
		List<Faturamento> proximosFaturamentos = new ArrayList<Faturamento>();
		List<Faturamento> faturamentosLiberados = new ArrayList<Faturamento>();

		Double valorTotalIngressos = 0.0;
		Double valorTotalTaxas = 0.0;
		Double valorTotalFaturado = 0.0;
		Double valorTotalIngressosFuturo = 0.0;
		Double valorTotalTaxasFuturo = 0.0;
		Double valorTotalFaturadoFuturo = 0.0;
		
		for (Faturamento faturamento : listaFaturamentos) {
			
			if (faturamento.getDataLiberacao().compareTo(LocalDateTime.now()) <= 0) {
				valorTotalIngressos += faturamento.getValorTotalIngressos();
				valorTotalTaxas += faturamento.getValorTotalTaxas();
				valorTotalFaturado += faturamento.getValorTotalFaturamento();
				faturamentosLiberados.add(faturamento);
			} else {
				valorTotalIngressosFuturo += faturamento.getValorTotalIngressos();
				valorTotalTaxasFuturo += faturamento.getValorTotalTaxas();
				valorTotalFaturadoFuturo += faturamento.getValorTotalFaturamento();
				proximosFaturamentos.add(faturamento);
			}
			
		}
		
		FaturamentoPagamentoDTO faturamentoPagamentoDTO = new FaturamentoPagamentoDTO();
		faturamentoPagamentoDTO.setValorTotalIngressos(valorTotalIngressos);
		faturamentoPagamentoDTO.setValorTotalTaxas(valorTotalTaxas);
		faturamentoPagamentoDTO.setValorTotalFaturado(valorTotalFaturado);
		faturamentoPagamentoDTO.setValorTotalIngressosFuturo(valorTotalIngressosFuturo);
		faturamentoPagamentoDTO.setValorTotalTaxasFuturo(valorTotalTaxasFuturo);
		faturamentoPagamentoDTO.setValorTotalFaturadoFuturo(valorTotalFaturadoFuturo);
		faturamentoPagamentoDTO.setProximosFaturamentos(proximosFaturamentos.stream().map(x -> new FaturamentoDTO(x, x.getEvento())).collect(Collectors.toList()));
		faturamentoPagamentoDTO.setFaturamentosLiberados(faturamentosLiberados.stream().map(x -> new FaturamentoDTO(x, x.getEvento())).collect(Collectors.toList()));
		faturamentoPagamentoDTO.setFaturamentosPagos(faturamentosPagos.stream().map(x -> new FaturamentoDTO(x, x.getEvento())).collect(Collectors.toList()));
		
		buscarEPopularInformacoesContaBancariaTransferencia(faturamentoPagamentoDTO);
		
		return faturamentoPagamentoDTO;
	}
	
	@Transactional
	private void buscarEPopularInformacoesContaBancariaTransferencia(FaturamentoPagamentoDTO faturamentoPagamentoDTO) {
		
		Usuario usuarioLogado = usuarioService.getUsuarioLogado();
		
		if (usuarioLogado.getIdentificadorContaBancaria() != null && !usuarioLogado.getIdentificadorContaBancaria().trim().isEmpty()) {
			
			popularInformacoesContaBancaria(faturamentoPagamentoDTO, usuarioLogado.getIdentificadorContaBancaria());
			
		} else {
			
			Account account = pagamentoService.createExternalAccount();
		
			usuarioService.atualizarIdentificadorContaBancariaUsuarioLogado(account.getId());
			
			popularInformacoesContaBancaria(faturamentoPagamentoDTO, account.getId());
		}
		
	}
	
	@Transactional
	private void popularInformacoesContaBancaria(FaturamentoPagamentoDTO faturamentoPagamentoDTO, String identificadorContaBancaria) {
		
		Account account = pagamentoService.getExternalAccount(identificadorContaBancaria);
		
		if (!account.getPayoutsEnabled()) {
			
			AccountLink accountLink = pagamentoService.createLinkExternalAccount(account.getId());
			faturamentoPagamentoDTO.setLinkConnectStripe(accountLink.getUrl());
			
		}
		
		faturamentoPagamentoDTO.setPayoutsEnabled(account.getPayoutsEnabled());
		faturamentoPagamentoDTO.setEmail(account.getEmail());
		faturamentoPagamentoDTO.setAccount(account.getId());
		
	}
	
	@Transactional
	public void pagarFechamentosLiberados() {
		
		List<Faturamento> listaFaturamentosLiberados = faturamentoRepository.buscarFaturamentosLiberadosENaoPagosPorUsuario(ServiceLocator.getUsuarioLogado().getId());
		
		validarSeExisteValorDisponivel(listaFaturamentosLiberados);
		
		validarSeExisteAccountValida();
		
		List<Long> listaIdsEvento = getListaIdsEvento(listaFaturamentosLiberados);
		
		List<Ingresso> listaIngressos = ingressoRepository.buscarIngressosPorListaIdsEventos(listaIdsEvento);
		
		Usuario usuario = usuarioService.getUsuarioLogado();
		
		for (Ingresso ingresso : listaIngressos) {
			
			if (ingresso.getIdentificadorTransacaoPagamento() != null && !usuario.getIdentificadorContaBancaria().trim().isEmpty()) {
				
				pagamentoService.createTransfer(usuario.getIdentificadorContaBancaria(), ingresso.getValorFaturamento(), ingresso.getIdentificadorTransacaoPagamento());
				
			}
			
		}
		
		for (Faturamento faturamento : listaFaturamentosLiberados) {
			faturamento.setDataPagamento(LocalDateTime.now());
			faturamentoRepository.save(faturamento);
		}
		
	}

	private void validarSeExisteAccountValida() {
		
		Usuario usuario = usuarioService.getUsuarioLogado();
		
		if (usuario.getIdentificadorContaBancaria() == null || usuario.getIdentificadorContaBancaria().trim().isEmpty()) {
			throw new BusinessException("Não existe conta bancária configurada para repasse.");
		}
		
		Account account = pagamentoService.getExternalAccount(usuario.getIdentificadorContaBancaria());
		
		if (!account.getPayoutsEnabled()) {
			throw new BusinessException("Não existe conta bancária configurada para repasse.");
		}
		
	}

	private List<Long> getListaIdsEvento(List<Faturamento> listaFaturamentosLiberados) {
		
		List<Long> listaIdsEvento = new ArrayList<Long>();
		
		for (Faturamento faturamento : listaFaturamentosLiberados) {
			listaIdsEvento.add(faturamento.getEvento().getId());
		}
		
		return listaIdsEvento;
	}

	private void validarSeExisteValorDisponivel(List<Faturamento> listaFaturamentosLiberados) {
		
		Double valorTotal = 0.0;
		
		for (Faturamento faturamento : listaFaturamentosLiberados) {
			valorTotal += faturamento.getValorTotalFaturamento();
		}
		
		if (valorTotal.compareTo(0.0) <= 0) {
			throw new BusinessException("Não existe valor para ser repassado nesse momento.");
		}
		
	}
	
	@Transactional
	public void removerConnectedAccount() {
		
		Usuario usuarioLogado = usuarioService.getUsuarioLogado();
		
		if (usuarioLogado.getIdentificadorContaBancaria() != null && !usuarioLogado.getIdentificadorContaBancaria().trim().isEmpty()) {
			
		//	pagamentoService.deleteAccount(usuarioLogado.getIdentificadorContaBancaria());
			
			usuarioService.atualizarIdentificadorContaBancariaUsuarioLogado(null);
			
		}
		
	}
	
	
}