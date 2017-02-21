package br.com.lucas.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lucas.blog.entity.Autor;
import br.com.lucas.blog.repository.AutorRepository;

/**
 * @author Lucas.
 * 
 *         Classe que representa as regras de negócio de Autor com seus respectivos métodos.
 * 
 */

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class AutorService {

	@Autowired
	AutorRepository autorRepository;

	/**
	 * Método para encontrar por paginação a lista da entidade Autor.
	 * 
	 * * @param page a page a ser buscada .
	 * * @param size o size a ser representado na tela por página.
	 * 
	 * @return uma ou (+) página(s) de autor(es).
	 */
	public Page<Autor> findByPagination(int page, int size) {

		Pageable pageable = new PageRequest(page, size);

		return autorRepository.findAllByOrderByNomeAsc(pageable);		
	}

	/**
	 * Método para deletar um objeto autor.
	 * 
	 * * @param id o id a ser buscado para realizar a exclusão no banco de dados.
	 *
	 */
	@Transactional(readOnly = false)
	public void delete(Long id) {

		autorRepository.delete(id);
	}

	/**
	 * Método para salvar um objeto autor.
	 * 
	 * * @param autor o autor a ser salvo no banco de dados.
	 *
	 */
	@Transactional(readOnly = false)
	public void save(Autor autor) {

		if(autor.getId() == null) { //se não possuir id quer dizer que trata-se de uma inserção nova

			autorRepository.save(autor); //realiza a inserção

		} else {

			autorRepository.updateNomeAndBiografia(autor.getNome(), autor.getBiografia(), autor.getId()); 
			//já possui id, faz o update dos campos que deseja modificar através da query realizada no repository 
		}		
	}

	/**
	 * Método para encontrar toda lista de autor(es).
	 * 
	 * @return uma lista de autores.
	 */
	public List<Autor> findAll() {

		return autorRepository.findAll();
	}

	/**
	 * Método para encontrar um autor através de seu nome.
	 * 
	 * * @param nome o nome do autor a ser encontrado.
	 * 
	 * @return um autor.
	 */
	public Autor findByNome(String nome) {

		return autorRepository.findByNome(nome);
	}

	/**
	 * Método para encontrar um autor por seu id.
	 * 
	 * * @param id o id do autor a ser encontrado.
	 * 
	 * @return um autor.
	 */
	public Autor findById(Long id) {

		return autorRepository.findOne(id);
	}

	/**
	 * Método para encontrar um autor através de um usuário por seu id.
	 * 
	 * * @param id o id do autor a ser encontrado.
	 * 
	 * @return um autor.
	 */
	public Autor findByUsuario(Long id) {

		return autorRepository.findByUsuario(id);
	}	
}