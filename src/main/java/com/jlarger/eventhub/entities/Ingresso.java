package com.jlarger.eventhub.entities;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "ingresso")
public class Ingresso {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", nullable=false)
    private Evento evento;
	
	@Column(nullable=false, length = 60)
	private String email;
	
	@Column(nullable=false, length = 255)
	private String nome;
	
	@Column(nullable=true, length = 20)
	private String documentoPrincipal;
	
	@Column(nullable=false, length = 15)
	private String telefone;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=true)
	private Date dataComemorativa;
	
	@Column(nullable=false)
	private Double valorTotalIngresso;
	
	@Column(nullable=false)
	private Double valorTaxa;
	
	@Column(nullable=false)
	private Double valorFaturamento;
	
	@Column(nullable=true, length = 255)
	private String identificadorTransacaoPagamento;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable=false)
    private Usuario usuario;
	
	public Ingresso() {
	}

	public Ingresso(Long id, Evento evento, String email, String nome, String documentoPrincipal, String telefone,
			Date dataComemorativa, Double valorTotalIngresso, Double valorTaxa, Double valorFaturamento,
			String identificadorTransacaoPagamento) {
		super();
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
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataComemorativa, documentoPrincipal, email, evento, id, identificadorTransacaoPagamento,
				nome, telefone, valorFaturamento, valorTaxa, valorTotalIngresso);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ingresso other = (Ingresso) obj;
		return Objects.equals(dataComemorativa, other.dataComemorativa)
				&& Objects.equals(documentoPrincipal, other.documentoPrincipal) && Objects.equals(email, other.email)
				&& Objects.equals(evento, other.evento) && Objects.equals(id, other.id)
				&& Objects.equals(identificadorTransacaoPagamento, other.identificadorTransacaoPagamento)
				&& Objects.equals(nome, other.nome) && Objects.equals(telefone, other.telefone)
				&& Objects.equals(valorFaturamento, other.valorFaturamento)
				&& Objects.equals(valorTaxa, other.valorTaxa)
				&& Objects.equals(valorTotalIngresso, other.valorTotalIngresso);
	}
	
}