package br.com.lucas.blog.web.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.servlet.ModelAndView;

import br.com.lucas.blog.entity.Avatar;
import br.com.lucas.blog.entity.Usuario;
import br.com.lucas.blog.enumeradores.Perfil;
import br.com.lucas.blog.service.AvatarService;
import br.com.lucas.blog.service.UsuarioService;
import br.com.lucas.blog.web.editor.PerfilEditorSupport;
import br.com.lucas.blog.web.validator.UsuarioValidator;

/**
 * @author Lucas.
 * 
 *         Classe que representa o controller de Categoria com seus respectivos m�todos.
 * 
 */

@Controller
@RequestMapping("usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private AvatarService avatarService;

	/**
	 * M�todo para inicializar a p�gina realizando a convers�o do Perfil de Enum para objeto/classe Perfil + ir� chamar o Validator criado.
	 * 
	 * * @param binder o WebDataBinder binder conector para ligar o param aos m�todos informados.
	 * 	
	 */
	//Ir� inicializar realizando a convers�o do Perfil de Enum para objeto/classe Perfil
	@InitBinder("usuario") //precisa informar qual ModelAttribute(usuario) ser� validado pelo m�todo inicializador
	public void initBinder(WebDataBinder binder) {

		binder.registerCustomEditor(Perfil.class, new PerfilEditorSupport()); //ir� registrar a classe Perfil e inicializar com o nome da convers�o criada(PerfilEditorSupport)
		binder.setValidator(new UsuarioValidator()); //adiciona o validator do spring criado em outra classe no m�todo inicializador
	}	

	/**
	 * M�todo para editar o perfil de um objeto Usu�rio.
	 * 
	 * * @param usuario o @ModelAttribute usuario a ser referenciado na p�gina jsp atrav�s de seu modelAttribute.
	 * 
	 * @return redirecionamento para p�gina jsp usuario/list.
	 */
	@RequestMapping(value = "/update/perfil", method = RequestMethod.POST)
	public String updatePerfil(@ModelAttribute("usuario") Usuario usuario) { //@ModelAttribute � o modelo dos dados que estamos trabalhando na pagina

		usuarioService.updatePerfil(usuario);

		return "redirect:/usuario/list";
	}

	/**
	 * M�todo para pagina��o de usu�rios + ordena��o ordenadas de forma Asc.
	 * 
	 * * @param pagina a @PathVariable pagina a ser buscada por page.
	 * * @param order o @PathVariable order a ser tratado por page.
	 * * @param field o @PathVariable field a ser buscado por page.
	 * 
	 * @return view(acesso a p�gina list.jsp do diret�rio usuario).
	 */
	@RequestMapping(value = "/sort/{order}/{field}/page/{page}", method = RequestMethod.GET)
	public ModelAndView pageUsuario(@PathVariable("page") Integer pagina, //ModelAndView � utilizado para o m�todo GET
			@PathVariable("order") String order, 
			@PathVariable("field") String field) {

		ModelAndView view = new ModelAndView("usuario/list");

		Page<Usuario> page = usuarioService.findByPaginationOrderByField(pagina - 1, 10, field, order);

		view.addObject("page", page);
		view.addObject("urlPagination", "/usuario/sort/" + order + "/" + field + "/page"); //urls din�micas concatenadas respectivas as @RequestMapping acima

		return view;		
	}

	/**
	 * M�todo para pagina��o de usu�rios + ordena��o ordenadas de forma Asc.
	 * 
	 * * @param pagina a @PathVariable pagina a ser buscada por page.
	 * 
	 * @return view(acesso a p�gina list.jsp do diret�rio usuario).
	 */
	@RequestMapping(value = "/page/{page}", method = RequestMethod.GET) //{page} � a p�gina que iremos buscar
	public ModelAndView pageUsuarios(@PathVariable("page") Integer pagina) { 

		ModelAndView view = new ModelAndView("usuario/list"); //ir� acessar a p�gina de list.jsp, a lista de usuarios se encontra nesta lista para pagina��o

		Page<Usuario> page = usuarioService.findByPagination(pagina - 1, 10); 
		//cria um objeto onde iremos armazenar o retorno da consulta 
		//pagina � a variavel informada na url, necessita trabalhar com -1 pois a p�gina come�a com 0 por�m o usuario visualiza como p�gina / 5 � o tamanho de itens por p�gina
		//objeto Page/Slice cont�m v�rias op��es uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(cont�m o conteudo da lista)
		//devido a todas essas op��es absorvidas do objeto Page n�o � necess�rio incluir o List de categorias pois o Page j� est� fazendo este trabalho

		view.addObject("page", page); 
		view.addObject("urlPagination", "/usuario/page");

		return view;
	}

	/**
	 * M�todo para excluir um objeto Usu�rio.
	 * 
	 * * @param id o @PathVariable id a ser buscado para realizar exclus�o.
	 * 
	 * @return redirecionamento para p�gina jsp usuario/add.
	 */
	//m�todo para deletar um usuario atraves do id
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") Long id) {

		usuarioService.delete(id);

		return "redirect:/usuario/add";
	}

	/**
	 * M�todo para editar a senha de um objeto Usu�rio.
	 * 
	 * * @param id o @PathVariable Optional<Long> id a ser buscado.
	 * * @param usuario o @ModelAttribute usuario a ser referenciado na p�gina jsp atrav�s de seu modelAttribute.
	 * * @param result o resultado da valida��o do usuario.
	 * 
	 * @return redirecionamento para p�gina jsp usuario/perfil + id capturado do usuario.
	 */
	@RequestMapping(value = {"/update/senha/{id}", "/update/senha"}, method = {RequestMethod.GET, RequestMethod.POST})
	//utilizando um Requestmapping de arrays com 2 requests/url
	public ModelAndView updateSenha(@PathVariable("id") Optional<Long> id, 
			@ModelAttribute("usuario") @Validated Usuario usuario, BindingResult result) {

		ModelAndView view = new ModelAndView();		

		if(id.isPresent()) { //se id for diferente de nulo quer dizer que j� possui um id e o mesmo pode ser alterado
			usuario = usuarioService.findById(id.get()); //necessita encontrar um usuario pelo id e atribui-lo ao usuario
			view.addObject("usuario", usuario); //add no view a propriedade (usuario) que quer enviar para p�gina jsp
			view.setViewName("usuario/atualizar"); //onde ir� enviar o objeto usu�rio - qual p�gina jsp ser� vinculada ao acessar
			return view;

			//fun��o acima � parte da consulta que recebemos para enviar os dados para respectiva p�gina
		}

		if(result.hasFieldErrors("senha")) {
			usuario = usuarioService.findById(usuario.getId());
			view.addObject("nome", usuario.getNome());
			view.addObject("email", usuario.getEmail());
			view.setViewName("usuario/atualizar");
			return view;
		}

		usuarioService.updateSenha(usuario); //realiza o update junto ao bd

		view.setViewName("redirect:/usuario/perfil/" + usuario.getId()); //ser� redirecionado para o perfil referente ao id capturado ap�s realizar a altera��o

		return view; 		
	}	

	/**
	 * M�todo para editar um objeto Usu�rio.
	 * 
	 * * @param id o @PathVariable Optional<Long> id a ser buscado.
	 * * @param usuario o @ModelAttribute usuario a ser referenciado na p�gina jsp atrav�s de seu modelAttribute.
	 * * @param result o resultado da valida��o do usuario.
	 * 
	 * @return redirecionamento para p�gina jsp usuario/perfil + id capturado do usuario.
	 */
	@RequestMapping(value = {"/update/{id}", "/update"}, method = {RequestMethod.GET, RequestMethod.POST}) 
	//utilizando um Requestmapping de arrays com 2 requests/url 
	// - 1� para capturar o id junto a tela de update / vai receber a requisi��o para selecionar o usu�rio que ter� os dados alterados
	// - 2� para realizar a altera��o / vai receber o submit com os dados j� alterados
	//Optional pode ser utilizado para trabalhar com opera��es de diferentes URLs.
	public ModelAndView update(@PathVariable("id") Optional<Long> id, 
			@ModelAttribute("usuario") @Validated Usuario usuario, BindingResult result) {
		//utiliza-se o Optional para enganar o PathVariable id para que o sistema enxergue que j� exista um valor vindo da url (caso n�o utilize Optional neste caso causar� uma exception n�o deixando salvar o update realizado)
		//utilizando Optional n�o causar� exception ou null

		ModelAndView view = new ModelAndView();

		//esta fun��o refere-se ao 1� RequestMapping informado("/update/{id}")
		//utilizando Optional � necess�rio utilizar o m�todo .isPresent, se for true significa que possui id, se for false significa que n�o possui id
		//neste caso o Optional vai enxergar esse if como false n�o o utilizando.
		if(id.isPresent()) { //se id for diferente de nulo quer dizer que j� possui um id e o mesmo pode ser alterado
			usuario = usuarioService.findById(id.get()); //necessita encontrar um usuario pelo id e atribui-lo ao usuario
			view.addObject("usuario", usuario); //add no view a propriedade (usuario) que quer enviar para p�gina jsp
			view.setViewName("usuario/atualizar"); //atribuir o nome da vari�vel para p�gina jsp (atualizar.jsp)			
			return view; //ir� enviar para o formul�rio que se encontra na p�gina jsp(atualizar), ser� alterado os dados Nome e Email informados abaixo e ao clicar no bot�o submit ser� direcionado a tela do update
			//fun��o acima � parte da consulta que recebemos para enviar os dados para respectiva p�gina
		}

		if(result.hasErrors()) {
			view.setViewName("usuario/atualizar");
			return view;
		}

		//ser� utilizado este update pois o Optional enxerga o if acima como false
		usuarioService.updateNomeAndEmail(usuario); //ir� atualizar nome e e-mail

		view.setViewName("redirect:/usuario/perfil/" + usuario.getId()); //redireciona para a p�gina jsp perfil e captura o usu�rio informado no requestMapping

		return view;
	}

	/**
	 * M�todo para listar todos os usu�rios cadastrados.
	 * 
	 * * @param model o modelmap a add atributos e serem referenciados na p�gina jsp.
	 * 
	 * @return view(acesso a p�gina list.jsp do diret�rio usuario + @param model com seus atributos a serem referenciado na p�gina jsp).
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listUsuarios(ModelMap model) {

		//List<Usuario> usuarios = usuarioService.findAll(); //encontra todos os usu�rios cadastrados		
		//model.addAttribute("usuarios", usuarios); // atribui a lista de "usu�rios" na p�gina list.jsp 

		Page<Usuario> page = usuarioService.findByPagination(0, 10); 
		//cria um objeto onde iremos armazenar o retorno da consulta / 0 � a variavel informada na url por�m neste caso sempre ser� aberto a primeira p�gina do formul�rio(0 igual a Array) / 5 � o tamanho de itens por p�gina
		//objeto Page/Slice cont�m v�rias op��es uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(cont�m a sua lista)
		//devido a todas essas op��es absorvidas do objeto Page n�o � necess�rio incluir o List de categorias pois o Page j� est� fazendo este trabalho

		model.addAttribute("page", page); //n�o pode ser encontrado diretamente todas as categorias, necessita utilizar o Page acima pois sen�o ele n�o encontra as pagina��es ao abrir o formul�rio
		model.addAttribute("urlPagination", "/usuario/page");

		return new ModelAndView("usuario/list", model); //diret�rio usu�rio / p�gina list - tela que ser� visualizada
	}

	/**
	 * M�todo para acessar a p�gina perfil.jsp.
	 * 
	 * * @param id o @PathVariable id a ser buscado.
	 * 
	 * @return view(acesso a p�gina perfil.jsp do diret�rio usuario).
	 */
	//metodo que ir� acessar a p�gina perfil.jsp
	@RequestMapping(value = "/perfil/{id}", method = RequestMethod.GET)
	public ModelAndView perfil(@PathVariable("id") Long id) {

		ModelAndView view = new ModelAndView("usuario/perfil"); //tela que ser� visualizada

		Usuario usuario = usuarioService.findById(id);

		view.addObject("usuario", usuario); //visualiza pelo id informado as informa��es do perfil		

		return view;
	}

	/**
	 * M�todo para salvar um objeto Usu�rio junto ao Avatar.
	 * 
	 * * @param usuario o @ModelAttribute usuario a ser salvo a referenciando na p�gina jsp atrav�s de seu modelAttribute.
	 * * @param result o resultado da valida��o do usuario.
	 * 
	 * @return redirecionamento para p�gina jsp auth/form).
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("usuario") @Validated Usuario usuario, BindingResult result) { //Par�metros para fazer upload da foto do avatar

		if( result.hasErrors() ) {
			return "usuario/cadastro";
		}

		Avatar avatar = avatarService.getAvatarByUpload(usuario.getFile()); //receber atrav�s de upload o file

		usuario.setAvatar(avatar); //setando avatar dentro do usuario

		usuarioService.save(usuario); //ir� salvar usu�rio e foto do avatar juntos
		//devido a isso o seu Transactional no UsuarioService foi criado para suportar as 2 transa��es juntas (se somente 1 der certo, toda a transa��o � desfeita)

		return "redirect:/auth/form"; //redireciona para a pagina de login ao salvar um usuario
		//ap�s salvo � redirecionado para usuario(UsuarioController) e com um m�todo dentro deste Controller(perfil) 
		// Redireciona para a p�gina de perfil junto ao id informado no Requestmapping
	}

	/**
	 * M�todo para criar uma p�gina b�sica com o formul�rio de cadastro de um objeto Usu�rio.
	 * 
	 * * @param usuario o @ModelAttribute usuario a ser capturado na tela atrav�s de sua referencia do modelAttribute.
	 * 
	 * @return view(acesso a p�gina cadastro.jsp do diret�rio usuario).
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView showForm(@ModelAttribute("usuario") Usuario usuario) { //d� acesso ao formul�rio para cadastro
		//@ModelAttribute(usuario) ser� a variavel que ir� representar o seu formulario, cria uma instancia do objeto usuario no formulario
		//� criado para receber e enviar para as p�ginas .JSP, objetos do tipo Usuario.
		return new ModelAndView("usuario/cadastro"); //diret�rio usu�rio / p�gina cadastro - tela que ser� visualizada
	}	
}