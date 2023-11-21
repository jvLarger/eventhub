package com.jlarger.eventhub.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlarger.eventhub.dto.PushNotificationRequest;
import com.jlarger.eventhub.dto.UsuarioComentarioDTO;
import com.jlarger.eventhub.dto.UsuarioDTO;
import com.jlarger.eventhub.entities.Notificacao;
import com.jlarger.eventhub.entities.Usuario;
import com.jlarger.eventhub.entities.UsuarioComentario;
import com.jlarger.eventhub.entities.type.TipoNotificacao;
import com.jlarger.eventhub.repositories.UsuarioComentarioRepository;
import com.jlarger.eventhub.services.exceptions.BusinessException;
import com.jlarger.eventhub.utils.ServiceLocator;

@Service
public class UsuarioComentarioService {

	@Autowired
	private UsuarioComentarioRepository usuarioComentarioRepository;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private NotificacaoService notificacaoService;

	@Autowired
	private PushNotificationService pushNotificationService;

	@Autowired
	private AmizadeService amizadeService;

	@Transactional
	public void enviarSolicitacaoComentario(Long id, UsuarioComentarioDTO dto) {

		validarComentarioInformado(dto);

		Usuario usuarioOrigem = usuarioService.getUsuarioLogado();

		Usuario usuarioDestino = usuarioService.getUsuario(id);

		Notificacao notificacao = popularNotificacaoSolicitacaoComentario(usuarioOrigem, usuarioDestino, dto);

		notificacaoService.enviarNotificacao(notificacao);

		if (usuarioDestino.getIdentificadorNotificacao() != null) {
			pushNotificationService.sendPushNotificationToToken(new PushNotificationRequest("Event Hub",
					usuarioOrigem.getNomeCompleto() + " deseja comentar em seu perfil.", "",
					usuarioDestino.getIdentificadorNotificacao()));
		}
	}

	private void validarComentarioInformado(UsuarioComentarioDTO dto) {

		if (dto == null || dto.getComentario() == null || dto.getComentario().trim().isEmpty()) {
			throw new BusinessException("Comentário não informado!");
		}

		if (dto.getComentario().length() <= 5) {
			throw new BusinessException("O comentário deve possuir pelo menos 5 caracteres!");
		}

	}

	private Notificacao popularNotificacaoSolicitacaoComentario(Usuario usuarioOrigem, Usuario usuarioDestino,
			UsuarioComentarioDTO dto) {

		Notificacao notificacao = new Notificacao();
		notificacao.setDataNotificacao(LocalDateTime.now());
		notificacao.setDescricao(dto.getComentario());
		notificacao.setUsuarioOrigem(usuarioOrigem);
		notificacao.setUsuarioDestino(usuarioDestino);
		notificacao.setTipo(TipoNotificacao.SOLICITACAO_COMENTARIO_PUBLICO);

		return notificacao;
	}

	@Transactional
	public void aceitarSolicitacaoComentario(Long idNotificacao) {

		Notificacao notificacao = notificacaoService.getNotificacao(idNotificacao);

		if (notificacao.getUsuarioDestino().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0) {
			throw new BusinessException("Apenas o dono do convite pode aceitar.");
		}

		notificacaoService.marcarComoLida(idNotificacao);

		UsuarioComentario usuarioComentario = new UsuarioComentario();
		usuarioComentario.setUsuario(notificacao.getUsuarioDestino());
		usuarioComentario.setUsuarioOrigem(notificacao.getUsuarioOrigem());
		usuarioComentario.setComentario(notificacao.getDescricao());
		usuarioComentario.setDataComentario(notificacao.getDataNotificacao());

		usuarioComentarioRepository.save(usuarioComentario);
	}

	@Transactional
	public void removerComentario(Long id) {

		UsuarioComentario usuarioComentario = getUsuarioComentario(id);

		if (usuarioComentario.getUsuario().getId().compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0
				&& usuarioComentario.getUsuarioOrigem().getId()
						.compareTo(ServiceLocator.getUsuarioLogado().getId()) != 0) {
			throw new BusinessException("Apenas o dono do comentário ou do perfil podem remover.");
		}

		usuarioComentarioRepository.delete(usuarioComentario);
	}

