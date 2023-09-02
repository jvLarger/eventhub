package com.jlarger.eventhub.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.ArquivoDTO;
import com.jlarger.eventhub.entities.Arquivo;
import com.jlarger.eventhub.repositories.ArquivoRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;

@Service
public class ArquivoService {

	@Value("${imagens.diretorio}")
	private String diretorioImagens;
	
	@Autowired
	private ArquivoRepository arquivoRepository;
	
	public ArquivoDTO uploadBase64ToFile(ArquivoDTO dto) {
		
		if (dto.getBase64() == null || dto.getBase64().trim().isEmpty()) {
			throw new BusinessException("Arquivo não informado!");
		}
		
		String nomeArquivo = new Date().getTime() + ".jpg";

		String filePath = diretorioImagens + "/" + nomeArquivo;

		try {

			byte[] decodedBytes = Base64.getDecoder().decode(dto.getBase64());


			try (FileOutputStream fos = new FileOutputStream(filePath)) {
				fos.write(decodedBytes);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Arquivo arquivo = new Arquivo();
		arquivo.setNome(nomeArquivo);
		arquivo.setNomeAbsoluto(nomeArquivo);
		
		arquivo = arquivoRepository.save(arquivo);
		
		return new ArquivoDTO(arquivo);
	}
	
	@Transactional
	public Arquivo getArquivo(Long id) {
		
		Optional<Arquivo> optionalArquivo = arquivoRepository.findById(id);

		Arquivo arquivo = optionalArquivo.orElseThrow(() -> new BusinessException("Arquivo não encontrado"));

		return arquivo;
	}
	
}