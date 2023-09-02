package com.jlarger.eventhub.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PublicacaoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private UsuarioDTO usuario;
	private String descricao;
	private LocalDateTime data;
	private Integer curtidas;
	private List<PublicacaoComentarioDTO> listaComentarios = new ArrayList<PublicacaoComentarioDTO>();
	private List<PublicacaArquivoDTO> listaArquivos = new ArrayList<PublicacaArquivoDTO>();

	public PublicacaoDTO() {
	}

	public PublicacaoDTO(Long id, UsuarioDTO usuario, String descricao, LocalDateTime data, Integer curtidas, List<PublicacaoComentarioDTO> listaComentarios, List<PublicacaArquivoDTO> listaArquivos) {
		this.id = id;
		this.usuario = usuario;
		this.descricao = descricao;
		this.data = data;
		this.curtidas = curtidas;
		this.listaComentarios = listaComentarios;
		this.listaArquivos = listaArquivos;
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public Integer getCurtidas() {
		return curtidas;
	}

	public void setCurtidas(Integer curtidas) {
		this.curtidas = curtidas;
	}

	public List<PublicacaoComentarioDTO> getListaComentarios() {
		return listaComentarios;
	}

	public void setListaComentarios(List<PublicacaoComentarioDTO> listaComentarios) {
		this.listaComentarios = listaComentarios;
	}

	public List<PublicacaArquivoDTO> getListaArquivos() {
		return listaArquivos;
	}

	public void setListaArquivos(List<PublicacaArquivoDTO> listaArquivos) {
		this.listaArquivos = listaArquivos;
	}
	
}