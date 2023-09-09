package com.jlarger.eventhub.dto;

import java.io.Serializable;
import java.util.Date;

import com.jlarger.eventhub.entities.Evento;
import com.jlarger.eventhub.entities.Ingresso;

public class IngressoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private EventoDTO evento;
	private String email;
	private String nome;
	private String documentoPrincipal;
	private String telefone;
	private Date dataComemorativa;
	private Double valorTotalIngresso;
	private Double valorTaxa;
	private Double valorFaturamento;
	private String identificadorTransacaoPagamento;
    private UsuarioDTO usuario;
    
    public IngressoDTO() {
    }

	public IngressoDTO(EventoDTO evento, Long id, String email, String nome, String documentoPrincipal, String telefone, Date dataComemorativa, Double valorTotalIngresso, Double valorTaxa, Double valorFaturamento, String identificadorTransacaoPagamento, UsuarioDTO usuario) {
		this.id = id;
		this.evento = evento;
		this.email = email;
		this.nome = nome;
		this.documentoPrincipal = documentoPrincipal;
		this.telefone = telefone;
		this.dataComemorativa = dataComemorativa;
		this.valorTotalIngresso = valorTotalIngresso;
		this.valorTaxa = valorTaxa;
		this.valorFaturamento = valorFaturamento;
		this.identificadorTransacaoPagamento = identificadorTransacaoPagamento;
		this.usuario = usuario;
	}
	
	public IngressoDTO(Ingresso ingresso) {
		this.id = ingresso.getId();
		this.email = ingresso.getEmail();
		this.nome = ingresso.getNome();
		this.documentoPrincipal = ingresso.getDocumentoPrincipal();
		this.telefone = ingresso.getTelefone();
		this.dataComemorativa = ingresso.getDataComemorativa();
		this.valorTotalIngresso = ingresso.getValorTotalIngresso();
		this.valorTaxa = ingresso.getValorTaxa();
		this.valorFaturamento = ingresso.getValorFaturamento();
		this.identificadorTransacaoPagamento = ingresso.getIdentificadorTransacaoPagamento();
		this.usuario = new UsuarioDTO(ingresso.getUsuario());
	}
	
	public IngressoDTO(Ingresso ingresso, Evento evento) {
		this(ingresso);
		this.evento = new EventoDTO(evento);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDocumentoPrincipal() {
		return documentoPrincipal;
	}

	public void setDocumentoPrincipal(String documentoPrincipal) {
		this.documentoPrincipal = documentoPrincipal;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Date getDataComemorativa() {
		return dataComemorativa;
	}

	public void setDataComemorativa(Date dataComemorativa) {
		this.dataComemorativa = dataComemorativa;
	}

	public Double getValorTotalIngresso() {
		return valorTotalIngresso;
	}

	public void setValorTotalIngresso(Double valorTotalIngresso) {
		this.valorTotalIngresso = valorTotalIngresso;
	}

	public Double getValorTaxa() {
		return valorTaxa;
	}

	public void setValorTaxa(Double valorTaxa) {
		this.valorTaxa = valorTaxa;
	}

	public Double getValorFaturamento() {
		return valorFaturamento;
	}

	public void setValorFaturamento(Double valorFaturamento) {
		this.valorFaturamento = valorFaturamento;
	}

	public String getIdentificadorTransacaoPagamento() {
		return identificadorTransacaoPagamento;
	}

	public void setIdentificadorTransacaoPagamento(String identificadorTransacaoPagamento) {
		this.identificadorTransacaoPagamento = identificadorTransacaoPagamento;
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}

	public EventoDTO getEvento() {
		return evento;
	}

	public void setEvento(EventoDTO evento) {
		this.evento = evento;
	}
    
}