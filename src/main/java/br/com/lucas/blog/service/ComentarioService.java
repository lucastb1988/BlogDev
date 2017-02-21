package br.com.lucas.blog.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lucas.blog.entity.Comentario;
import br.com.lucas.blog.repository.ComentarioRepository;

/**
 * @author Lucas.
 * 
 *         Classe que representa as regras de neg�cio de Coment�rio com seus respectivos m�todos.
 * 
 */

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class ComentarioService {

	@Autowired
	private ComentarioRepository comentarioRepository;
	
	/**
	 * M�todo para salvar um objeto comentario.
	 * 
	 * * @param comentario o comentario a ser salvo no banco de dados.
	 *
	 */
	@Transactional(readOnly = false)
	public void save(Comentario comentario) {
		
		comentario.setDataComentario(LocalDateTime.now()); //setar a data do coment�rio como na data e hora do sistema operacional
		
		comentarioRepository.save(comentario); //salva o comentario no bd
	}
}