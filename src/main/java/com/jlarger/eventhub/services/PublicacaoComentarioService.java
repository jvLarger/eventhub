package com.jlarger.eventhub.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.PublicacaoComentarioDTO;
import com.jlarger.eventhub.entities.Publicacao;
import com.jlarger.eventhub.entities.PublicacaoComentario;
import com.jlarger.eventhub.entities.Usuario;
import com.jlarger.eventhub.repositories.PublicacaoComentarioRepository;
import com.jlarger.eventhub.repositories.PublicacaoRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.ServiceLocator;

@Service
public class PublicacaoComentarioService {
	
	@Autowired
	private PublicacaoComentarioRepository publicacaoComentarioRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PublicacaoRepository publicacaoRepository;
	
	@Transactional(readOnly = true)
	public List<PublicacaoComentario> buscarComentariosPorPublicacao(Long idPublicacao) {

		validarPublicacaoInformada(idPublicacao);

		List<PublicacaoComentario> listaPublicacaoComentario = publicacaoComentarioRepository.buscarComentariosPorPublicacao(idPublicacao);

		return listaPublicacaoComentario;
	}
	
	@Transactional
	public void excluirComentariosPorPublicacao(Long idPublicacao) {
		
		validarPublicacaoInformada(idPublicacao);
		
		List<PublicacaoComentario> listaPublicacaoComentario = publicacaoComentarioRepository.buscarComentariosPorPublicacao(idPublicacao);
		
		for (PublicacaoComentario publicacaoComentario : listaPublicacaoComentario) {
			publicacaoComentarioRepository.delete(publicacaoComentario);
		}
		
	}

	private void validarPublicacaoInformada(Long idPublicacao) {
		
		if (idPublicacao == null) {
			throw new BusinessException("Publicação não informada!");
		}
		
	}
	
	@Transactional
	public PublicacaoComentarioDTO criarPublicacaoComentario(PublicacaoComentarioDTO dto, Long idPublicacao) {
		
		validarPublicacaoInformada(idPublicacao);
		
		validarCamposObrigatorios(dto);
		
		Usuario usuarioLogado = usuarioService.getUsuarioLogado();
		
		Publicacao publicacao = getPublicacao(idPublicacao);
		
		PublicacaoComentario publicacaoComentario = new PublicacaoComentario();
		publicacaoComentario.setPublicacao(publicacao);
		publicacaoComentario.setData(LocalDateTime.now());
		publicacaoComentario.setDescricao(dto.getDescricao().trim());
		publicacaoComentario.setUsuario(usuarioLogado);
		
		publicacaoComentario = publicacaoComentarioRepository.save(publicacaoComentario);
		
		return new PublicacaoComentarioDTO(publicacaoComentario);
	}
	
	private void validarCamposObrigatorios(PublicacaoComentarioDTO dto) {
		
		if (dto.getDescricao() == null || dto.getDescricao().trim().length() < 10 ) {
			throw new BusinessException("O comentário deve possuir pelo menos 10 caracteres!");
		} 
		
	}

	@Transactional
	public void excluirPublicacaoComentario(Long idPublicacaoComentario) {
		
		if (idPublicacaoComentario == null) {
			throw new BusinessException("Comentário não informado!");
		}
		
		PublicacaoComentario publicacaoComentario = getPublicacaoComentario(idPublicacaoComentario);
		
		if (publicacaoComentario.getUsuario().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0 && publicacaoComentario.getPublicacao().getUsuario().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0 ) {
			throw new BusinessException("Somente o dono da publicação ou do comentário pode realizar sua exclusão!");
		}
		
		publicacaoComentarioRepository.delete(publicacaoComentario);
	}
	
	@Transactional(readOnly = true)
	private PublicacaoComentario getPublicacaoComentario(Long idPublicacaoComentario) {
	
		Optional<PublicacaoComentario> optionalPublicacaoComentario = publicacaoComentarioRepository.findById(idPublicacaoComentario);

		PublicacaoComentario publicacaoComentario = optionalPublicacaoComentario.orElseThrow(() -> new BusinessException("Comentário não encontrado"));

		return publicacaoComentario;
	}
	
	@Transactional(readOnly = true)
	public Publicacao getPublicacao(Long id) {
		
		Optional<Publicacao> optionalPublicacao = publicacaoRepository.findById(id);

		Publicacao publicacao = optionalPublicacao.orElseThrow(() -> new BusinessException("Publicação não encontrado"));

		return publicacao;
	}
	
