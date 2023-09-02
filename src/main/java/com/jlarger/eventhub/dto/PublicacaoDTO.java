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
	private Boolean isMinhaPublicacao;
	private Boolean isCurti;
	private List<PublicacaoComentarioDTO> comentarios = new ArrayList<PublicacaoComentarioDTO>();
	private List<PublicacaoArquivoDTO> arquivos = new ArrayList<PublicacaoArquivoDTO>();

	public PublicacaoDTO() {
	}

	public PublicacaoDTO(Long id, UsuarioDTO usuario, String descricao, LocalDateTime data, Integer curtidas, List<PublicacaoComentarioDTO> listaComentarios, List<PublicacaoArquivoDTO> listaArquivos, Boolean isMinhaPublicacao, Boolean isCurti) {
		this.id = id;
		this.usuario = usuario;
		this.descricao = descricao;
		this.data = data;
		this.curtidas = curtidas;
		this.comentarios = listaComentarios;
		this.arquivos = listaArquivos;
		this.isCurti = isCurti;
		this.isMinhaPublicacao = isMinhaPublicacao;
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

	public List<PublicacaoComentarioDTO> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<PublicacaoComentarioDTO> comentarios) {
		this.comentarios = comentarios;
	}

	public List<PublicacaoArquivoDTO> getArquivos() {
		return arquivos;
	}

	public void setArquivos(List<PublicacaoArquivoDTO> arquivos) {
		this.arquivos = arquivos;
	}

	public Boolean getIsMinhaPublicacao() {
		return isMinhaPublicacao;
	}

	public void setIsMinhaPublicacao(Boolean isMinhaPublicacao) {
		this.isMinhaPublicacao = isMinhaPublicacao;
	}

	public Boolean getIsCurti() {
		return isCurti;
	}

	public void setIsCurti(Boolean isCurti) {
		this.isCurti = isCurti;
	}

}