package br.com.lucas.blog.web.controller;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.lucas.blog.entity.Autor;
import br.com.lucas.blog.entity.Usuario;
import br.com.lucas.blog.entity.UsuarioLogado;
import br.com.lucas.blog.service.AutorService;
import br.com.lucas.blog.service.UsuarioService;

/**
 * @author Lucas.
 * 
 *         Classe que representa o controller de Autor com seus respectivos m�todos.
 * 
 */

@Controller
@RequestMapping("autor")
public class AutorController {

	@Autowired
	private AutorService autorService;

	@Autowired
	private UsuarioService usuarioService;

	/**
	 * M�todo para pagina��o da lista da entidade Autor ordenadas de forma Asc.
	 * 
	 * * @param pagina a @PathVariable pagina a ser buscada por page.
	 * 
	 * @return view(acesso a p�gina perfil.jsp do diret�rio autor).
	 */
	@RequestMapping(value = "page/{page}", method = RequestMethod.GET) //{page} � a p�gina que iremos buscar
	public ModelAndView pageAutores(@PathVariable("page") Integer pagina) {

		ModelAndView view = new ModelAndView("autor/perfil"); ////ModelAndView � utilizado para o m�todo GET/ ir� acessar a p�gina de list.jsp, a lista de usuarios se encontra nesta lista para pagina��o

		Page<Autor> page = autorService.findByPagination(pagina - 1, 10); //cria um objeto onde iremos armazenar o retorno da consulta 
		//pagina � a variavel informada na url, necessita trabalhar com -1 pois a p�gina come�a com 0 por�m o usuario visualiza como p�gina / 5 � o tamanho de itens por p�gina
		//objeto Page/Slice cont�m v�rias op��es uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(cont�m o conteudo da lista)
		//devido a todas essas op��es absorvidas do objeto Page n�o � necess�rio incluir o List de categorias pois o Page j� est� fazendo este trabalho

		view.addObject("page", page); // adiciona na view este objeto page criado acima/ "page" � o nome do objeto que vamos enviar para p�gina / page cont�m o resultado da nossa consulta(valor) 
		view.addObject("urlPagination", "/autor/page");

		return view;
	}

	/**
	 * M�todo para deletar um objeto Autor.
	 * 
	 * * @param id a id a ser buscado para realizar exclus�o.
	 * 
	 * @return redirecionamento para p�gina jsp autor/add.
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") Long id) {

		autorService.delete(id);

		return "redirect:/autor/add";
	}

	/**
	 * M�todo para editar um objeto Autor.
	 * 
	 * * @param id a id a ser buscado para realizar update.
	 * 
	 * @return view(acesso a p�gina perfil.jsp do diret�rio autor).
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public ModelAndView preUpdate(@PathVariable("id") Long id) {

		ModelAndView view = new ModelAndView("autor/cadastro");

		Autor autor = autorService.findById(id);

		view.addObject("autor", autor);

		return view;
	}

	/**
	 * M�todo para capturar um objeto Autor cadastrado.
	 * 
	 * * @param id o Optional<Long> id a ser buscado para capturar um objeto Autor.
	 * 
	 * @return view(acesso a p�gina perfil.jsp do diret�rio autor).
	 */
	//este m�todo getAutor abaixo poderia ser criado separadamente as 2 RequestMapping normalmente, 
	//por�m neste exemplo foram criadas juntas baseadas em 2 urls diferentes para uma an�lise diferente,
	//tamb�m foi utilizada apenas uma p�gina jsp (perfil.jsp) para tratar as 2 urls juntas apenas adicionando forEach para a list