	public UsuarioComentario getUsuarioComentario(Long id) {

		Optional<UsuarioComentario> optionalUsuarioComentario = usuarioComentarioRepository.findById(id);

		UsuarioComentario usuarioComentario = optionalUsuarioComentario
				.orElseThrow(() -> new BusinessException("Comentário não encontrado"));

		return usuarioComentario;
	}

	@Transactional
	public void comentarPerfil() {

		List<Usuario> listaUsuario = usuarioService.findAll();

		List<String> listaComentarios = getListaComentarios();

		for (Usuario usuario : listaUsuario) {

			List<UsuarioDTO> listaUsuarioDTO = amizadeService.buscarAmigos(usuario.getId());

			int numeroComentarios = new Random().nextInt(0, listaUsuarioDTO.size() > 10 ? 10 : listaUsuarioDTO.size());

			List<UsuarioDTO> listaEmbaralhada = shuffleList(listaUsuarioDTO);

			for (int i = 0; i < numeroComentarios; i++) {

				int comentario = new Random().nextInt(0, listaComentarios.size() - 1);
				
				Usuario usuarioOrigem = usuarioService.getUsuario(listaEmbaralhada.get(i).getId());
				
				UsuarioComentario usuarioComentario = new UsuarioComentario();
				usuarioComentario.setUsuario(usuario);
				usuarioComentario.setUsuarioOrigem(usuarioOrigem);
				usuarioComentario.setComentario(listaComentarios.get(comentario));
				usuarioComentario.setDataComentario(LocalDateTime.now());

				usuarioComentarioRepository.save(usuarioComentario);
			}

		}

	}

	private List<String> getListaComentarios() {

		List<String> elogios = new ArrayList<>();

		// Adicionar os elogios à lista
		elogios.add("Você é uma fonte constante de inspiração! 💫");
		elogios.add("Sua bondade ilumina as redes sociais! 🌟");
		elogios.add("Ninguém faz como você, sempre com um toque especial! 👌");
		elogios.add("Seu coração é tão grande quanto seu sorriso! 😊");
		elogios.add("Admiro muito a pessoa incrível que você é! 👏");
		elogios.add("Sempre trazendo luz e positividade para o meu dia! ☀️");
		elogios.add("Você é um exemplo de determinação e força! 💪");
		elogios.add("Sua autenticidade é o que mais me encanta! 🌈");
		elogios.add("Além de talentoso(a), você é genuinamente bom(a) de coração! ❤️");
		elogios.add("Seu jeito único de ser deixa o mundo mais bonito! 🌍");
		elogios.add("Você é uma verdadeira inspiração para todos nós! 🌟");
		elogios.add("O mundo precisa de mais pessoas como você! 🌎");
		elogios.add("Sua generosidade não tem limites, e eu admiro isso demais! 🙌");
		elogios.add("Seu positivismo é contagiante, obrigado(a) por espalhar alegria! 😄");
		elogios.add("Nunca deixo de me impressionar com sua gentileza e compaixão! 🌷");
		elogios.add("Você é uma pessoa rara, e o mundo é melhor com você nele! 💖");
		elogios.add("Sua capacidade de fazer a diferença é admirável! 👊");
		elogios.add("Seu brilho próprio ilumina a todos ao redor! ✨");
		elogios.add("Seu comprometimento com o bem é inspirador! 🌟");
		elogios.add("Você faz deste espaço digital um lugar melhor a cada post! 👍");

		return elogios;
	}

	private List<UsuarioDTO> shuffleList(List<UsuarioDTO> listaUsuario) {

		List<UsuarioDTO> list = new ArrayList<>();
		list.addAll(listaUsuario);

		Collections.shuffle(listaUsuario);

		return listaUsuario;
	}

}