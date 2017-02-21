package br.com.lucas.blog.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.lucas.blog.entity.Categoria;
import br.com.lucas.blog.service.CategoriaService;

/**
 * @author Lucas.
 * 
 *         Classe que representa o controller de Categoria com seus respectivos métodos.
 * 
 */

@Controller
@RequestMapping("categoria")
public class CategoriaController {

	@Autowired
	private CategoriaService categoriaService;
	
	/**
	 * Método para paginação da lista da entidade Categoria ordenadas de forma Asc.
	 * 
	 * * @param pagina a @PathVariable pagina a ser buscada por page.
	 * * @param categoria a @ModelAttribute categoria a ser referenciado na página jsp através de seu modelAttribute.
	 * 
	 * @return view(acesso a página cadastro.jsp do diretório categoria).
	 */
	@RequestMapping(value = "/page/{page}", method = RequestMethod.GET) //{page} é a página que iremos buscar
	public ModelAndView pageCategorias(@PathVariable("page") Integer pagina, 
									   @ModelAttribute("categoria") Categoria categoria) {
		
		ModelAndView view = new ModelAndView("categoria/cadastro"); //irá acessar a página de cadastro.jsp, a lista de categorias se encontra nesta lista para paginação
	
		Page<Categoria> page = categoriaService.findByPagination(pagina - 1, 5); 
		//cria um objeto onde iremos armazenar o retorno da consulta 
		//pagina é a variavel informada na url, necessita trabalhar com -1 pois a página começa com 0 porém o usuario visualiza como página / 5 é o tamanho de itens por página
		//objeto Page/Slice contém várias opções uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(contém a sua lista)
		//devido a todas essas opções absorvidas do objeto Page não é necessário incluir o List de categorias pois o Page já está fazendo este trabalho
		
		view.addObject("page", page); // adiciona na view este objeto page criado acima/ "page" é o nome do objeto que vamos enviar para página / page contém o resultado da nossa consulta(valor) 
		view.addObject("urlPagination", "/categoria/page");
		
		return view;
	}
	
	/**
	 * Método para editar um objeto Categoria.
	 * 
	 * * @param id a id a ser buscado para realizar update.
	 * * @param model o modelmap a add atributos e serem referenciados na página jsp.
	 * 
	 * @return view(acesso a página cadastro.jsp do diretório categoria + @param model com seus atributos a serem referenciado na página jsp).
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public ModelAndView preUpdate(@PathVariable("id") Long id, ModelMap model) {
		
		Categoria categoria = categoriaService.findById(id); //encontra o id e o atribui em categoria para que possa ter esse dado no formulario e utilizá-lo
		
		Page<Categoria> page = categoriaService.findByPagination(0, 5); 
		//cria um objeto onde iremos armazenar o retorno da consulta / 0 é a variavel informada na url porém neste caso sempre será aberto a primeira página do formulário(0 igual a Array) / 5 é o tamanho de itens por página
		//objeto Page/Slice contém várias opções uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(contém a sua lista)
		//devido a todas essas opções absorvidas do objeto Page não é necessário incluir o List de categorias pois o Page já está fazendo este trabalho
		
		model.addAttribute("categoria", categoria); //página que será acessada/visualizada / retorna para página o valor que será localizado a partir da consulta que faremos por id
		//model.addAttribute("categorias", categoriaService.findAll()); //página que será acessada/visualizada 
		//retorna para página o valor que será localizado a partir da consulta que faremos pelo método findAll retornando toda lista
			
		model.addAttribute("page", page); //não pode ser encontrado diretamente todas as categorias, necessita utilizar o Page acima pois senão ele não encontra as paginações ao abrir o formulário
		model.addAttribute("urlPagination", "/categoria/page");
		
		return new ModelAndView("categoria/cadastro", model); //diretório categoria / página cadastro.jsp / model receberá o objeto que está sendo retornado para ser alterado 		
	}
	
	/**
	 * Método para excluir um objeto Categoria.
	 * 
	 * * @param id o @PathVariable id a ser buscado para realizar exclusão.
	 * 
	 * @return redirecionamento para página jsp categoria/add.
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") Long id) {
		
		categoriaService.delete(id);
		
		return "redirect:/categoria/add"; //deleta e volta para mesma página de cadastro que será onde está alocada a list também
	}
	
	/**
	 * Método para salvar um objeto Categoria e retornar a mesma página de cadastro pois será nessa mesma página que se encontra a listagem de categorias.
	 * 
	 * * @param categoria a @ModelAttribute categoria a ser salva a referenciando na página jsp através de seu modelAttribute.
	 * * @param result o resultado da validação da categoria.
	 * 
	 * @return view(redirecionamento para página jsp categoria/add).
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("categoria") @Validated Categoria categoria, 
							 BindingResult result) {
		
		ModelAndView view = new ModelAndView(); //quando necessita enviar dados para página trabalha-se com ModelAndView
		
		//se contiver erros ele volta para mesma página de cadastro(esta página contém cadastro e lista juntos) e mostra a lista antiga não realizando o save
		if( result.hasErrors()) {
			
			Page<Categoria> page = categoriaService.findByPagination(0, 5);
			
			view.addObject("page", page); //enviar objeto para página através do ModelAndView
			view.addObject("urlPagination", "/categoria/page"); //enviar objeto para página através do ModelAndView
			view.setViewName("categoria/cadastro"); //se contiver erros acessa a mesma página de cadastro novamente
			
			return view;
		}
		
		categoriaService.saveOrUpdate(categoria); //salva categoria se estiver validado
		
		view.setViewName("redirect:/categoria/add"); //salva e volta para mesma página de cadastro que será onde está alocada a list de categorias também
		
		return view; 
	}
	
	/**
	 * Método para criar uma página básica com o formulário de cadastro de um objeto Categoria e será incluido também a listagem de categorias na mesma página .jsp.
	 * 
	 * * @param categoria a @ModelAttribute categoria a ser capturado na tela através de sua referencia do modelAttribute.
	 * 
	 * @return view(acesso a página cadastro.jsp do diretório categoria).
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView cadastro(@ModelAttribute("categoria") Categoria categoria) {
		 
		ModelAndView view = new ModelAndView("categoria/cadastro"); //diretório categoria / página cadastro.jsp
		
		Page<Categoria> page = categoriaService.findByPagination(0, 5); 
		//cria um objeto onde iremos armazenar o retorno da consulta / 0 é a variavel informada na url porém neste caso sempre será aberto a primeira página do formulário(0 igual a Array) / 5 é o tamanho de itens por página
		//objeto Page/Slice contém várias opções uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(contém a sua lista)
		//devido a todas essas opções absorvidas do objeto Page não é necessário incluir o List de categorias pois o Page já está fazendo este trabalho
				
		//view.addObject("categorias", categoriaService.findAll());
		view.addObject("page", page); //não pode ser encontrado diretamente todas as categorias, necessita utilizar o Page acima pois senão ele não encontra as paginações ao abrir o formulário
		view.addObject("urlPagination", "/categoria/page");
				
		return view;		
	}
}