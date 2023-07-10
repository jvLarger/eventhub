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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jlarger.eventhub.dto.UsuarioAutenticadoDTO;
import com.jlarger.eventhub.dto.UsuarioDTO;
import com.jlarger.eventhub.services.UsuarioService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/publico")
public class PublicoResource {
	
	@Autowired
	private UsuarioService service;
	
	@Value("${imagens.diretorio}")
    private String diretorioImagens;
	
	@PostMapping("/login")
	public ResponseEntity<UsuarioAutenticadoDTO> login(@RequestBody UsuarioDTO dto) {
		
		UsuarioAutenticadoDTO usuarioAutenticadoDTO = service.login(dto);
		
		return ResponseEntity.ok().body(usuarioAutenticadoDTO);
	}
	
	@PostMapping("/nova-conta")
	public ResponseEntity<UsuarioAutenticadoDTO> novoUsuario(@RequestBody UsuarioDTO dto) {
		
		UsuarioAutenticadoDTO usuarioAutenticadoDTO = service.novoUsuario(dto);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		
		return ResponseEntity.created(uri).body(usuarioAutenticadoDTO);
	}
	
	@PostMapping("/token-valido")
	public ResponseEntity<Boolean> isTokenValido(@RequestBody UsuarioAutenticadoDTO dto) {
		
		Boolean isTokenValido = service.isTokenValido(dto);
		
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
}