	@Transactional(readOnly = true)
	public HashMap<Long, ArrayList<PublicacaoComentario>> getMapaComentariosPorListaPublicacao(List<Long> listaIdPublicacao) {
		
		validarPeloMenosUmaPublicacaoInformada(listaIdPublicacao);

		List<PublicacaoComentario> listaPublicacaoComentario = publicacaoComentarioRepository.buscarComentariosPorListaPublicacao(listaIdPublicacao);
		
		HashMap<Long, ArrayList<PublicacaoComentario>> mapaComentarios = new HashMap<Long, ArrayList<PublicacaoComentario>>();
		
		for (PublicacaoComentario publicacaoComentario : listaPublicacaoComentario) {
			
			if (!mapaComentarios.containsKey(publicacaoComentario.getPublicacao().getId())) {
				mapaComentarios.put(publicacaoComentario.getPublicacao().getId(), new ArrayList<PublicacaoComentario>());
			}
			
			mapaComentarios.get(publicacaoComentario.getPublicacao().getId()).add(publicacaoComentario);
		}
		
		return mapaComentarios;
	}
	
	private void validarPeloMenosUmaPublicacaoInformada(List<Long> listaIdPublicacao) {
		
		if (listaIdPublicacao == null || listaIdPublicacao.isEmpty()) {
			throw new BusinessException("Nenhuma publicação informada!");
		}
		
	}

	public void comentarPublicacoes() {
		
		List<Usuario> listaUsuario = usuarioService.findAll();
		
		List<Publicacao> listaPublicacao = publicacaoRepository.findAll();
		
		List<String> listaComentarios = getListaComentarios();
		
		for (Publicacao publicacao : listaPublicacao) {
			
			int numeroComentarios = new Random().nextInt(0, 12);
			
			List<Usuario> listaEmbaralhada = shuffleList(listaUsuario);
			
			for (int i = 0; i < numeroComentarios; i++) {
				
				int comentario = new Random().nextInt(0, listaComentarios.size() -1);
				
				PublicacaoComentario publicacaoComentario = new PublicacaoComentario();
				publicacaoComentario.setPublicacao(publicacao);
				publicacaoComentario.setData(LocalDateTime.now());
				publicacaoComentario.setDescricao(listaComentarios.get(comentario));
				publicacaoComentario.setUsuario(listaEmbaralhada.get(i));
				
				publicacaoComentario = publicacaoComentarioRepository.save(publicacaoComentario);
				
			}
			
		}
	}
	
	private List<String> getListaComentarios() {
	      
			List<String> comentarios = new ArrayList<>();

	        // Adicionar os comentários à lista
	        comentarios.add("Que festa incrível! 🔥 Mal posso esperar pela próxima!");
	        comentarios.add("Cada detalhe estava perfeito! 👏 Parabéns pela organização!");
	        comentarios.add("A energia dessa festa estava contagiante! 💃");
	        comentarios.add("Momentos que vou lembrar para sempre. 🌟");
	        comentarios.add("Amei a escolha da música! 🎶 Qual o nome daquela última música mesmo?");
	        comentarios.add("Que vibe maravilhosa! ✨ Quando é a próxima edição?");
	        comentarios.add("Comida deliciosa, ambiente incrível! 🍽️🎉");
	        comentarios.add("Essa festa foi o ponto alto da semana! 🙌");
	        comentarios.add("Rindo até agora das histórias da festa! 😂");
	        comentarios.add("Que decoração sensacional! 😍 Deu um toque especial.");
	        comentarios.add("Foi uma noite inesquecível! 🌙💫");
	        comentarios.add("Quero a playlist dessa festa para a vida toda! 🎵");
	        comentarios.add("Que encontro maravilhoso! 👫 Mal posso esperar pelo próximo.");
	        comentarios.add("Estava tudo perfeito! Desde a comida até a música. 👌");
	        comentarios.add("As melhores memórias foram feitas aqui! 💖");
	        comentarios.add("Que energia boa! 🌈✨ Quando é a próxima edição?");
	        comentarios.add("Essa festa superou todas as expectativas! 🚀");
	        comentarios.add("Amei a diversidade de pessoas e estilos! 🌍👯‍♂️");
	        comentarios.add("As fotos estão incríveis! 📷 Quem é o fotógrafo?");
	        comentarios.add("Ressaca de felicidade pós-festa! 😄");
	        comentarios.add("Essa festa foi um espetáculo! 🎉 Parabéns aos organizadores.");
	        comentarios.add("Acho que encontrei a festa do ano! 🏆");
	        comentarios.add("Momentos únicos e espontâneos! 🤩");
	        comentarios.add("A combinação de música e ambiente foi perfeita! 🎶🌆");
	        comentarios.add("Fiquei encantado(a) com a decoração! 🌺✨");
	        comentarios.add("Que noite sensacional! 🌙🕺");
	        comentarios.add("Essa festa foi um verdadeiro refúgio de alegria! 🎊");
	        comentarios.add("Adorei cada segundo! 🕰️ Quando será a próxima edição?");
	        comentarios.add("Foi mais do que uma festa, foi uma experiência! 🌟 #Inesquecível");

		return comentarios;
	}

	private List<Usuario> shuffleList(List<Usuario> listaUsuario) {
		
		List<Usuario> list = new ArrayList<>();
		list.addAll(listaUsuario);
		
		Collections.shuffle(listaUsuario);

		return listaUsuario;
	}
}