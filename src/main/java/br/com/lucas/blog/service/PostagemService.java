package br.com.lucas.blog.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lucas.blog.entity.Postagem;
import br.com.lucas.blog.repository.PostagemRepository;
import br.com.lucas.blog.util.MyReplaceString;

/**
 * @author Lucas.
 * 
 *         Classe que representa as regras de neg�cio de Postagem com seus respectivos m�todos.
 * 
 */

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class PostagemService {

	@Autowired
	private PostagemRepository postagemRepository;	
	
	/**
	 * M�todo para encontrar por pagina��o/titulo/autor a lista da entidade Postagem.
	 * 
	 * * @param page a page a ser buscada .
	 * * @param size o size a ser representado na tela por p�gina.
	 * * @param titulo o titulo a ser buscado.
	 * * @param id o id do autor a ser buscado.
	 * 
	 * @return uma ou (+) p�gina(s) de postagem(ns).
	 */
	public Page<Postagem> findByTituloAndAutor(int page, int size, String titulo, Long id) {
		
		Pageable pageable = new PageRequest(page, size);
		
		return postagemRepository.findAllByAutorIdAndTituloContainingIgnoreCaseOrderByDataPostagemDesc(pageable, id, titulo);
	}

	/**
	 * M�todo para encontrar(search) por pagina��o/titulo a lista da entidade Postagem utilizando ajax.
	 * 
	 * * @param page a page a ser buscada .
	 * * @param size o size a ser representado na tela por p�gina.
	 * * @param titulo o titulo a ser buscado.
	 * 
	 * @return uma ou (+) p�gina(s) de postagem(ns).
	 */
	public Page<Postagem> findByTitulo(int page, int size, String titulo) {

		Pageable pageable = new PageRequest(page, size);

		return postagemRepository.findAllByTituloContainingIgnoreCaseOrderByDataPostagemDesc(pageable, titulo);
	}

	/**
	 * M�todo para encontrar(search) por pagina��o/titulo a lista da entidade Postagem utilizando ajax.
	 * 
	 * * @param page a page a ser buscada .
	 * * @param size o size a ser representado na tela por p�gina.
	 * * @param texto o texto a ser buscado.
	 * 
	 * @return uma ou (+) p�gina(s) de postagem(ns).
	 */
	public Page<Postagem> findByTexto(int page, int size, String texto) {

		return postagemRepository.findByTextoContainingIgnoreCaseOrderByDataPostagemDesc(texto, new PageRequest(page, size)); 
		//retorna texto contendo qualquer palavra ignorando letra maiscula/minuscula e ordenando por data de Postagem
		//utilizando keywords
	}

	/**
	 * M�todo para encontrar(search) por pagina��o o Autor da Postagem na p�gina Home.
	 * 
	 * * @param page a page a ser buscada .
	 * * @param size o size a ser representado na tela por p�gina.
	 * * @param id o id a ser buscado.
	 * 
	 * @return uma ou (+) p�gina(s) de postagem(ns) de acordo com Autor informado.
	 */
	public Page<Postagem> findByPaginationByAutor(int page, int size, Long id) {

		Pageable pageable = new PageRequest(page, size);

		return postagemRepository.findAllByAutorIdOrderByDataPostagemDesc(pageable, id);
	}

	/**
	 * M�todo para encontrar(search) por pagina��o a Categoria da Postagem na p�gina Home.
	 * 
	 * * @param page a page a ser buscada .
	 * * @param size o size a ser representado na tela por p�gina.
	 * * @param permalink o permalink a ser buscado.
	 * 
	 * @return uma ou (+) p�gina(s) de postagem(ns) de acordo com Categoria informada.
	 */
	public Page<Postagem> findByPaginationByCategoria(int page, int size, String permalink) {

		Pageable pageable = new PageRequest(page, size);

		return postagemRepository.findAllByCategoriasPermalinkOrderByDataPostagemDesc(pageable, permalink);
	}	

	/**
	 * M�todo para encontrar por pagina��o a lista da entidade Postagem.
	 * 
	 * * @param page a page a ser buscada .
	 * * @param size o size a ser representado na tela por p�gina.
	 * 
	 * @return uma ou (+) p�gina(s) de postagem(ns).
	 */
	public Page<Postagem> findByPagination(int page, int size) {

		Pageable pageable = new PageRequest(page, size);

		return postagemRepository.findAllByOrderByDataPostagemDesc(pageable);		
	}

	/**
	 * M�todo para encontrar a lista da entidade Postagem.
	 *  	 
	 * @return uma ou (+) p�gina(s) de postagem(ns).
	 */
	public List<Postagem> findAll() {

		return postagemRepository.findAll();
	}

	/**
	 * M�todo para encontrar uma postagem atrav�s de seu npermalinkome.
	 * 
	 * * @param permalink o permalink do autor a ser encontrado.
	 * 
	 * @return uma postagem.
	 */
	public Postagem findByPermalink(String permalink) {

		return postagemRepository.findByPermalink(permalink);
	}

	/**
	 * M�todo para encontrar uma postagem atrav�s de seu id.
	 * 
	 * * @param id o id da postagem a ser encontrado.
	 * 
	 * @return uma postagem.
	 */
	public Postagem findById(Long id) {

		return postagemRepository.findOne(id);
	}

	/**
	 * M�todo para encontrar uma lista de postagens atrav�s de sua categoria.
	 * 
	 * * @param link o link da categoria da postagem a ser encontrado.
	 * 
	 * @return uma lista de postagens.
	 */
	public List<Postagem> findByCategoria(String link) {

		return postagemRepository.findByCategoriasPermalink(link); //encontrar pela keyword(Categorias Permalink)
	}

	/**
	 * M�todo para encontrar uma lista de postagens atrav�s de seu autor.
	 * 
	 * * @param nome o nome do autor da postagem a ser encontrado.
	 * 
	 * @return uma lista de postagens.
	 */
	public List<Postagem> findByAutor(String nome) {

		return postagemRepository.findByAutorNome(nome);
	}

	/**
	 * M�todo para deletar um objeto postagem.
	 * 
	 * * @param id o id a ser buscado para realizar a exclus�o no banco de dados.
	 *
	 */
	@Transactional(readOnly = false)
	public void delete(Long id) {

		postagemRepository.delete(id);
	}	

	/**
	 * M�todo para salvar ou editar um objeto postagem.
	 * 
	 * * @param postagem a postagem a ser salva ou editada no banco de dados.
	 *
	 */
	@Transactional(readOnly = false)
	public void saveOrUpdate(Postagem postagem) {

		if(postagem.getId() == null) {
			save(postagem);

		} else {
			update(postagem);
		}
	}	

	/**
	 * M�todo para editar um objeto postagem.
	 * 
	 * * @param postagem a postagem a ser editada no banco de dados.
	 *
	 */
	private void update(Postagem postagem) {

		Postagem persistente = postagemRepository.findOne(postagem.getId());

		if(!persistente.getTitulo().equals(postagem.getTitulo())) { // ! se seu objeto persistente (titulo) for diferente da postagem que possui no banco
			persistente.setTitulo(postagem.getTitulo()); //seta o novo titulo
		}

		if(!persistente.getTexto().equals(postagem.getTexto())) { // ! se seu objeto persistente (texto) for diferente da postagem que possui no banco
			persistente.setTexto(postagem.getTexto()); //seta o novo texto
		}

		if(persistente.getCategorias() != postagem.getCategorias()) { //se seu objeto persistente (lista de categorias) for diferente da lista de categorias que possui no banco
			persistente.setCategorias(postagem.getCategorias()); //seta a nova lista de  categorias
		}

		postagemRepository.save(persistente);
	}

	/**
	 * M�todo para salvar um objeto postagem.
	 * 
	 * * @param postagem a postagem a ser salva no banco de dados.
	 *
	 */
	private void save(Postagem postagem) {

		String permalink = MyReplaceString.formatarPermalink(postagem.getTitulo());	//passa o titulo para o m�todo formatarLink e realiza a formata��o conforme informado na pr�pria classe	
		postagem.setPermalink(permalink); //atribui o permalink dentro da postagem		
		postagem.setDataPostagem(LocalDateTime.now()); //setar postagem com a data e hora do sistema operacional

		postagemRepository.save(postagem); //salva postagem no repositorio/bd
	}
}