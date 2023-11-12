package com.jlarger.eventhub.resources;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jlarger.eventhub.dto.TokenDTO;
import com.jlarger.eventhub.dto.UsuarioAutenticadoDTO;
import com.jlarger.eventhub.dto.UsuarioDTO;
import com.jlarger.eventhub.services.AmizadeService;
import com.jlarger.eventhub.services.PublicacaoComentarioService;
import com.jlarger.eventhub.services.PublicacaoCurtidaService;
import com.jlarger.eventhub.services.TokenService;
import com.jlarger.eventhub.services.UsuarioComentarioService;
import com.jlarger.eventhub.services.UsuarioService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/publico")
public class PublicoResource {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioComentarioService usuarioComentarioService;
	
	@Autowired
	private AmizadeService amizadeService;
	
	@Autowired
	private PublicacaoCurtidaService publicacaoCurtidaService;
	
	@Autowired
	private PublicacaoComentarioService publicacaoComentarioService;
	
	@Autowired
	private TokenService tokenService;
	
	@Value("${imagens.diretorio}")
    private String diretorioImagens;
	
	@PostMapping("/login")
	public ResponseEntity<UsuarioAutenticadoDTO> login(@RequestBody UsuarioDTO dto) {
		
		UsuarioAutenticadoDTO usuarioAutenticadoDTO = usuarioService.login(dto);
		
		return ResponseEntity.ok().body(usuarioAutenticadoDTO);
	}
	
	@PostMapping("/nova-conta")
	public ResponseEntity<UsuarioAutenticadoDTO> novoUsuario(@RequestBody UsuarioDTO dto) {
		
		UsuarioAutenticadoDTO usuarioAutenticadoDTO = usuarioService.novoUsuario(dto);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		
		return ResponseEntity.created(uri).body(usuarioAutenticadoDTO);
	}
	
	@PostMapping("/token-valido")
	public ResponseEntity<Boolean> isTokenValido(@RequestBody UsuarioAutenticadoDTO dto) {
		
		Boolean isTokenValido = usuarioService.isTokenValido(dto);
		
		return ResponseEntity.ok().body(isTokenValido);
	}
	
	@GetMapping("/imagens/{nomeImagem:.+}")
    public ResponseEntity<Resource> exibirImagem(@PathVariable String nomeImagem) throws IOException {
        
		Path imagemPath = Paths.get(diretorioImagens, nomeImagem);
       
        Resource imagemResource = new UrlResource(imagemPath.toUri());

        if (imagemResource.exists() && imagemResource.isReadable()) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagemResource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	@PostMapping("/recuperar-senha")
	public ResponseEntity<TokenDTO> enviarCodigoRecuperacao(@RequestBody TokenDTO tokenDTO) {
		
		TokenDTO token = tokenService.enviarCodigoRecuperacao(tokenDTO);
		
		return ResponseEntity.ok().body(token);
	}
	
	@PostMapping("/amizades")
	public ResponseEntity<String> criarAmizades() {
		
		amizadeService.popularAmizades();
		
		return ResponseEntity.ok().body("foi");
	}
	
	
	@PostMapping("/curtidas")
	public ResponseEntity<String> curtirPublicacoes() {
		
		publicacaoCurtidaService.curtirPublicacoes();
		
		return ResponseEntity.ok().body("foi");
	}
	
	@PostMapping("/comentar-publicacoes")
	public ResponseEntity<String> comentarPublicacoes() {
		
		publicacaoComentarioService.comentarPublicacoes();
		
		return ResponseEntity.ok().body("foi");
	}
	
	@PostMapping("/comentar-usuario")
	public ResponseEntity<String> comentarPerfil() {
		
		usuarioComentarioService.comentarPerfil();
		
		return ResponseEntity.ok().body("foi");
	}
	
	@GetMapping("/validar-token/{codigo}/{email}")
	public ResponseEntity<TokenDTO> validarCodigoRecuperacao(@PathVariable Integer codigo, @PathVariable String email) {
		
		TokenDTO token = tokenService.validarCodigoRecuperacao(codigo, email);
		
		return ResponseEntity.ok().body(token);
	}
	
	@PutMapping("/nova-senha")
	public ResponseEntity<?> gerarNovaSenhaUsuario(@RequestBody TokenDTO tokenDTO) {
		
		tokenService.gerarNovaSenhaUsuario(tokenDTO);
		
		return ResponseEntity.noContent().build();
	}
	
}