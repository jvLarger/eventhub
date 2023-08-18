package com.jlarger.eventhub.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.jlarger.eventhub.entities.Token;
import com.jlarger.eventhub.entities.Usuario;

public class TokenDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
    private UsuarioDTO usuario;
	private Integer codigo;
    private LocalDateTime dataExpiracao;
    
    public TokenDTO() {
    }
    
	public TokenDTO(Long id, Usuario usuario, Integer codigo, LocalDateTime dataExpiracao) {
		super();
		this.id = id;
		this.usuario = new UsuarioDTO(usuario);
		this.codigo = codigo;
		this.dataExpiracao = dataExpiracao;
	}
	
	public TokenDTO(Token token) {
		super();
		this.id = token.getId();
		this.codigo = token.getCodigo();
		this.dataExpiracao = token.getDataExpiracao();
	}
	
	public TokenDTO(Token token, Usuario usuario) {
		super();
		this.id = token.getId();
		this.codigo = token.getCodigo();
		this.dataExpiracao = token.getDataExpiracao();
		this.usuario = new UsuarioDTO(usuario);
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
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public LocalDateTime getDataExpiracao() {
		return dataExpiracao;
	}
	public void setDataExpiracao(LocalDateTime dataExpiracao) {
		this.dataExpiracao = dataExpiracao;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(codigo, dataExpiracao, id, usuario);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TokenDTO other = (TokenDTO) obj;
		return Objects.equals(codigo, other.codigo) && Objects.equals(dataExpiracao, other.dataExpiracao)
				&& Objects.equals(id, other.id) && Objects.equals(usuario, other.usuario);
	}

}