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
 *         Classe que representa o controller de Autor com seus respectivos métodos.
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
	 * Método para paginação da lista da entidade Autor ordenadas de forma Asc.
	 * 
	 * * @param pagina a @PathVariable pagina a ser buscada por page.
	 * 
	 * @return view(acesso a página perfil.jsp do diretório autor).
	 */
	@RequestMapping(value = "page/{page}", method = RequestMethod.GET) //{page} é a página que iremos buscar
	public ModelAndView pageAutores(@PathVariable("page") Integer pagina) {

		ModelAndView view = new ModelAndView("autor/perfil"); ////ModelAndView é utilizado para o método GET/ irá acessar a página de list.jsp, a lista de usuarios se encontra nesta lista para paginação

		Page<Autor> page = autorService.findByPagination(pagina - 1, 10); //cria um objeto onde iremos armazenar o retorno da consulta 
		//pagina é a variavel informada na url, necessita trabalhar com -1 pois a página começa com 0 porém o usuario visualiza como página / 5 é o tamanho de itens por página
		//objeto Page/Slice contém várias opções uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(contém o conteudo da lista)
		//devido a todas essas opções absorvidas do objeto Page não é necessário incluir o List de categorias pois o Page já está fazendo este trabalho

		view.addObject("page", page); // adiciona na view este objeto page criado acima/ "page" é o nome do objeto que vamos enviar para página / page contém o resultado da nossa consulta(valor) 
		view.addObject("urlPagination", "/autor/page");

		return view;
	}

	/**
	 * Método para deletar um objeto Autor.
	 * 
	 * * @param id a id a ser buscado para realizar exclusão.
	 * 
	 * @return redirecionamento para página jsp autor/add.
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") Long id) {

		autorService.delete(id);

		return "redirect:/autor/add";
	}

	/**
	 * Método para editar um objeto Autor.
	 * 
	 * * @param id a id a ser buscado para realizar update.
	 * 
	 * @return view(acesso a página perfil.jsp do diretório autor).
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public ModelAndView preUpdate(@PathVariable("id") Long id) {

		ModelAndView view = new ModelAndView("autor/cadastro");

		Autor autor = autorService.findById(id);

		view.addObject("autor", autor);

		return view;
	}

	/**
	 * Método para capturar um objeto Autor cadastrado.
	 * 
	 * * @param id o Optional<Long> id a ser buscado para capturar um objeto Autor.
	 * 
	 * @return view(acesso a página perfil.jsp do diretório autor).
	 */
	//este método getAutor abaixo poderia ser criado separadamente as 2 RequestMapping normalmente, 
	//porém neste exemplo foram criadas juntas baseadas em 2 urls diferentes para uma análise diferente,
	//também foi utilizada apenas uma página jsp (perfil.jsp) para tratar as 2 urls juntas apenas adicionando forEach para a list

	//metodo que irá acessar a página perfil.jsp / exibe os dados do autor cadastrado
	//será criado no mesmo método um array de urls.
	// - 1º RequestMapping será utilizado para encontrar o perfil através de somente 1 id
	// - 2º RequestMapping será utilizado para encontrar todos os autores cadastrados(list)
	@RequestMapping(value = {"/perfil/{id}", "/list"}, method = RequestMethod.GET)
	public ModelAndView getAutor(@PathVariable("id") Optional<Long> id) {

		ModelAndView view = new ModelAndView("autor/perfil"); //a mesma view/página será mostrado ou somente 1 id capturado no 1ºrequest ou a lista capturado no 2º request 

		//Se encontrar/existir 1 id irá trabalhar com 1º RequestMapping / está buscando pelo (/perfil/{id})
		//true or false (está presente ou não - (isPresent))
		if(id.isPresent()) {
			Autor autor = autorService.findById(id.get()); //consulta pelo id para recuperar o autor 

			view.addObject("autores", Arrays.asList(autor)); //se for encontrado somente 1 autor será mostrado somente 1 na página jsp. se tiver mais de 1 autor cairá na list abaixo	

			//Se pular o if, caira no else e irá trazer toda a lista capturada de autores
		} else {
			//List<Autor> autores = autorService.findAll(); //define na lista autores o conteudo de todos autores cadastrados
			//view.addObject("autores", autores); // autores - contéudo da lista que será atribuido na página jsp conforme foi capturado na List acima

			Page<Autor> page = autorService.findByPagination(0, 10); 
			//cria um objeto onde iremos armazenar o retorno da consulta / 0 é a variavel informada na url porém neste caso sempre será aberto a primeira página do formulário(0 igual a um Array) / 10 é o tamanho de itens por página
			//objeto Page/Slice contém várias opções uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(contém a sua lista)
			//devido a todas essas opções absorvidas do objeto Page não é necessário incluir o List de categorias pois o Page já está fazendo este trabalho

			view.addObject("page", page); //não pode ser encontrado diretamente todas as categorias, necessita utilizar o Page acima pois senão ele não encontra as paginações ao abrir o formulário
			view.addObject("urlPagination", "/autor/page");
		}

		return view;	
	}

	/**
	 * Método para salvar um objeto Autor.
	 * 
	 * * @param autor o @ModelAttribute autor a ser salvo o referenciando na página jsp através de seu modelAttribute.
	 * * @param result o resultado da validação do autor.
	 * 
	 * @return redirecionamento para página jsp autor/perfil + id capturado do autor informado.
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST) //utiliza a url /save para encontrar este método + método Post (para inserção)
	public String save(@ModelAttribute("autor") @Validated Autor autor, BindingResult result,
			@AuthenticationPrincipal UsuarioLogado logado) { //utiliza a variável autor nas páginas jsp
		//BindingResult irá testar se existe erro ou não através do validator

		if( result.hasErrors() ) { //se o bindingresult contiver erros ele volta para página de cadastro sem validar e salvar
			return "autor/cadastro";
		}

		//		if( logado.getId() != null ) {
		//			Usuario usuario = usuarioService.findById(logado.getId()); //realiza a consulta para inserir no objeto autor o objeto usuario
		//			autor.setUsuario(usuario); //atribui ao autor o id do usuario
		//		}

		autorService.save(autor); //salva autor

		return "redirect:/autor/perfil/" + autor.getId(); //ao salvar um autor gera automático o id e redireciona para a página perfil.jsp
	}

	/**
	 * Método para criar uma página básica com o formulário de cadastro de um objeto Autor.
	 * 
	 * * @param autor o @ModelAttribute autor a ser capturado na tela o referenciando na página jsp através de seu modelAttribute.
	 * 
	 * @return view(acesso a página cadastro.jsp do diretório autor).
	 */
	//método para redirecionar para a página cadastro.jsp / abre o cadastro de autor
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView addAutor(@ModelAttribute("autor") Autor autor, 
			@AuthenticationPrincipal() UsuarioLogado logado) {

		//		autor = autorService.findByUsuario(logado.getId()); //verifica se o autor já possui id e o atribui a autor
		//
		//		if(autor == null) {
		//			return new ModelAndView("autor/cadastro"); //se autor for nulo retorna para página de cadastro para o autor realizar seu cadastro
		//		}

		return new ModelAndView("redirect:/autor/cadastro"); //senão retorna para seu perfil com o respectivo id	
	}
}