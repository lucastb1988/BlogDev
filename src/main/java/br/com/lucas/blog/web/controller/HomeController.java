package br.com.lucas.blog.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import br.com.lucas.blog.entity.Comentario;
import br.com.lucas.blog.entity.Postagem;
import br.com.lucas.blog.service.PostagemService;

/**
 * @author Lucas.
 * 
 *         Classe que representa o controller de Home(sua página principal do projeto) com seus respectivos métodos.
 * 
 */

@Controller
public class HomeController {

	@Autowired
	private PostagemService postagemService;

	/**
	 * Método para search por texto(palavra-chave) na home para encontrar por postagem utilizando variaveis de caminho.
	 * 
	 * * @param texto o @PathVariable texto a ser buscada por page.
	 * * @param pagina a @PathVariable pagina a ser buscada por page.
	 * * @param model o modelmap a add atributos e serem referenciados na página jsp.
	 * 
	 * @return view(acesso a página posts.jsp + @param model com seus atributos a serem referenciado na página jsp).
	 */
	@RequestMapping(value = "/search/texto/{texto}/page/{page}", method = RequestMethod.GET)
	public ModelAndView search(@PathVariable("texto") String texto,
			@PathVariable("page") Integer pagina, ModelMap model) { //PathParam relaciona o parametro informado no controller com o parametro na pagina posts.jsp (input name="texto") 
		// model é utilizado para que possamos trabalhar e enviar para página a resposta da nossa consulta

		Page<Postagem> page = postagemService.findByTexto(pagina - 1, 10, texto);

		model.addAttribute("page", page);
		model.addAttribute("urlPagination", "/search/texto/" + texto + "/page");

		return new ModelAndView("posts", model);
	}

	/**
	 * Método para search por texto(palavra-chave) na home para encontrar por postagem utilizando requisições de parametro.
	 * 
	 * * @param texto o @RequestParam texto a ser buscada por page.
	 * * @param model o modelmap a add atributos e serem referenciados na página jsp.
	 * 
	 * @return view(acesso a página posts.jsp + @param model com seus atributos a serem referenciado na página jsp).
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam("texto") String texto, ModelMap model) { //RequestParam relaciona o parametro informado no controller com o parametro na pagina posts.jsp(input name="texto") 
		// model é utilizado para que possamos trabalhar e enviar para página a resposta da nossa consulta

		Page<Postagem> page = postagemService.findByTexto(0, 10, texto);

		model.addAttribute("page", page);
		model.addAttribute("urlPagination", "/search/texto/" + texto + "/page");

		return new ModelAndView("posts", model);
	}

	/**
	 * Método para abrir a postagem selecionada e trazer também os comentários.
	 * 
	 * * @param comentario o @ModelAttribute comentario a ser salvo a referenciando na página jsp através de seu modelAttribute.
	 * * @param permalink o @PathVariable permalink a ser buscada por postagem.
	 * * @param model o modelmap a add atributos e serem referenciados na página jsp.
	 * 
	 * @return view(acesso a página post.jsp + @param model com seus atributos a serem referenciado na página jsp).
	 */
	//método para abrir a postagem selecionada e trazer também os comentários
	@RequestMapping(value = "/{permalink}", method = RequestMethod.GET)
	public ModelAndView openPostagem(
			@ModelAttribute("comentario") Comentario comentario, //atributo inserido no formulario da página comments.jsp
			@PathVariable("permalink") String permalink, ModelMap model) throws NoHandlerFoundException { //ModelMap serve para enviar o objeto para página

		Postagem postagem = postagemService.findByPermalink(permalink);

		if(postagem == null) {
			throw new NoHandlerFoundException(null, null, null); 
			//se tentar incluir na url {permalink} um permalink que não existe(não possui tal conteudo) irá lançar a exceção NoHandlerFoundException
		}

		model.addAttribute("postagem", postagem);

		return new ModelAndView("post", model);
	}