	//metodo que ir� acessar a p�gina perfil.jsp / exibe os dados do autor cadastrado
	//ser� criado no mesmo m�todo um array de urls.
	// - 1� RequestMapping ser� utilizado para encontrar o perfil atrav�s de somente 1 id
	// - 2� RequestMapping ser� utilizado para encontrar todos os autores cadastrados(list)
	@RequestMapping(value = {"/perfil/{id}", "/list"}, method = RequestMethod.GET)
	public ModelAndView getAutor(@PathVariable("id") Optional<Long> id) {

		ModelAndView view = new ModelAndView("autor/perfil"); //a mesma view/p�gina ser� mostrado ou somente 1 id capturado no 1�request ou a lista capturado no 2� request 

		//Se encontrar/existir 1 id ir� trabalhar com 1� RequestMapping / est� buscando pelo (/perfil/{id})
		//true or false (est� presente ou n�o - (isPresent))
		if(id.isPresent()) {
			Autor autor = autorService.findById(id.get()); //consulta pelo id para recuperar o autor 

			view.addObject("autores", Arrays.asList(autor)); //se for encontrado somente 1 autor ser� mostrado somente 1 na p�gina jsp. se tiver mais de 1 autor cair� na list abaixo	

			//Se pular o if, caira no else e ir� trazer toda a lista capturada de autores
		} else {
			//List<Autor> autores = autorService.findAll(); //define na lista autores o conteudo de todos autores cadastrados
			//view.addObject("autores", autores); // autores - cont�udo da lista que ser� atribuido na p�gina jsp conforme foi capturado na List acima

			Page<Autor> page = autorService.findByPagination(0, 10); 
			//cria um objeto onde iremos armazenar o retorno da consulta / 0 � a variavel informada na url por�m neste caso sempre ser� aberto a primeira p�gina do formul�rio(0 igual a um Array) / 10 � o tamanho de itens por p�gina
			//objeto Page/Slice cont�m v�rias op��es uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(cont�m a sua lista)
			//devido a todas essas op��es absorvidas do objeto Page n�o � necess�rio incluir o List de categorias pois o Page j� est� fazendo este trabalho

			view.addObject("page", page); //n�o pode ser encontrado diretamente todas as categorias, necessita utilizar o Page acima pois sen�o ele n�o encontra as pagina��es ao abrir o formul�rio
			view.addObject("urlPagination", "/autor/page");
		}

		return view;	
	}

	/**
	 * M�todo para salvar um objeto Autor.
	 * 
	 * * @param autor o @ModelAttribute autor a ser salvo o referenciando na p�gina jsp atrav�s de seu modelAttribute.
	 * * @param result o resultado da valida��o do autor.
	 * 
	 * @return redirecionamento para p�gina jsp autor/perfil + id capturado do autor informado.
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST) //utiliza a url /save para encontrar este m�todo + m�todo Post (para inser��o)
	public String save(@ModelAttribute("autor") @Validated Autor autor, BindingResult result,
			@AuthenticationPrincipal UsuarioLogado logado) { //utiliza a vari�vel autor nas p�ginas jsp
		//BindingResult ir� testar se existe erro ou n�o atrav�s do validator

		if( result.hasErrors() ) { //se o bindingresult contiver erros ele volta para p�gina de cadastro sem validar e salvar
			return "autor/cadastro";
		}

		//		if( logado.getId() != null ) {
		//			Usuario usuario = usuarioService.findById(logado.getId()); //realiza a consulta para inserir no objeto autor o objeto usuario
		//			autor.setUsuario(usuario); //atribui ao autor o id do usuario
		//		}

		autorService.save(autor); //salva autor

		return "redirect:/autor/perfil/" + autor.getId(); //ao salvar um autor gera autom�tico o id e redireciona para a p�gina perfil.jsp
	}

	/**
	 * M�todo para criar uma p�gina b�sica com o formul�rio de cadastro de um objeto Autor.
	 * 
	 * * @param autor o @ModelAttribute autor a ser capturado na tela o referenciando na p�gina jsp atrav�s de seu modelAttribute.
	 * 
	 * @return view(acesso a p�gina cadastro.jsp do diret�rio autor).
	 */
	//m�todo para redirecionar para a p�gina cadastro.jsp / abre o cadastro de autor
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView addAutor(@ModelAttribute("autor") Autor autor, 
			@AuthenticationPrincipal() UsuarioLogado logado) {

		//		autor = autorService.findByUsuario(logado.getId()); //verifica se o autor j� possui id e o atribui a autor
		//
		//		if(autor == null) {
		//			return new ModelAndView("autor/cadastro"); //se autor for nulo retorna para p�gina de cadastro para o autor realizar seu cadastro
		//		}

		return new ModelAndView("redirect:/autor/cadastro"); //sen�o retorna para seu perfil com o respectivo id	
	}
}