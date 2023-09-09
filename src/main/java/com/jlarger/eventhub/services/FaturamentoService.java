package com.jlarger.eventhub.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.entities.Evento;
import com.jlarger.eventhub.entities.Faturamento;
import com.jlarger.eventhub.repositories.FaturamentoRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;

@Service
public class FaturamentoService {
	
	@Autowired
	private FaturamentoRepository faturamentoRepository;
	
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
		
		LocalDate localDate = evento.getData().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
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
}