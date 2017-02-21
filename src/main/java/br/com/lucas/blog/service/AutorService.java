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
 *         Classe que representa as regras de neg�cio de Autor com seus respectivos m�todos.
 * 
 */

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class AutorService {

	@Autowired
	AutorRepository autorRepository;

	/**
	 * M�todo para encontrar por pagina��o a lista da entidade Autor.
	 * 
	 * * @param page a page a ser buscada .
	 * * @param size o size a ser representado na tela por p�gina.
	 * 
	 * @return uma ou (+) p�gina(s) de autor(es).
	 */
	public Page<Autor> findByPagination(int page, int size) {

		Pageable pageable = new PageRequest(page, size);

		return autorRepository.findAllByOrderByNomeAsc(pageable);		
	}

	/**
	 * M�todo para deletar um objeto autor.
	 * 
	 * * @param id o id a ser buscado para realizar a exclus�o no banco de dados.
	 *
	 */
	@Transactional(readOnly = false)
	public void delete(Long id) {

		autorRepository.delete(id);
	}

	/**
	 * M�todo para salvar um objeto autor.
	 * 
	 * * @param autor o autor a ser salvo no banco de dados.
	 *
	 */
	@Transactional(readOnly = false)
	public void save(Autor autor) {

		if(autor.getId() == null) { //se n�o possuir id quer dizer que trata-se de uma inser��o nova

			autorRepository.save(autor); //realiza a inser��o

		} else {

			autorRepository.updateNomeAndBiografia(autor.getNome(), autor.getBiografia(), autor.getId()); 
			//j� possui id, faz o update dos campos que deseja modificar atrav�s da query realizada no repository 
		}		
	}

	/**
	 * M�todo para encontrar toda lista de autor(es).
	 * 
	 * @return uma lista de autores.
	 */
	public List<Autor> findAll() {

		return autorRepository.findAll();
	}

	/**
	 * M�todo para encontrar um autor atrav�s de seu nome.
	 * 
	 * * @param nome o nome do autor a ser encontrado.
	 * 
	 * @return um autor.
	 */
	public Autor findByNome(String nome) {

		return autorRepository.findByNome(nome);
	}

	/**
	 * M�todo para encontrar um autor por seu id.
	 * 
	 * * @param id o id do autor a ser encontrado.
	 * 
	 * @return um autor.
	 */
	public Autor findById(Long id) {

		return autorRepository.findOne(id);
	}

	/**
	 * M�todo para encontrar um autor atrav�s de um usu�rio por seu id.
	 * 
	 * * @param id o id do autor a ser encontrado.
	 * 
	 * @return um autor.
	 */
	public Autor findByUsuario(Long id) {

		return autorRepository.findByUsuario(id);
	}	
}