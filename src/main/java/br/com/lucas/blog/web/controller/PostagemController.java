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
 *         Classe que representa o controller de Postagem com seus respectivos m�todos.
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

	//Ao inicializar ser� consumido a convers�o de objeto Categoria para list Categoria
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(
				List.class, 
				new CategoriaEditorSupport(List.class, categoriaService)
				);
	}
	
	//m�todo para search por titulo(palavra-chave) na postagem para encontrar por titulo utilizando ajax
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
	 * M�todo para listar todas as Postagens por pagina��o por Autor.
	 * 
	 * * @param id o @PathVariable id a ser buscada por page.
	 * * @param model o modelmap a add atributos e serem referenciados na p�gina jsp.
	 * 
	 * @return view(acesso a p�gina list.jsp do diret�rio postagem + @param model com seus atributos a serem referenciado na p�gina jsp).
	 */
	@RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	public ModelAndView listPostagensByAutor(@PathVariable("id") Long id, ModelMap model) {

		Long autorId = autorService.findByUsuario(id).getId();		

		Page<Postagem> page = postagemService.findByPaginationByAutor(0, 5, autorId); 
		//cria um objeto onde iremos armazenar o retorno da consulta / 0 � a variavel informada na url por�m neste caso sempre ser� aberto a primeira p�gina do formul�rio(0 igual a Array) / 5 � o tamanho de itens por p�gina
		//objeto Page/Slice cont�m v�rias op��es uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(cont�m a sua lista)
		//devido a todas essas op��es absorvidas do objeto Page n�o � necess�rio incluir o List de categorias pois o Page j� est� fazendo este trabalho

		model.addAttribute("page", page); //n�o pode ser encontrado diretamente todas as categorias, necessita utilizar o Page acima pois sen�o ele n�o encontra as pagina��es ao abrir o formul�rio
		model.addAttribute("autorId", autorId);	

		return new ModelAndView("postagem/list", model); // diret�rio postagem / p�gina list.jsp 
		// p�gina que ser� acessada/visualizada / "model" cont�m a lista com as postagens
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

	//m�todo para search por titulo(palavra-chave) na postagem para encontrar por titulo utilizando ajax
	@RequestMapping(value = "/ajax/titulo/{titulo}/page/{page}", method = RequestMethod.GET) 
	public ModelAndView searchByAjax(@PathVariable("titulo") String titulo,
			@PathVariable("page") Integer pagina) {

		ModelAndView view = new ModelAndView("postagem/table-rows");

		Page<Postagem> page = postagemService.findByTitulo(pagina - 1, 20, titulo);

		view.addObject("page", page);

		return view;
	}	

	/**
	 * M�todo para pagina��o das postagens ordenadas de forma asc
	 * 
	 * * @param pagina a @PathVariable pagina a ser buscada por page.
	 * 
	 * @return view(acesso a p�gina table-rows.jsp do diret�rio postagem).
	 */
	//m�todo para pagina��o dos usuarios ordenadas de forma asc
	@RequestMapping(value = "/ajax/page/{page}", method = RequestMethod.GET) //{page} � a p�gina que iremos buscar
	public ModelAndView pagePostagens(@PathVariable("page") Integer pagina) {

		ModelAndView view = new ModelAndView("postagem/table-rows"); //ir� acessar a p�gina de table-rows.jsp, a lista de usuarios se encontra

		Page<Postagem> page = postagemService.findByPagination(pagina - 1, 20); 
		//cria um objeto onde iremos armazenar o retorno da consulta 
		//pagina � a variavel informada na url, necessita trabalhar com -1 pois a p�gina come�a com 0 por�m o usuario visualiza como p�gina / 5 � o tamanho de itens por p�gina
		//objeto Page/Slice cont�m v�rias op��es uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(cont�m o conteudo da lista)
		//devido a todas essas op��es absorvidas do objeto Page n�o � necess�rio incluir o List de categorias pois o Page j� est� fazendo este trabalho


		view.addObject("page", page); // adiciona na view este objeto page criado acima/ "page" � o nome do objeto que vamos enviar para p�gina / page cont�m o resultado da nossa consulta(valor) 
		//view.addObject("urlPagination", "/postagem/page");

		return view;
	}

	/**
	 * M�todo para pr�-editar um objeto Postagem.
	 * 
	 * * @param id a id a ser buscado para realizar update.
	 * * @param model o modelmap a add atributos e serem referenciados na p�gina jsp.
	 * 
	 * @return view(acesso a p�gina cadastro.jsp do diret�rio postagem + @param model com seus atributos a serem referenciado na p�gina jsp).
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public ModelAndView preUpdate(@PathVariable("id") Long id, ModelMap model) {

		Postagem postagem = postagemService.findById(id); //procura pelo id e atribui junto a postagem o id

		model.addAttribute("postagem", postagem); //p�gina que ser� acessada/visualizada / retorna para p�gina o valor que ser� localizado a partir da consulta que faremos por id
		model.addAttribute("categorias", categoriaService.findAll()); //relacionar a listagem das categorias ao acessar o cadastro da postagem (ser� selecionada uma categoria ap�s inser��o/update da postagem)

		return new ModelAndView("postagem/cadastro", model); // diret�rio postagem / p�gina list.jsp / 
		// p�gina que ser� acessada/visualizada / "model" receber� o objeto que est� sendo retornado para ser alterado 
	}

	/**
	 * M�todo para excluir um objeto Postagem.
	 * 
	 * * @param id o @PathVariable id a ser buscado para realizar exclus�o.
	 * 
	 * @return redirecionamento para p�gina jsp postagem/list.
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") Long id) { //insere o id referente ao RequestMapping atrav�s do PathVariable

		postagemService.delete(id); //realiza a exclus�o

		return "redirect:/postagem/list"; //ap�s exclus�o redireciona para a p�gina list.jsp
	}

	/**
	 * M�todo para listar as postagens ordenadas de forma asc.
	 * 
	 * * @param model o modelmap a add atributos e serem referenciados na p�gina jsp.
	 * 
	 * @return view(acesso a p�gina list.jsp do diret�rio postagem + @param model com seus atributos a serem referenciado na p�gina jsp).
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listPostagens(ModelMap model) {

		//model.addAttribute("postagens", postagemService.findAll()); // Nome do Atributo / Valor do Atributo
		Page<Postagem> page = postagemService.findByPagination(0, 20); 
		//cria um objeto onde iremos armazenar o retorno da consulta / 0 � a variavel informada na url por�m neste caso sempre ser� aberto a primeira p�gina do formul�rio(0 igual a Array) / 5 � o tamanho de itens por p�gina
		//objeto Page/Slice cont�m v�rias op��es uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(cont�m a sua lista)
		//devido a todas essas op��es absorvidas do objeto Page n�o � necess�rio incluir o List de categorias pois o Page j� est� fazendo este trabalho


		model.addAttribute("page", page); //n�o pode ser encontrado diretamente todas as categorias, necessita utilizar o Page acima pois sen�o ele n�o encontra as pagina��es ao abrir o formul�rio
		//model.addAttribute("urlPagination", "/postagem/page");

		return new ModelAndView("postagem/list", model); //diret�rio postagem / p�gina list.jsp 
		//p�gina que ser� acessada/visualizada / "model" cont�m a lista/page com as postagens
	}

	/**
	 * M�todo para salvar um objeto Postagem.
	 * 
	 * * @param postagem a @ModelAttribute postagem a ser salva a referenciando na p�gina jsp atrav�s de seu modelAttribute.
	 * * @param result o resultado da valida��o da postagem.
	 * 
	 * @return view(redirecionamento para p�gina jsp postagem/list).
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
	 * M�todo para criar uma p�gina b�sica com o formul�rio de cadastro de um objeto Postagem e ser� incluido tamb�m a listagem de categorias na mesma p�gina .jsp.
	 * 
	 * * @param postagem a @ModelAttribute postagem a ser capturado na tela atrav�s de sua referencia do modelAttribute.
	 * 
	 * @return view(acesso a p�gina cadastro.jsp do diret�rio postagem).
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView cadastro(@ModelAttribute("postagem") Postagem postagem) {
		
		ModelAndView view = new ModelAndView("postagem/cadastro"); //p�gina que ser� acessada/visualizada
		view.addObject("categorias", categoriaService.findAll()); //relacionar a listagem das categorias ao acessar o cadastro da postagem (ser� selecionada uma/+ categoria(s) ao inserir uma postagem)
		return view;
	}
}