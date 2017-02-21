package br.com.lucas.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lucas.blog.entity.Categoria;
import br.com.lucas.blog.repository.CategoriaRepository;
import br.com.lucas.blog.util.MyReplaceString;

/**
 * @author Lucas.
 * 
 *         Classe que representa as regras de neg�cio de Categoria com seus respectivos m�todos.
 * 
 */

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	/**
	 * M�todo para encontrar por pagina��o a lista da entidade Categoria.
	 * 
	 * * @param page a page a ser buscada .
	 * * @param size o size a ser representado na tela por p�gina.
	 * 
	 * @return uma ou (+) p�gina(s) de categoria(s).
	 */
	public Page<Categoria> findByPagination(int page, int size) { //page � a p�gina que estamos buscando, size � a quantidade de dados definido por p�gina
		
		Pageable pageable = new PageRequest(page, size); //atribuindo pageable o request das p�ginas
		
		return categoriaRepository.findAllByOrderByDescricaoAsc(pageable); //retorna pagina��o com todas as categorias de forma asc
	}

	/**
	 * M�todo para encontrar toda lista de categoria(s).
	 * 
	 * @return uma lista de categorias.
	 */
	public List<Categoria> findAll() {
		
		Sort sort = new Sort(new Order(Direction.ASC, "descricao"));
		
		return categoriaRepository.findAll(sort);
	}

	/**
	 * M�todo para encontrar um autor atrav�s de sua descri��o.
	 * 
	 * * @param nome o nome do autor a ser encontrado.
	 * 
	 * @return uma categoria.
	 */
	public Categoria findByDescricao(String descricao) {

		return categoriaRepository.findByDescricao(descricao);
	}

	/**
	 * M�todo para encontrar uma categoria por seu id.
	 * 
	 * * @param id o id da categoria a ser encontrada.
	 * 
	 * @return uma categoria.
	 */
	public Categoria findById(Long id) {

		return categoriaRepository.findOne(id);
	}

	/**
	 * M�todo para deletar um objeto categoria.
	 * 
	 * * @param id o id a ser buscado para realizar a exclus�o no banco de dados.
	 *
	 */
	@Transactional(readOnly = false)
	public void delete(Long id) {

		categoriaRepository.delete(id);
	}

	/**
	 * M�todo para salvar ou editar um objeto categoria.
	 * 
	 * * @param categoria a categoria a ser salva ou editada no banco de dados.
	 *
	 */
	@Transactional(readOnly = false)
	public void saveOrUpdate(Categoria categoria) {

		String permalink = MyReplaceString.formatarPermalink(categoria.getDescricao());
		
		if(categoria.getId() != null) {
			Categoria cPersistente = categoriaRepository.findOne(categoria.getId());
			cPersistente.setPermalink(permalink);
			cPersistente.setDescricao(categoria.getDescricao());
			
			categoriaRepository.save(cPersistente);
			
		} else {
			categoria.setPermalink(permalink);

			categoriaRepository.save(categoria);
		}		
	}
}