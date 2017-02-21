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
 *         Classe que representa o controller de Home(sua p�gina principal do projeto) com seus respectivos m�todos.
 * 
 */

@Controller
public class HomeController {

	@Autowired
	private PostagemService postagemService;

	/**
	 * M�todo para search por texto(palavra-chave) na home para encontrar por postagem utilizando variaveis de caminho.
	 * 
	 * * @param texto o @PathVariable texto a ser buscada por page.
	 * * @param pagina a @PathVariable pagina a ser buscada por page.
	 * * @param model o modelmap a add atributos e serem referenciados na p�gina jsp.
	 * 
	 * @return view(acesso a p�gina posts.jsp + @param model com seus atributos a serem referenciado na p�gina jsp).
	 */
	@RequestMapping(value = "/search/texto/{texto}/page/{page}", method = RequestMethod.GET)
	public ModelAndView search(@PathVariable("texto") String texto,
			@PathVariable("page") Integer pagina, ModelMap model) { //PathParam relaciona o parametro informado no controller com o parametro na pagina posts.jsp (input name="texto") 
		// model � utilizado para que possamos trabalhar e enviar para p�gina a resposta da nossa consulta

		Page<Postagem> page = postagemService.findByTexto(pagina - 1, 10, texto);

		model.addAttribute("page", page);
		model.addAttribute("urlPagination", "/search/texto/" + texto + "/page");

		return new ModelAndView("posts", model);
	}

	/**
	 * M�todo para search por texto(palavra-chave) na home para encontrar por postagem utilizando requisi��es de parametro.
	 * 
	 * * @param texto o @RequestParam texto a ser buscada por page.
	 * * @param model o modelmap a add atributos e serem referenciados na p�gina jsp.
	 * 
	 * @return view(acesso a p�gina posts.jsp + @param model com seus atributos a serem referenciado na p�gina jsp).
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam("texto") String texto, ModelMap model) { //RequestParam relaciona o parametro informado no controller com o parametro na pagina posts.jsp(input name="texto") 
		// model � utilizado para que possamos trabalhar e enviar para p�gina a resposta da nossa consulta

		Page<Postagem> page = postagemService.findByTexto(0, 10, texto);

		model.addAttribute("page", page);
		model.addAttribute("urlPagination", "/search/texto/" + texto + "/page");

		return new ModelAndView("posts", model);
	}

	/**
	 * M�todo para abrir a postagem selecionada e trazer tamb�m os coment�rios.
	 * 
	 * * @param comentario o @ModelAttribute comentario a ser salvo a referenciando na p�gina jsp atrav�s de seu modelAttribute.
	 * * @param permalink o @PathVariable permalink a ser buscada por postagem.
	 * * @param model o modelmap a add atributos e serem referenciados na p�gina jsp.
	 * 
	 * @return view(acesso a p�gina post.jsp + @param model com seus atributos a serem referenciado na p�gina jsp).
	 */
	//m�todo para abrir a postagem selecionada e trazer tamb�m os coment�rios
	@RequestMapping(value = "/{permalink}", method = RequestMethod.GET)
	public ModelAndView openPostagem(
			@ModelAttribute("comentario") Comentario comentario, //atributo inserido no formulario da p�gina comments.jsp
			@PathVariable("permalink") String permalink, ModelMap model) throws NoHandlerFoundException { //ModelMap serve para enviar o objeto para p�gina

		Postagem postagem = postagemService.findByPermalink(permalink);

		if(postagem == null) {
			throw new NoHandlerFoundException(null, null, null); 
			//se tentar incluir na url {permalink} um permalink que n�o existe(n�o possui tal conteudo) ir� lan�ar a exce��o NoHandlerFoundException
		}

		model.addAttribute("postagem", postagem);

		return new ModelAndView("post", model);
	}

