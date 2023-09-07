package com.jlarger.eventhub.dto;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jlarger.eventhub.entities.Evento;

public class EventoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
    private UsuarioDTO usuario;
	private String nome;
	private Date data;
    private LocalTime horaInicio;
	private Double valor;
	private String descricao;
	private String cep;
	private String cidade;
	private String estado;
	private String logradouro;
	private String bairro;
	private String complemento;
	private String numero;
	private Double latitude;
	private Double longitude;
	private Boolean restrito;
	private List<EventoArquivoDTO> arquivos = new ArrayList<EventoArquivoDTO>();
	private List<EventoCategoriaDTO> categorias = new ArrayList<EventoCategoriaDTO>();

	public EventoDTO() {
	}
	
	public EventoDTO(Long id, UsuarioDTO usuario, String nome, Date data, LocalTime horaInicio, Double valor,String descricao, String cep, String cidade, String estado, String logradouro, String bairro, String complemento, String numero, Double latitude, Double longitude, Boolean restrito, List<EventoArquivoDTO> arquivos, List<EventoCategoriaDTO> categorias) {
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
		this.arquivos = arquivos;
		this.categorias = categorias;
	}

	public EventoDTO(Evento evento) {
		this.id = evento.getId();
		this.usuario = new UsuarioDTO(evento.getUsuario());
		this.nome = evento.getNome();
		this.data = evento.getData();
		this.horaInicio = evento.getHoraInicio();
		this.valor = evento.getValor();
		this.descricao = evento.getDescricao();
		this.cep = evento.getCep();
		this.cidade = evento.getCidade();
		this.estado = evento.getEstado();
		this.logradouro = evento.getLogradouro();
		this.bairro = evento.getBairro();
		this.complemento = evento.getComplemento();
		this.numero = evento.getNumero();
		this.latitude = evento.getLatitude();
		this.longitude = evento.getLongitude();
		this.restrito = evento.getRestrito();
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public UsuarioDTO getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioDTO usuario) {
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

	public List<EventoArquivoDTO> getArquivos() {
		return arquivos;
	}

	public void setArquivos(List<EventoArquivoDTO> arquivos) {
		this.arquivos = arquivos;
	}

	public List<EventoCategoriaDTO> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<EventoCategoriaDTO> categorias) {
		this.categorias = categorias;
	}

}