	/**
	 * Método para acessar os posts pelo Autor através do id e utilizando paginação.
	 * 
	 * * @param id o @PathVariable id a ser buscada por page.
	 * * @param pagina a @PathVariable pagina a ser buscada por page.
	 * * @param model o modelmap a add atributos e serem referenciados na página jsp.
	 * 
	 * @return view(acesso a página posts.jsp + @param model com seus atributos a serem referenciado na página jsp).
	 */
	@RequestMapping(value = "/autor/{id}/page/{page}", method = RequestMethod.GET)
	public ModelAndView postsByAutor(@PathVariable("id") Long id,
			@PathVariable("page") Integer pagina, ModelMap model) {

		//List<Postagem> postagens = postagemService.findByAutor(nome); //atribui a lista de postagens o autor encontrado através do id
		//model.addAttribute("postagens", postagens); // variavel postagens com a sua lista

		Page<Postagem> page = postagemService.findByPaginationByAutor(pagina - 1, 5, id); 
		//cria um objeto onde iremos armazenar o retorno da consulta / 0 é a variavel informada na url porém neste caso sempre será aberto a primeira página do formulário(0 igual a Array) / 5 é o tamanho de itens por página
		//objeto Page/Slice contém várias opções uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(contém a sua lista)
		//devido a todas essas opções absorvidas do objeto Page não é necessário incluir o List de categorias pois o Page já está fazendo este trabalho

		model.addAttribute("page", page); //não pode ser encontrado diretamente todas as categorias, necessita utilizar o Page acima pois senão ele não encontra as paginações ao abrir o formulário
		model.addAttribute("urlPagination", "/autor/" + id + "/page"); //adicionando urlPagination pois para paginar por autor na home, após mudar de página no autor ele se perde e traz outros autores não selecionados
		//urlPagination é adicionado no link das pages da pagina posts.jsp para corrigir este erro usando o valor "/page" 
		//utiliza o valor "/autor/" + link + "/page" para concatenar com link + page	

		return new ModelAndView("posts", model); // posts - página que será acessada / model irá levar sua variavel(list neste caso) para a página
	}

	/**
	 * Método para acessar os posts pela Categoria através do link(permalink) e utilizando paginação.
	 * 
	 * * @param pagina a @PathVariable pagina a ser buscada por page.
	 * * @param link o @PathVariable link a ser buscada por page.
	 * * @param model o modelmap a add atributos e serem referenciados na página jsp.
	 * 
	 * @return view(acesso a página posts.jsp + @param model com seus atributos a serem referenciado na página jsp).
	 */
	@RequestMapping(value = "/categoria/{link}/page/{page}", method = RequestMethod.GET)
	public ModelAndView postsByCategoria(@PathVariable("page") Integer pagina,
			@PathVariable("link") String link, ModelMap model) {

		//List<Postagem> postagens = postagemService.findByCategoria(link); //atribui a lista de postagens a categoria encontrada através do link(permalink)
		//model.addAttribute("postagens", postagens); // variavel postagens com a sua lista

		Page<Postagem> page = postagemService.findByPaginationByCategoria(pagina - 1, 5, link); 
		//cria um objeto onde iremos armazenar o retorno da consulta / 0 é a variavel informada na url porém neste caso sempre será aberto a primeira página do formulário(0 igual a Array) / 5 é o tamanho de itens por página
		//objeto Page/Slice contém várias opções uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(contém a sua lista)
		//devido a todas essas opções absorvidas do objeto Page não é necessário incluir o List de categorias pois o Page já está fazendo este trabalho

		model.addAttribute("page", page); //não pode ser encontrado diretamente todas as categorias, necessita utilizar o Page acima pois senão ele não encontra as paginações ao abrir o formulário
		model.addAttribute("urlPagination", "/categoria/" + link + "/page"); //adicionando urlPagination pois para paginar por categoria na home, após mudar de página na categoria ele se perde e traz outras categorias não selecionadas
		//urlPagination é adicionado no link das pages da pagina posts.jsp para corrigir este erro usando o valor "/page" 
		//utiliza o valor "/categoria/" + link + "/page" para concatenar com link + page	

		return new ModelAndView("posts", model); // posts - página que será acessada / model irá levar sua variavel(list neste caso) para a página
	}

