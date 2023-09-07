package com.jlarger.eventhub.services;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.entities.Evento;
import com.jlarger.eventhub.entities.Faturamento;
import com.jlarger.eventhub.repositories.FaturamentoRepository;

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
}