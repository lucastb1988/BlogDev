package br.com.lucas.blog.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.lucas.blog.entity.Postagem;
import br.com.lucas.blog.entity.UsuarioLogado;
import br.com.lucas.blog.service.AutorService;
import br.com.lucas.blog.service.CategoriaService;
import br.com.lucas.blog.service.PostagemService;
import br.com.lucas.blog.web.editor.CategoriaEditorSupport;
import br.com.lucas.blog.web.validator.PostagemAjaxValidator;

/**
 * @author Lucas.
 * 
 *         Classe que representa o controller de Postagem com seus respectivos métodos.
 * 
 */

@Controller
@RequestMapping("postagem")
public class PostagemController {

	@Autowired
	private PostagemService postagemService;

	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private AutorService autorService;

	//Ao inicializar será consumido a conversão de objeto Categoria para list Categoria
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(
				List.class, 
				new CategoriaEditorSupport(List.class, categoriaService)
				);
	}
	
	//método para search por titulo(palavra-chave) na postagem para encontrar por titulo utilizando ajax
	@RequestMapping(value = "/ajax/autor/{id}/titulo/{titulo}/page/{page}", method = RequestMethod.GET) 
	public ModelAndView searchByAjaxByAutor(@PathVariable("id") Long id,
			@PathVariable("titulo") String titulo,
			@PathVariable("page") Integer pagina) {

		ModelAndView view = new ModelAndView("postagem/table-rows");

		Page<Postagem> page = postagemService.findByTituloAndAutor(pagina - 1, 20, titulo, id);

		view.addObject("page", page);

		return view;
	}	

	@RequestMapping(value = "/ajax/autor/{id}/page/{page}", method = RequestMethod.GET)
	public ModelAndView pagePostagens(@PathVariable("id") Long id,
			@PathVariable("page") Integer pagina) {

		ModelAndView view = new ModelAndView("postagem/table-rows"); 

		Page<Postagem> page = postagemService.findByPaginationByAutor(pagina - 1, 20, id);

		view.addObject("page", page);

		return view;
	}

	/**
	 * Método para listar todas as Postagens por paginação por Autor.
	 * 
	 * * @param id o @PathVariable id a ser buscada por page.
	 * * @param model o modelmap a add atributos e serem referenciados na página jsp.
	 * 
	 * @return view(acesso a página list.jsp do diretório postagem + @param model com seus atributos a serem referenciado na página jsp).
	 */
	@RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	public ModelAndView listPostagensByAutor(@PathVariable("id") Long id, ModelMap model) {

		Long autorId = autorService.findByUsuario(id).getId();		

		Page<Postagem> page = postagemService.findByPaginationByAutor(0, 5, autorId); 
		//cria um objeto onde iremos armazenar o retorno da consulta / 0 é a variavel informada na url porém neste caso sempre será aberto a primeira página do formulário(0 igual a Array) / 5 é o tamanho de itens por página
		//objeto Page/Slice contém várias opções uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(contém a sua lista)
		//devido a todas essas opções absorvidas do objeto Page não é necessário incluir o List de categorias pois o Page já está fazendo este trabalho

		model.addAttribute("page", page); //não pode ser encontrado diretamente todas as categorias, necessita utilizar o Page acima pois senão ele não encontra as paginações ao abrir o formulário
		model.addAttribute("autorId", autorId);	

		return new ModelAndView("postagem/list", model); // diretório postagem / página list.jsp 
		// página que será acessada/visualizada / "model" contém a lista com as postagens
	}

	//salvar postagem utilizando ajax
	@RequestMapping(value = "/ajax/save", method = RequestMethod.POST)
	public @ResponseBody PostagemAjaxValidator saveAjax(@Validated Postagem postagem, 
			BindingResult result, @AuthenticationPrincipal UsuarioLogado logado) {

		PostagemAjaxValidator validator = new PostagemAjaxValidator();

		if ( result.hasErrors() ) {

			validator.setStatus("FAIL");

			validator.validar(result);

			return validator;
		}
		
		postagem.setAutor(autorService.findByUsuario(logado.getId()));

		postagemService.saveOrUpdate(postagem);

		validator.setPostagem(postagem);

		return validator;
	}

	@RequestMapping(value = "/ajax/add", method = RequestMethod.GET)
	public ModelAndView cadastroAjax() {
		
		ModelAndView view = new ModelAndView("postagem/cadastro-ajax");
		view.addObject("categorias", categoriaService.findAll());
		return view;
	}

	//método para search por titulo(palavra-chave) na postagem para encontrar por titulo utilizando ajax
	@RequestMapping(value = "/ajax/titulo/{titulo}/page/{page}", method = RequestMethod.GET) 
	public ModelAndView searchByAjax(@PathVariable("titulo") String titulo,
			@PathVariable("page") Integer pagina) {

		ModelAndView view = new ModelAndView("postagem/table-rows");

		Page<Postagem> page = postagemService.findByTitulo(pagina - 1, 20, titulo);

		view.addObject("page", page);

		return view;
	}	

	/**
	 * Método para paginação das postagens ordenadas de forma asc
	 * 
	 * * @param pagina a @PathVariable pagina a ser buscada por page.
	 * 
	 * @return view(acesso a página table-rows.jsp do diretório postagem).
	 */
	//método para paginação dos usuarios ordenadas de forma asc
	@RequestMapping(value = "/ajax/page/{page}", method = RequestMethod.GET) //{page} é a página que iremos buscar
	public ModelAndView pagePostagens(@PathVariable("page") Integer pagina) {

		ModelAndView view = new ModelAndView("postagem/table-rows"); //irá acessar a página de table-rows.jsp, a lista de usuarios se encontra

		Page<Postagem> page = postagemService.findByPagination(pagina - 1, 20); 
		//cria um objeto onde iremos armazenar o retorno da consulta 
		//pagina é a variavel informada na url, necessita trabalhar com -1 pois a página começa com 0 porém o usuario visualiza como página / 5 é o tamanho de itens por página
		//objeto Page/Slice contém várias opções uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(contém o conteudo da lista)
		//devido a todas essas opções absorvidas do objeto Page não é necessário incluir o List de categorias pois o Page já está fazendo este trabalho


		view.addObject("page", page); // adiciona na view este objeto page criado acima/ "page" é o nome do objeto que vamos enviar para página / page contém o resultado da nossa consulta(valor) 
		//view.addObject("urlPagination", "/postagem/page");

		return view;
	}

	/**
	 * Método para pré-editar um objeto Postagem.
	 * 
	 * * @param id a id a ser buscado para realizar update.
	 * * @param model o modelmap a add atributos e serem referenciados na página jsp.
	 * 
	 * @return view(acesso a página cadastro.jsp do diretório postagem + @param model com seus atributos a serem referenciado na página jsp).
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public ModelAndView preUpdate(@PathVariable("id") Long id, ModelMap model) {

		Postagem postagem = postagemService.findById(id); //procura pelo id e atribui junto a postagem o id

		model.addAttribute("postagem", postagem); //página que será acessada/visualizada / retorna para página o valor que será localizado a partir da consulta que faremos por id
		model.addAttribute("categorias", categoriaService.findAll()); //relacionar a listagem das categorias ao acessar o cadastro da postagem (será selecionada uma categoria após inserção/update da postagem)

		return new ModelAndView("postagem/cadastro", model); // diretório postagem / página list.jsp / 
		// página que será acessada/visualizada / "model" receberá o objeto que está sendo retornado para ser alterado 
	}

	/**
	 * Método para excluir um objeto Postagem.
	 * 
	 * * @param id o @PathVariable id a ser buscado para realizar exclusão.
	 * 
	 * @return redirecionamento para página jsp postagem/list.
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") Long id) { //insere o id referente ao RequestMapping através do PathVariable

		postagemService.delete(id); //realiza a exclusão

		return "redirect:/postagem/list"; //após exclusão redireciona para a página list.jsp
	}

	/**
	 * Método para listar as postagens ordenadas de forma asc.
	 * 
	 * * @param model o modelmap a add atributos e serem referenciados na página jsp.
	 * 
	 * @return view(acesso a página list.jsp do diretório postagem + @param model com seus atributos a serem referenciado na página jsp).
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listPostagens(ModelMap model) {

		//model.addAttribute("postagens", postagemService.findAll()); // Nome do Atributo / Valor do Atributo
		Page<Postagem> page = postagemService.findByPagination(0, 20); 
		//cria um objeto onde iremos armazenar o retorno da consulta / 0 é a variavel informada na url porém neste caso sempre será aberto a primeira página do formulário(0 igual a Array) / 5 é o tamanho de itens por página
		//objeto Page/Slice contém várias opções uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(contém a sua lista)
		//devido a todas essas opções absorvidas do objeto Page não é necessário incluir o List de categorias pois o Page já está fazendo este trabalho


		model.addAttribute("page", page); //não pode ser encontrado diretamente todas as categorias, necessita utilizar o Page acima pois senão ele não encontra as paginações ao abrir o formulário
		//model.addAttribute("urlPagination", "/postagem/page");

		return new ModelAndView("postagem/list", model); //diretório postagem / página list.jsp 
		//página que será acessada/visualizada / "model" contém a lista/page com as postagens
	}

	/**
	 * Método para salvar um objeto Postagem.
	 * 
	 * * @param postagem a @ModelAttribute postagem a ser salva a referenciando na página jsp através de seu modelAttribute.
	 * * @param result o resultado da validação da postagem.
	 * 
	 * @return view(redirecionamento para página jsp postagem/list).
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("postagem") @Validated Postagem postagem,
			BindingResult result, @AuthenticationPrincipal UsuarioLogado logado) {

		if ( result.hasErrors() ) {

			return new ModelAndView("postagem/cadastro", "categorias", categoriaService.findAll());
		}
		
		//postagem.setAutor(autorService.findByUsuario(logado.getId()));

		postagemService.saveOrUpdate(postagem);

		return new ModelAndView("redirect:/postagem/list");
	}

	/**
	 * Método para criar uma página básica com o formulário de cadastro de um objeto Postagem e será incluido também a listagem de categorias na mesma página .jsp.
	 * 
	 * * @param postagem a @ModelAttribute postagem a ser capturado na tela através de sua referencia do modelAttribute.
	 * 
	 * @return view(acesso a página cadastro.jsp do diretório postagem).
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView cadastro(@ModelAttribute("postagem") Postagem postagem) {
		
		ModelAndView view = new ModelAndView("postagem/cadastro"); //página que será acessada/visualizada
		view.addObject("categorias", categoriaService.findAll()); //relacionar a listagem das categorias ao acessar o cadastro da postagem (será selecionada uma/+ categoria(s) ao inserir uma postagem)
		return view;
	}
}