	/**
	 * Método para paginação das postagens ordenadas de forma asc.
	 * 
	 * * @param pagina a @PathVariable pagina a ser buscada por page.
	 * * @param model o modelmap a add atributos e serem referenciados na página jsp.
	 * 
	 * @return view(acesso a página posts.jsp + @param model com seus atributos a serem referenciado na página jsp).
	 */
	@RequestMapping(value = "/page/{page}", method = RequestMethod.GET) //{page} é a página que iremos buscar
	public ModelAndView pageHome(@PathVariable("page") Integer pagina, ModelMap model) throws NoHandlerFoundException {

		Page<Postagem> page = postagemService.findByPagination(pagina - 1, 10); 
		//cria um objeto onde iremos armazenar o retorno da consulta 
		//pagina é a variavel informada na url, necessita trabalhar com -1 pois a página começa com 0 porém o usuario visualiza como página / 5 é o tamanho de itens por página
		//objeto Page/Slice contém várias opções uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(contém o conteudo da lista)
		//devido a todas essas opções absorvidas do objeto Page não é necessário incluir o List de categorias pois o Page já está fazendo este trabalho

		if(page.getContent().isEmpty()) {
			throw new NoHandlerFoundException(null, null, null); 
			//se tentar incluir na url page/{numero} pagina que não existe(não possui tal conteudo) irá lançar a exceção NoHandlerFoundException
		}

		model.addAttribute("page", page); // adiciona na view este objeto page criado acima/ "page" é o nome do objeto que vamos enviar para página / page contém o resultado da nossa consulta(valor) 
		model.addAttribute("urlPagination", "/page"); //adicionando urlPagination pois para paginar por categoria na home, após mudar de página na categoria ele se perde e traz outras categorias não selecionadas
		// urlPagination é adicionado no link das pages da pagina posts.jsp para corrigir este erro usando o valor "/page" 

		return new ModelAndView("posts", model); // posts - página que será acessada / model irá levar sua variavel(list neste caso) para a página
	}

	/**
	 * Método de acesso a página home utilizando simbolo "/" como url e listar as postagens cadastradas na página principal(home).
	 * 
	 * * @param model o modelmap a add atributos e serem referenciados na página jsp.
	 * 
	 * @return view(acesso a página cadastro.jsp do diretório categoria).
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home(ModelMap model) {

		//List<Postagem> postagens = postagemService.findAll(); //encontra todos as postagens cadastradas no bd e atribui a postagens
		//model.addAttribute("postagens", postagens); // variavel postagens com a sua lista

		Page<Postagem> page = postagemService.findByPagination(0, 10); 
		//cria um objeto onde iremos armazenar o retorno da consulta / 0 é a variavel informada na url porém neste caso sempre será aberto a primeira página do formulário(0 igual a Array) / 5 é o tamanho de itens por página
		//objeto Page/Slice contém várias opções uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(contém a sua lista)
		//devido a todas essas opções absorvidas do objeto Page não é necessário incluir o List de categorias pois o Page já está fazendo este trabalho

		model.addAttribute("page", page); //não pode ser encontrado diretamente todas as categorias, necessita utilizar o Page acima pois senão ele não encontra as paginações ao abrir o formulário
		model.addAttribute("urlPagination", "/page"); //adicionando urlPagination pois para paginar por categoria na home, após mudar de página na categoria ele se perde e traz outras categorias não selecionadas
		// urlPagination é adicionado no link das pages da pagina posts.jsp para corrigir este erro usando o valor "/page" 

		return new ModelAndView("posts", model); // posts - página.jsp que será acessada / model irá levar sua variavel(list neste caso) para a página
	}
}