	/**
	 * M�todo para acessar os posts pelo Autor atrav�s do id e utilizando pagina��o.
	 * 
	 * * @param id o @PathVariable id a ser buscada por page.
	 * * @param pagina a @PathVariable pagina a ser buscada por page.
	 * * @param model o modelmap a add atributos e serem referenciados na p�gina jsp.
	 * 
	 * @return view(acesso a p�gina posts.jsp + @param model com seus atributos a serem referenciado na p�gina jsp).
	 */
	@RequestMapping(value = "/autor/{id}/page/{page}", method = RequestMethod.GET)
	public ModelAndView postsByAutor(@PathVariable("id") Long id,
			@PathVariable("page") Integer pagina, ModelMap model) {

		//List<Postagem> postagens = postagemService.findByAutor(nome); //atribui a lista de postagens o autor encontrado atrav�s do id
		//model.addAttribute("postagens", postagens); // variavel postagens com a sua lista

		Page<Postagem> page = postagemService.findByPaginationByAutor(pagina - 1, 5, id); 
		//cria um objeto onde iremos armazenar o retorno da consulta / 0 � a variavel informada na url por�m neste caso sempre ser� aberto a primeira p�gina do formul�rio(0 igual a Array) / 5 � o tamanho de itens por p�gina
		//objeto Page/Slice cont�m v�rias op��es uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(cont�m a sua lista)
		//devido a todas essas op��es absorvidas do objeto Page n�o � necess�rio incluir o List de categorias pois o Page j� est� fazendo este trabalho

		model.addAttribute("page", page); //n�o pode ser encontrado diretamente todas as categorias, necessita utilizar o Page acima pois sen�o ele n�o encontra as pagina��es ao abrir o formul�rio
		model.addAttribute("urlPagination", "/autor/" + id + "/page"); //adicionando urlPagination pois para paginar por autor na home, ap�s mudar de p�gina no autor ele se perde e traz outros autores n�o selecionados
		//urlPagination � adicionado no link das pages da pagina posts.jsp para corrigir este erro usando o valor "/page" 
		//utiliza o valor "/autor/" + link + "/page" para concatenar com link + page	

		return new ModelAndView("posts", model); // posts - p�gina que ser� acessada / model ir� levar sua variavel(list neste caso) para a p�gina
	}

	/**
	 * M�todo para acessar os posts pela Categoria atrav�s do link(permalink) e utilizando pagina��o.
	 * 
	 * * @param pagina a @PathVariable pagina a ser buscada por page.
	 * * @param link o @PathVariable link a ser buscada por page.
	 * * @param model o modelmap a add atributos e serem referenciados na p�gina jsp.
	 * 
	 * @return view(acesso a p�gina posts.jsp + @param model com seus atributos a serem referenciado na p�gina jsp).
	 */
	@RequestMapping(value = "/categoria/{link}/page/{page}", method = RequestMethod.GET)
	public ModelAndView postsByCategoria(@PathVariable("page") Integer pagina,
			@PathVariable("link") String link, ModelMap model) {

		//List<Postagem> postagens = postagemService.findByCategoria(link); //atribui a lista de postagens a categoria encontrada atrav�s do link(permalink)
		//model.addAttribute("postagens", postagens); // variavel postagens com a sua lista

		Page<Postagem> page = postagemService.findByPaginationByCategoria(pagina - 1, 5, link); 
		//cria um objeto onde iremos armazenar o retorno da consulta / 0 � a variavel informada na url por�m neste caso sempre ser� aberto a primeira p�gina do formul�rio(0 igual a Array) / 5 � o tamanho de itens por p�gina
		//objeto Page/Slice cont�m v�rias op��es uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(cont�m a sua lista)
		//devido a todas essas op��es absorvidas do objeto Page n�o � necess�rio incluir o List de categorias pois o Page j� est� fazendo este trabalho

		model.addAttribute("page", page); //n�o pode ser encontrado diretamente todas as categorias, necessita utilizar o Page acima pois sen�o ele n�o encontra as pagina��es ao abrir o formul�rio
		model.addAttribute("urlPagination", "/categoria/" + link + "/page"); //adicionando urlPagination pois para paginar por categoria na home, ap�s mudar de p�gina na categoria ele se perde e traz outras categorias n�o selecionadas
		//urlPagination � adicionado no link das pages da pagina posts.jsp para corrigir este erro usando o valor "/page" 
		//utiliza o valor "/categoria/" + link + "/page" para concatenar com link + page	

		return new ModelAndView("posts", model); // posts - p�gina que ser� acessada / model ir� levar sua variavel(list neste caso) para a p�gina
	}

