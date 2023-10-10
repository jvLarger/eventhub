package com.jlarger.eventhub.entities;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "usuario")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique=true, nullable=false, length = 60)
	private String nomeUsuario;
	
	@Column(unique=true, nullable=false, length = 60)
	private String email;
	
	@JsonIgnore
	@Column(nullable=false, length = 60)
	private String senha;
	
	@Column(nullable=false, length = 255)
	private String nomeCompleto;
	
	@Column(nullable=true, length = 20)
	private String documentoPrincipal;
	
	@Column(nullable=true, length = 15)
	private String telefone;
	
	@Column(columnDefinition = "TEXT", length = 1000, nullable = true)
	private String identificadorNotificacao;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=true)
	private Date dataComemorativa;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_foto", nullable=true)
    private Arquivo foto;
	
	@Column(nullable=true, length = 255)
	private String identificadorContaBancaria;
	
	public Usuario() {
	}
	
	public Usuario(Long id, String nomeUsuario, String email, String senha, String nomeCompleto,
			String documentoPrincipal, String telefone, Date dataComemorativa, Arquivo foto, String identificadorNotificacao, String identificadorContaBancaria) {
		super();
		this.id = id;
		this.nomeUsuario = nomeUsuario;
		this.email = email;
		this.senha = senha;
		this.nomeCompleto = nomeCompleto;
		this.documentoPrincipal = documentoPrincipal;
		this.telefone = telefone;
		this.dataComemorativa = dataComemorativa;
		this.foto = foto;
		this.identificadorNotificacao = identificadorNotificacao;
		this.identificadorContaBancaria = identificadorContaBancaria;
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
	
	public Arquivo getFoto() {
		return foto;
	}

	public void setFoto(Arquivo foto) {
		this.foto = foto;
	}
	
	public String getIdentificadorNotificacao() {
		return identificadorNotificacao;
	}

	public void setIdentificadorNotificacao(String identificadorNotificacao) {
		this.identificadorNotificacao = identificadorNotificacao;
	}

	public String getIdentificadorContaBancaria() {
		return identificadorContaBancaria;
	}

	public void setIdentificadorContaBancaria(String identificadorContaBancaria) {
		this.identificadorContaBancaria = identificadorContaBancaria;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataComemorativa, documentoPrincipal, email, foto, id, identificadorContaBancaria,
				identificadorNotificacao, nomeCompleto, nomeUsuario, senha, telefone);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(dataComemorativa, other.dataComemorativa)
				&& Objects.equals(documentoPrincipal, other.documentoPrincipal) && Objects.equals(email, other.email)
				&& Objects.equals(foto, other.foto) && Objects.equals(id, other.id)
				&& Objects.equals(identificadorContaBancaria, other.identificadorContaBancaria)
				&& Objects.equals(identificadorNotificacao, other.identificadorNotificacao)
				&& Objects.equals(nomeCompleto, other.nomeCompleto) && Objects.equals(nomeUsuario, other.nomeUsuario)
				&& Objects.equals(senha, other.senha) && Objects.equals(telefone, other.telefone);
	}
	
}