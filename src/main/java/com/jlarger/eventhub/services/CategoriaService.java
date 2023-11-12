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

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private EntityManager entityManager;
	
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

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<CategoriaDTO> buscarCategoriasMaisUtilizadas() {

		String sql = "SELECT c.* FROM apresentacao.categoria c INNER JOIN apresentacao.evento_categoria ec ON c.id = ec.id_categoria GROUP BY c.nome, c.id ORDER BY COUNT(ec.id) DESC";

		Query query = entityManager.createNativeQuery(sql, Categoria.class);
		query.setMaxResults(5);

		List<Categoria> listaCategoria = query.getResultList();

		return listaCategoria.stream().map(x -> new CategoriaDTO(x)).collect(Collectors.toList());
	}

}