package com.jlarger.eventhub.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.CategoriaDTO;
import com.jlarger.eventhub.entities.Categoria;
import com.jlarger.eventhub.repositories.CategoriaRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Transactional(readOnly = true)
	public Categoria getCategoria(Long idCategoria) {
		
		validarIdCategoriaInformado(idCategoria);
		
		Optional<Categoria> optionalCategoria = categoriaRepository.findById(idCategoria);

		Categoria categoria = optionalCategoria.orElseThrow(() -> new BusinessException("Categoria não encontrada"));

		return categoria;
	}

	private void validarIdCategoriaInformado(Long idCategoria) {
		
		if (idCategoria == null || idCategoria.compareTo(0L) <= 0) {
			throw new BusinessException("Categoria não informada");
		}
		
	}
	
	@Transactional(readOnly = true)
	public List<CategoriaDTO> buscarCategorias() {
		
		List<Categoria> listaCategoria = categoriaRepository.buscarCategoriasOrdenadas();
		
		return listaCategoria.stream().map(x -> new CategoriaDTO(x)).collect(Collectors.toList());
	}

}