	/**
	 * M�todo para pagina��o das postagens ordenadas de forma asc.
	 * 
	 * * @param pagina a @PathVariable pagina a ser buscada por page.
	 * * @param model o modelmap a add atributos e serem referenciados na p�gina jsp.
	 * 
	 * @return view(acesso a p�gina posts.jsp + @param model com seus atributos a serem referenciado na p�gina jsp).
	 */
	@RequestMapping(value = "/page/{page}", method = RequestMethod.GET) //{page} � a p�gina que iremos buscar
	public ModelAndView pageHome(@PathVariable("page") Integer pagina, ModelMap model) throws NoHandlerFoundException {

		Page<Postagem> page = postagemService.findByPagination(pagina - 1, 10); 
		//cria um objeto onde iremos armazenar o retorno da consulta 
		//pagina � a variavel informada na url, necessita trabalhar com -1 pois a p�gina come�a com 0 por�m o usuario visualiza como p�gina / 5 � o tamanho de itens por p�gina
		//objeto Page/Slice cont�m v�rias op��es uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(cont�m o conteudo da lista)
		//devido a todas essas op��es absorvidas do objeto Page n�o � necess�rio incluir o List de categorias pois o Page j� est� fazendo este trabalho

		if(page.getContent().isEmpty()) {
			throw new NoHandlerFoundException(null, null, null); 
			//se tentar incluir na url page/{numero} pagina que n�o existe(n�o possui tal conteudo) ir� lan�ar a exce��o NoHandlerFoundException
		}

		model.addAttribute("page", page); // adiciona na view este objeto page criado acima/ "page" � o nome do objeto que vamos enviar para p�gina / page cont�m o resultado da nossa consulta(valor) 
		model.addAttribute("urlPagination", "/page"); //adicionando urlPagination pois para paginar por categoria na home, ap�s mudar de p�gina na categoria ele se perde e traz outras categorias n�o selecionadas
		// urlPagination � adicionado no link das pages da pagina posts.jsp para corrigir este erro usando o valor "/page" 

		return new ModelAndView("posts", model); // posts - p�gina que ser� acessada / model ir� levar sua variavel(list neste caso) para a p�gina
	}

	/**
	 * M�todo de acesso a p�gina home utilizando simbolo "/" como url e listar as postagens cadastradas na p�gina principal(home).
	 * 
	 * * @param model o modelmap a add atributos e serem referenciados na p�gina jsp.
	 * 
	 * @return view(acesso a p�gina cadastro.jsp do diret�rio categoria).
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home(ModelMap model) {

		//List<Postagem> postagens = postagemService.findAll(); //encontra todos as postagens cadastradas no bd e atribui a postagens
		//model.addAttribute("postagens", postagens); // variavel postagens com a sua lista

		Page<Postagem> page = postagemService.findByPagination(0, 10); 
		//cria um objeto onde iremos armazenar o retorno da consulta / 0 � a variavel informada na url por�m neste caso sempre ser� aberto a primeira p�gina do formul�rio(0 igual a Array) / 5 � o tamanho de itens por p�gina
		//objeto Page/Slice cont�m v�rias op��es uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(cont�m a sua lista)
		//devido a todas essas op��es absorvidas do objeto Page n�o � necess�rio incluir o List de categorias pois o Page j� est� fazendo este trabalho

		model.addAttribute("page", page); //n�o pode ser encontrado diretamente todas as categorias, necessita utilizar o Page acima pois sen�o ele n�o encontra as pagina��es ao abrir o formul�rio
		model.addAttribute("urlPagination", "/page"); //adicionando urlPagination pois para paginar por categoria na home, ap�s mudar de p�gina na categoria ele se perde e traz outras categorias n�o selecionadas
		// urlPagination � adicionado no link das pages da pagina posts.jsp para corrigir este erro usando o valor "/page" 

		return new ModelAndView("posts", model); // posts - p�gina.jsp que ser� acessada / model ir� levar sua variavel(list neste caso) para a p�gina
	}
}