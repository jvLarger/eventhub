package com.jlarger.eventhub.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jlarger.eventhub.dto.CategoriaDTO;
import com.jlarger.eventhub.services.CategoriaService;

@RestController
@RequestMapping(value = "api/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService categoriaService;
	
	@GetMapping
	public ResponseEntity<List<CategoriaDTO>> buscarCategorias() {
		
		List<CategoriaDTO> listaCategorias = categoriaService.buscarCategorias();
		
		return ResponseEntity.ok().body(listaCategorias);
	}
	
	@GetMapping("/populares")
	public ResponseEntity<List<CategoriaDTO>> buscarCategoriasMaisUtilizadas() {
		
		List<CategoriaDTO> listaCategorias = categoriaService.buscarCategoriasMaisUtilizadas();
		
		return ResponseEntity.ok().body(listaCategorias);
	}
	
}