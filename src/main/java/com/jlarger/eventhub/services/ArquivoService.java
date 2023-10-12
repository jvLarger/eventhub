package com.jlarger.eventhub.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
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
	
	@Transactional
	public void excluirArquivo(Arquivo arquivo) {
		
		if (arquivo.getId() == null) {
			throw new BusinessException("Arquivo não informado!");
		}
		
		arquivo = getArquivo(arquivo.getId());
		
		File file = new File(diretorioImagens + "/" + arquivo.getNomeAbsoluto());
		file.delete();
        
        arquivoRepository.delete(arquivo);
	}
	
	@Transactional
	public Arquivo gerarQrcode(String textoQrcode) {
		
		try {
           
			int width = 300;
            int height = 300;
            
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(textoQrcode, BarcodeFormat.QR_CODE, width, height);
            
            String nomeArquivo = "qrcode_" + new Date().getTime() + ".png";

    		String filePath = diretorioImagens + "/" + nomeArquivo;
    		
            Path path = FileSystems.getDefault().getPath(filePath);

            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
            
            Arquivo arquivo = new Arquivo();
    		arquivo.setNome(nomeArquivo);
    		arquivo.setNomeAbsoluto(nomeArquivo);
    		
    		arquivo = arquivoRepository.save(arquivo);
    		
    		return arquivo;
        
		} catch (Exception e) {
        	e.printStackTrace();
            throw new BusinessException("Erro ao gerar o Qrcode: " + e.getMessage());
        }

	}
	
}