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
 *         Classe que representa o controller de Categoria com seus respectivos métodos.
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
	 * Método para inicializar a página realizando a conversão do Perfil de Enum para objeto/classe Perfil + irá chamar o Validator criado.
	 * 
	 * * @param binder o WebDataBinder binder conector para ligar o param aos métodos informados.
	 * 	
	 */
	//Irá inicializar realizando a conversão do Perfil de Enum para objeto/classe Perfil
	@InitBinder("usuario") //precisa informar qual ModelAttribute(usuario) será validado pelo método inicializador
	public void initBinder(WebDataBinder binder) {

		binder.registerCustomEditor(Perfil.class, new PerfilEditorSupport()); //irá registrar a classe Perfil e inicializar com o nome da conversão criada(PerfilEditorSupport)
		binder.setValidator(new UsuarioValidator()); //adiciona o validator do spring criado em outra classe no método inicializador
	}	

	/**
	 * Método para editar o perfil de um objeto Usuário.
	 * 
	 * * @param usuario o @ModelAttribute usuario a ser referenciado na página jsp através de seu modelAttribute.
	 * 
	 * @return redirecionamento para página jsp usuario/list.
	 */
	@RequestMapping(value = "/update/perfil", method = RequestMethod.POST)
	public String updatePerfil(@ModelAttribute("usuario") Usuario usuario) { //@ModelAttribute é o modelo dos dados que estamos trabalhando na pagina

		usuarioService.updatePerfil(usuario);

		return "redirect:/usuario/list";
	}

	/**
	 * Método para paginação de usuários + ordenação ordenadas de forma Asc.
	 * 
	 * * @param pagina a @PathVariable pagina a ser buscada por page.
	 * * @param order o @PathVariable order a ser tratado por page.
	 * * @param field o @PathVariable field a ser buscado por page.
	 * 
	 * @return view(acesso a página list.jsp do diretório usuario).
	 */
	@RequestMapping(value = "/sort/{order}/{field}/page/{page}", method = RequestMethod.GET)
	public ModelAndView pageUsuario(@PathVariable("page") Integer pagina, //ModelAndView é utilizado para o método GET
			@PathVariable("order") String order, 
			@PathVariable("field") String field) {

		ModelAndView view = new ModelAndView("usuario/list");

		Page<Usuario> page = usuarioService.findByPaginationOrderByField(pagina - 1, 10, field, order);

		view.addObject("page", page);
		view.addObject("urlPagination", "/usuario/sort/" + order + "/" + field + "/page"); //urls dinâmicas concatenadas respectivas as @RequestMapping acima

		return view;		
	}

	/**
	 * Método para paginação de usuários + ordenação ordenadas de forma Asc.
	 * 
	 * * @param pagina a @PathVariable pagina a ser buscada por page.
	 * 
	 * @return view(acesso a página list.jsp do diretório usuario).
	 */
	@RequestMapping(value = "/page/{page}", method = RequestMethod.GET) //{page} é a página que iremos buscar
	public ModelAndView pageUsuarios(@PathVariable("page") Integer pagina) { 

		ModelAndView view = new ModelAndView("usuario/list"); //irá acessar a página de list.jsp, a lista de usuarios se encontra nesta lista para paginação

		Page<Usuario> page = usuarioService.findByPagination(pagina - 1, 10); 
		//cria um objeto onde iremos armazenar o retorno da consulta 
		//pagina é a variavel informada na url, necessita trabalhar com -1 pois a página começa com 0 porém o usuario visualiza como página / 5 é o tamanho de itens por página
		//objeto Page/Slice contém várias opções uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(contém o conteudo da lista)
		//devido a todas essas opções absorvidas do objeto Page não é necessário incluir o List de categorias pois o Page já está fazendo este trabalho

		view.addObject("page", page); 
		view.addObject("urlPagination", "/usuario/page");

		return view;
	}

	/**
	 * Método para excluir um objeto Usuário.
	 * 
	 * * @param id o @PathVariable id a ser buscado para realizar exclusão.
	 * 
	 * @return redirecionamento para página jsp usuario/add.
	 */
	//método para deletar um usuario atraves do id
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") Long id) {

		usuarioService.delete(id);

		return "redirect:/usuario/add";
	}

	/**
	 * Método para editar a senha de um objeto Usuário.
	 * 
	 * * @param id o @PathVariable Optional<Long> id a ser buscado.
	 * * @param usuario o @ModelAttribute usuario a ser referenciado na página jsp através de seu modelAttribute.
	 * * @param result o resultado da validação do usuario.
	 * 
	 * @return redirecionamento para página jsp usuario/perfil + id capturado do usuario.
	 */
	@RequestMapping(value = {"/update/senha/{id}", "/update/senha"}, method = {RequestMethod.GET, RequestMethod.POST})
	//utilizando um Requestmapping de arrays com 2 requests/url
	public ModelAndView updateSenha(@PathVariable("id") Optional<Long> id, 
			@ModelAttribute("usuario") @Validated Usuario usuario, BindingResult result) {

		ModelAndView view = new ModelAndView();		

		if(id.isPresent()) { //se id for diferente de nulo quer dizer que já possui um id e o mesmo pode ser alterado
			usuario = usuarioService.findById(id.get()); //necessita encontrar um usuario pelo id e atribui-lo ao usuario
			view.addObject("usuario", usuario); //add no view a propriedade (usuario) que quer enviar para página jsp
			view.setViewName("usuario/atualizar"); //onde irá enviar o objeto usuário - qual página jsp será vinculada ao acessar
			return view;

			//função acima é parte da consulta que recebemos para enviar os dados para respectiva página
		}

		if(result.hasFieldErrors("senha")) {
			usuario = usuarioService.findById(usuario.getId());
			view.addObject("nome", usuario.getNome());
			view.addObject("email", usuario.getEmail());
			view.setViewName("usuario/atualizar");
			return view;
		}

		usuarioService.updateSenha(usuario); //realiza o update junto ao bd

		view.setViewName("redirect:/usuario/perfil/" + usuario.getId()); //será redirecionado para o perfil referente ao id capturado após realizar a alteração

		return view; 		
	}	

	/**
	 * Método para editar um objeto Usuário.
	 * 
	 * * @param id o @PathVariable Optional<Long> id a ser buscado.
	 * * @param usuario o @ModelAttribute usuario a ser referenciado na página jsp através de seu modelAttribute.
	 * * @param result o resultado da validação do usuario.
	 * 
	 * @return redirecionamento para página jsp usuario/perfil + id capturado do usuario.
	 */
	@RequestMapping(value = {"/update/{id}", "/update"}, method = {RequestMethod.GET, RequestMethod.POST}) 
	//utilizando um Requestmapping de arrays com 2 requests/url 
	// - 1º para capturar o id junto a tela de update / vai receber a requisição para selecionar o usuário que terá os dados alterados
	// - 2º para realizar a alteração / vai receber o submit com os dados já alterados
	//Optional pode ser utilizado para trabalhar com operações de diferentes URLs.
	public ModelAndView update(@PathVariable("id") Optional<Long> id, 
			@ModelAttribute("usuario") @Validated Usuario usuario, BindingResult result) {
		//utiliza-se o Optional para enganar o PathVariable id para que o sistema enxergue que já exista um valor vindo da url (caso não utilize Optional neste caso causará uma exception não deixando salvar o update realizado)
		//utilizando Optional não causará exception ou null

		ModelAndView view = new ModelAndView();

		//esta função refere-se ao 1º RequestMapping informado("/update/{id}")
		//utilizando Optional é necessário utilizar o método .isPresent, se for true significa que possui id, se for false significa que não possui id
		//neste caso o Optional vai enxergar esse if como false não o utilizando.
		if(id.isPresent()) { //se id for diferente de nulo quer dizer que já possui um id e o mesmo pode ser alterado
			usuario = usuarioService.findById(id.get()); //necessita encontrar um usuario pelo id e atribui-lo ao usuario
			view.addObject("usuario", usuario); //add no view a propriedade (usuario) que quer enviar para página jsp
			view.setViewName("usuario/atualizar"); //atribuir o nome da variável para página jsp (atualizar.jsp)			
			return view; //irá enviar para o formulário que se encontra na página jsp(atualizar), será alterado os dados Nome e Email informados abaixo e ao clicar no botão submit será direcionado a tela do update
			//função acima é parte da consulta que recebemos para enviar os dados para respectiva página
		}

		if(result.hasErrors()) {
			view.setViewName("usuario/atualizar");
			return view;
		}

		//será utilizado este update pois o Optional enxerga o if acima como false
		usuarioService.updateNomeAndEmail(usuario); //irá atualizar nome e e-mail

		view.setViewName("redirect:/usuario/perfil/" + usuario.getId()); //redireciona para a página jsp perfil e captura o usuário informado no requestMapping

		return view;
	}

	/**
	 * Método para listar todos os usuários cadastrados.
	 * 
	 * * @param model o modelmap a add atributos e serem referenciados na página jsp.
	 * 
	 * @return view(acesso a página list.jsp do diretório usuario + @param model com seus atributos a serem referenciado na página jsp).
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listUsuarios(ModelMap model) {

		//List<Usuario> usuarios = usuarioService.findAll(); //encontra todos os usuários cadastrados		
		//model.addAttribute("usuarios", usuarios); // atribui a lista de "usuários" na página list.jsp 

		Page<Usuario> page = usuarioService.findByPagination(0, 10); 
		//cria um objeto onde iremos armazenar o retorno da consulta / 0 é a variavel informada na url porém neste caso sempre será aberto a primeira página do formulário(0 igual a Array) / 5 é o tamanho de itens por página
		//objeto Page/Slice contém várias opções uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(contém a sua lista)
		//devido a todas essas opções absorvidas do objeto Page não é necessário incluir o List de categorias pois o Page já está fazendo este trabalho

		model.addAttribute("page", page); //não pode ser encontrado diretamente todas as categorias, necessita utilizar o Page acima pois senão ele não encontra as paginações ao abrir o formulário
		model.addAttribute("urlPagination", "/usuario/page");

		return new ModelAndView("usuario/list", model); //diretório usuário / página list - tela que será visualizada
	}

	/**
	 * Método para acessar a página perfil.jsp.
	 * 
	 * * @param id o @PathVariable id a ser buscado.
	 * 
	 * @return view(acesso a página perfil.jsp do diretório usuario).
	 */
	//metodo que irá acessar a página perfil.jsp
	@RequestMapping(value = "/perfil/{id}", method = RequestMethod.GET)
	public ModelAndView perfil(@PathVariable("id") Long id) {

		ModelAndView view = new ModelAndView("usuario/perfil"); //tela que será visualizada

		Usuario usuario = usuarioService.findById(id);

		view.addObject("usuario", usuario); //visualiza pelo id informado as informações do perfil		

		return view;
	}

	/**
	 * Método para salvar um objeto Usuário junto ao Avatar.
	 * 
	 * * @param usuario o @ModelAttribute usuario a ser salvo a referenciando na página jsp através de seu modelAttribute.
	 * * @param result o resultado da validação do usuario.
	 * 
	 * @return redirecionamento para página jsp auth/form).
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("usuario") @Validated Usuario usuario, BindingResult result) { //Parâmetros para fazer upload da foto do avatar

		if( result.hasErrors() ) {
			return "usuario/cadastro";
		}

		Avatar avatar = avatarService.getAvatarByUpload(usuario.getFile()); //receber através de upload o file

		usuario.setAvatar(avatar); //setando avatar dentro do usuario

		usuarioService.save(usuario); //irá salvar usuário e foto do avatar juntos
		//devido a isso o seu Transactional no UsuarioService foi criado para suportar as 2 transações juntas (se somente 1 der certo, toda a transação é desfeita)

		return "redirect:/auth/form"; //redireciona para a pagina de login ao salvar um usuario
		//após salvo é redirecionado para usuario(UsuarioController) e com um método dentro deste Controller(perfil) 
		// Redireciona para a página de perfil junto ao id informado no Requestmapping
	}

	/**
	 * Método para criar uma página básica com o formulário de cadastro de um objeto Usuário.
	 * 
	 * * @param usuario o @ModelAttribute usuario a ser capturado na tela através de sua referencia do modelAttribute.
	 * 
	 * @return view(acesso a página cadastro.jsp do diretório usuario).
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView showForm(@ModelAttribute("usuario") Usuario usuario) { //dá acesso ao formulário para cadastro
		//@ModelAttribute(usuario) será a variavel que irá representar o seu formulario, cria uma instancia do objeto usuario no formulario
		//é criado para receber e enviar para as páginas .JSP, objetos do tipo Usuario.
		return new ModelAndView("usuario/cadastro"); //diretório usuário / página cadastro - tela que será visualizada
	}	
}