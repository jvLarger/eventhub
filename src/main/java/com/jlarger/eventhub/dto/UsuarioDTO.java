package com.jlarger.eventhub.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.jlarger.eventhub.entities.Arquivo;
import com.jlarger.eventhub.entities.Usuario;

public class UsuarioDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nomeUsuario;
	private String email;
	private String senha;
	private String nomeCompleto;
	private String documentoPrincipal;
	private String telefone;
	private Date dataComemorativa;
	private Boolean isAmigo;
	private ArquivoDTO foto;
	private Boolean isSolicitacaoAmizadePendente;

	public UsuarioDTO() {
		
	}

	public UsuarioDTO(Long id, String nomeUsuario, String email, String senha, String nomeCompleto, String documentoPrincipal, String telefone, Date dataComemorativa, Arquivo foto, Boolean isAmigo, Boolean isSolicitacaoAmizadePendente) {
		this.id = id;
		this.nomeUsuario = nomeUsuario;
		this.email = email;
		this.senha = senha;
		this.nomeCompleto = nomeCompleto;
		this.documentoPrincipal = documentoPrincipal;
		this.telefone = telefone;
		this.dataComemorativa = dataComemorativa;
		this.isAmigo = isAmigo;
		this.foto = new ArquivoDTO(foto);
		this.isSolicitacaoAmizadePendente = isSolicitacaoAmizadePendente;
	}
	
	public UsuarioDTO(Usuario usuario) {
		this.id = usuario.getId();
		this.nomeUsuario = usuario.getNomeUsuario();
		this.email = usuario.getEmail();
		this.senha = usuario.getSenha();
		this.nomeCompleto = usuario.getNomeCompleto();
		this.documentoPrincipal = usuario.getDocumentoPrincipal();
		this.telefone = usuario.getTelefone();
		this.dataComemorativa = usuario.getDataComemorativa();
		if (usuario.getFoto() != null) {
			this.foto = new ArquivoDTO(usuario.getFoto());
		}
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
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

	public ArquivoDTO getFoto() {
		return foto;
	}

	public void setFoto(ArquivoDTO foto) {
		this.foto = foto;
	}
	
	public Boolean getIsAmigo() {
		return isAmigo;
	}

	public void setIsAmigo(Boolean isAmigo) {
		this.isAmigo = isAmigo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Boolean getIsSolicitacaoAmizadePendente() {
		return isSolicitacaoAmizadePendente;
	}

	public void setIsSolicitacaoAmizadePendente(Boolean isSolicitacaoAmizadePendente) {
		this.isSolicitacaoAmizadePendente = isSolicitacaoAmizadePendente;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataComemorativa, documentoPrincipal, email, foto, id, nomeCompleto, nomeUsuario, senha,
				telefone);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioDTO other = (UsuarioDTO) obj;
		return Objects.equals(dataComemorativa, other.dataComemorativa)
				&& Objects.equals(documentoPrincipal, other.documentoPrincipal) && Objects.equals(email, other.email)
				&& Objects.equals(foto, other.foto) && Objects.equals(id, other.id)
				&& Objects.equals(nomeCompleto, other.nomeCompleto) && Objects.equals(nomeUsuario, other.nomeUsuario)
				&& Objects.equals(senha, other.senha) && Objects.equals(telefone, other.telefone);
	}

}