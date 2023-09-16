package com.jlarger.eventhub.entities;

import java.time.LocalTime;
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
@Table(name = "evento")
public class Evento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable=false)
    private Usuario usuario;
	
	@Column(nullable=false, length = 60)
	private String nome;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date data;
	
	@Column(nullable=false)
    @Temporal(TemporalType.TIME)
    private LocalTime horaInicio;
	
	@Column(nullable=false)
	private Double valor;
	
    @Column(columnDefinition = "TEXT", nullable=false)
	private String descricao;
    
    @Column(nullable=false, length = 8)
	private String cep;
    
    @Column(nullable=false, length = 255)
	private String cidade;
    
    @Column(nullable=false, length = 2)
	private String estado;
    
    @Column(nullable=false, length = 255)
	private String logradouro;
    
    @Column(nullable=false, length = 255)
	private String bairro;
    
    @Column(nullable=true, length = 255)
	private String complemento;
    
    @Column(nullable=false, length = 10)
	private String numero;
    
    @Column(nullable=false)
	private Double latitude;
    
    @Column(nullable=false)
	private Double longitude;
    
    @Column(nullable=false)
	private Boolean restrito;
    
    @Column(nullable=false)
    private Integer numeroMaximoIngressos;
    
    @Column(nullable=false)
    private Integer numeroVisualizacoes;
    
    @Column(nullable=false)
    private Boolean visivel;
    
    public Evento() {
	}

	public Evento(Long id, Usuario usuario, String nome, Date data, LocalTime horaInicio, LocalTime horaTermino,
			Double valor, String descricao, String cep, String cidade, String estado, String logradouro, String bairro,
			String complemento, String numero, Double latitude, Double longitude, Boolean restrito, Integer numeroMaximoIngressos, Integer numeroVisualizacoes, Boolean visivel) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.nome = nome;
		this.data = data;
		this.horaInicio = horaInicio;
		this.valor = valor;
		this.descricao = descricao;
		this.cep = cep;
		this.cidade = cidade;
		this.estado = estado;
		this.logradouro = logradouro;
		this.bairro = bairro;
		this.complemento = complemento;
		this.numero = numero;
		this.latitude = latitude;
		this.longitude = longitude;
		this.restrito = restrito;
		this.numeroMaximoIngressos = numeroMaximoIngressos;
		this.numeroVisualizacoes = numeroVisualizacoes;
		this.visivel = visivel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Boolean getRestrito() {
		return restrito;
	}

	public void setRestrito(Boolean restrito) {
		this.restrito = restrito;
	}

	public Integer getNumeroMaximoIngressos() {
		return numeroMaximoIngressos;
	}

	public void setNumeroMaximoIngressos(Integer numeroMaximoIngressos) {
		this.numeroMaximoIngressos = numeroMaximoIngressos;
	}

	public Integer getNumeroVisualizacoes() {
		return numeroVisualizacoes;
	}

	public void setNumeroVisualizacoes(Integer numeroVisualizacoes) {
		this.numeroVisualizacoes = numeroVisualizacoes;
	}

	public Boolean getVisivel() {
		return visivel;
	}

	public void setVisivel(Boolean visivel) {
		this.visivel = visivel;
	}

	@Override
	public int hashCode() {
		return Objects.hash(bairro, cep, cidade, complemento, data, descricao, estado, horaInicio, id, latitude,
				logradouro, longitude, nome, numero, numeroMaximoIngressos, numeroVisualizacoes, restrito, usuario,
				valor, visivel);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Evento other = (Evento) obj;
		return Objects.equals(bairro, other.bairro) && Objects.equals(cep, other.cep)
				&& Objects.equals(cidade, other.cidade) && Objects.equals(complemento, other.complemento)
				&& Objects.equals(data, other.data) && Objects.equals(descricao, other.descricao)
				&& Objects.equals(estado, other.estado) && Objects.equals(horaInicio, other.horaInicio)
				&& Objects.equals(id, other.id) && Objects.equals(latitude, other.latitude)
				&& Objects.equals(logradouro, other.logradouro) && Objects.equals(longitude, other.longitude)
				&& Objects.equals(nome, other.nome) && Objects.equals(numero, other.numero)
				&& Objects.equals(numeroMaximoIngressos, other.numeroMaximoIngressos)
				&& Objects.equals(numeroVisualizacoes, other.numeroVisualizacoes)
				&& Objects.equals(restrito, other.restrito) && Objects.equals(usuario, other.usuario)
				&& Objects.equals(valor, other.valor) && Objects.equals(visivel, other.visivel);
	}

	@Override
	public String toString() {
		return "Evento [id=" + id + ", usuario=" + usuario + ", nome=" + nome + ", data=" + data + ", horaInicio="
				+ horaInicio + ", valor=" + valor + ", descricao=" + descricao + ", cep=" + cep + ", cidade=" + cidade
				+ ", estado=" + estado + ", logradouro=" + logradouro + ", bairro=" + bairro + ", complemento="
				+ complemento + ", numero=" + numero + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", restrito=" + restrito + ", numeroMaximoIngressos=" + numeroMaximoIngressos
				+ ", numeroVisualizacoes=" + numeroVisualizacoes + ", visivel=" + visivel + "]";
	}
	
}