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
 *         Classe que representa as regras de negócio de Categoria com seus respectivos métodos.
 * 
 */

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	/**
	 * Método para encontrar por paginação a lista da entidade Categoria.
	 * 
	 * * @param page a page a ser buscada .
	 * * @param size o size a ser representado na tela por página.
	 * 
	 * @return uma ou (+) página(s) de categoria(s).
	 */
	public Page<Categoria> findByPagination(int page, int size) { //page é a página que estamos buscando, size é a quantidade de dados definido por página
		
		Pageable pageable = new PageRequest(page, size); //atribuindo pageable o request das páginas
		
		return categoriaRepository.findAllByOrderByDescricaoAsc(pageable); //retorna paginação com todas as categorias de forma asc
	}

	/**
	 * Método para encontrar toda lista de categoria(s).
	 * 
	 * @return uma lista de categorias.
	 */
	public List<Categoria> findAll() {
		
		Sort sort = new Sort(new Order(Direction.ASC, "descricao"));
		
		return categoriaRepository.findAll(sort);
	}

	/**
	 * Método para encontrar um autor através de sua descrição.
	 * 
	 * * @param nome o nome do autor a ser encontrado.
	 * 
	 * @return uma categoria.
	 */
	public Categoria findByDescricao(String descricao) {

		return categoriaRepository.findByDescricao(descricao);
	}

	/**
	 * Método para encontrar uma categoria por seu id.
	 * 
	 * * @param id o id da categoria a ser encontrada.
	 * 
	 * @return uma categoria.
	 */
	public Categoria findById(Long id) {

		return categoriaRepository.findOne(id);
	}

	/**
	 * Método para deletar um objeto categoria.
	 * 
	 * * @param id o id a ser buscado para realizar a exclusão no banco de dados.
	 *
	 */
	@Transactional(readOnly = false)
	public void delete(Long id) {

		categoriaRepository.delete(id);
	}

	/**
	 * Método para salvar ou editar um objeto categoria.
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