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
 *         Classe que representa o controller de Categoria com seus respectivos m�todos.
 * 
 */

@Controller
@RequestMapping("categoria")
public class CategoriaController {

	@Autowired
	private CategoriaService categoriaService;
	
	/**
	 * M�todo para pagina��o da lista da entidade Categoria ordenadas de forma Asc.
	 * 
	 * * @param pagina a @PathVariable pagina a ser buscada por page.
	 * * @param categoria a @ModelAttribute categoria a ser referenciado na p�gina jsp atrav�s de seu modelAttribute.
	 * 
	 * @return view(acesso a p�gina cadastro.jsp do diret�rio categoria).
	 */
	@RequestMapping(value = "/page/{page}", method = RequestMethod.GET) //{page} � a p�gina que iremos buscar
	public ModelAndView pageCategorias(@PathVariable("page") Integer pagina, 
									   @ModelAttribute("categoria") Categoria categoria) {
		
		ModelAndView view = new ModelAndView("categoria/cadastro"); //ir� acessar a p�gina de cadastro.jsp, a lista de categorias se encontra nesta lista para pagina��o
	
		Page<Categoria> page = categoriaService.findByPagination(pagina - 1, 5); 
		//cria um objeto onde iremos armazenar o retorno da consulta 
		//pagina � a variavel informada na url, necessita trabalhar com -1 pois a p�gina come�a com 0 por�m o usuario visualiza como p�gina / 5 � o tamanho de itens por p�gina
		//objeto Page/Slice cont�m v�rias op��es uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(cont�m a sua lista)
		//devido a todas essas op��es absorvidas do objeto Page n�o � necess�rio incluir o List de categorias pois o Page j� est� fazendo este trabalho
		
		view.addObject("page", page); // adiciona na view este objeto page criado acima/ "page" � o nome do objeto que vamos enviar para p�gina / page cont�m o resultado da nossa consulta(valor) 
		view.addObject("urlPagination", "/categoria/page");
		
		return view;
	}
	
	/**
	 * M�todo para editar um objeto Categoria.
	 * 
	 * * @param id a id a ser buscado para realizar update.
	 * * @param model o modelmap a add atributos e serem referenciados na p�gina jsp.
	 * 
	 * @return view(acesso a p�gina cadastro.jsp do diret�rio categoria + @param model com seus atributos a serem referenciado na p�gina jsp).
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public ModelAndView preUpdate(@PathVariable("id") Long id, ModelMap model) {
		
		Categoria categoria = categoriaService.findById(id); //encontra o id e o atribui em categoria para que possa ter esse dado no formulario e utiliz�-lo
		
		Page<Categoria> page = categoriaService.findByPagination(0, 5); 
		//cria um objeto onde iremos armazenar o retorno da consulta / 0 � a variavel informada na url por�m neste caso sempre ser� aberto a primeira p�gina do formul�rio(0 igual a Array) / 5 � o tamanho de itens por p�gina
		//objeto Page/Slice cont�m v�rias op��es uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(cont�m a sua lista)
		//devido a todas essas op��es absorvidas do objeto Page n�o � necess�rio incluir o List de categorias pois o Page j� est� fazendo este trabalho
		
		model.addAttribute("categoria", categoria); //p�gina que ser� acessada/visualizada / retorna para p�gina o valor que ser� localizado a partir da consulta que faremos por id
		//model.addAttribute("categorias", categoriaService.findAll()); //p�gina que ser� acessada/visualizada 
		//retorna para p�gina o valor que ser� localizado a partir da consulta que faremos pelo m�todo findAll retornando toda lista
			
		model.addAttribute("page", page); //n�o pode ser encontrado diretamente todas as categorias, necessita utilizar o Page acima pois sen�o ele n�o encontra as pagina��es ao abrir o formul�rio
		model.addAttribute("urlPagination", "/categoria/page");
		
		return new ModelAndView("categoria/cadastro", model); //diret�rio categoria / p�gina cadastro.jsp / model receber� o objeto que est� sendo retornado para ser alterado 		
	}
	
	/**
	 * M�todo para excluir um objeto Categoria.
	 * 
	 * * @param id o @PathVariable id a ser buscado para realizar exclus�o.
	 * 
	 * @return redirecionamento para p�gina jsp categoria/add.
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") Long id) {
		
		categoriaService.delete(id);
		
		return "redirect:/categoria/add"; //deleta e volta para mesma p�gina de cadastro que ser� onde est� alocada a list tamb�m
	}
	
	/**
	 * M�todo para salvar um objeto Categoria e retornar a mesma p�gina de cadastro pois ser� nessa mesma p�gina que se encontra a listagem de categorias.
	 * 
	 * * @param categoria a @ModelAttribute categoria a ser salva a referenciando na p�gina jsp atrav�s de seu modelAttribute.
	 * * @param result o resultado da valida��o da categoria.
	 * 
	 * @return view(redirecionamento para p�gina jsp categoria/add).
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("categoria") @Validated Categoria categoria, 
							 BindingResult result) {
		
		ModelAndView view = new ModelAndView(); //quando necessita enviar dados para p�gina trabalha-se com ModelAndView
		
		//se contiver erros ele volta para mesma p�gina de cadastro(esta p�gina cont�m cadastro e lista juntos) e mostra a lista antiga n�o realizando o save
		if( result.hasErrors()) {
			
			Page<Categoria> page = categoriaService.findByPagination(0, 5);
			
			view.addObject("page", page); //enviar objeto para p�gina atrav�s do ModelAndView
			view.addObject("urlPagination", "/categoria/page"); //enviar objeto para p�gina atrav�s do ModelAndView
			view.setViewName("categoria/cadastro"); //se contiver erros acessa a mesma p�gina de cadastro novamente
			
			return view;
		}
		
		categoriaService.saveOrUpdate(categoria); //salva categoria se estiver validado
		
		view.setViewName("redirect:/categoria/add"); //salva e volta para mesma p�gina de cadastro que ser� onde est� alocada a list de categorias tamb�m
		
		return view; 
	}
	
	/**
	 * M�todo para criar uma p�gina b�sica com o formul�rio de cadastro de um objeto Categoria e ser� incluido tamb�m a listagem de categorias na mesma p�gina .jsp.
	 * 
	 * * @param categoria a @ModelAttribute categoria a ser capturado na tela atrav�s de sua referencia do modelAttribute.
	 * 
	 * @return view(acesso a p�gina cadastro.jsp do diret�rio categoria).
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView cadastro(@ModelAttribute("categoria") Categoria categoria) {
		 
		ModelAndView view = new ModelAndView("categoria/cadastro"); //diret�rio categoria / p�gina cadastro.jsp
		
		Page<Categoria> page = categoriaService.findByPagination(0, 5); 
		//cria um objeto onde iremos armazenar o retorno da consulta / 0 � a variavel informada na url por�m neste caso sempre ser� aberto a primeira p�gina do formul�rio(0 igual a Array) / 5 � o tamanho de itens por p�gina
		//objeto Page/Slice cont�m v�rias op��es uteis que pode ser utilizada na pagina.jsp ex.(getTotalPages, getTotalElements, getNumber, getNumberElements, getContent(cont�m a sua lista)
		//devido a todas essas op��es absorvidas do objeto Page n�o � necess�rio incluir o List de categorias pois o Page j� est� fazendo este trabalho
				
		//view.addObject("categorias", categoriaService.findAll());
		view.addObject("page", page); //n�o pode ser encontrado diretamente todas as categorias, necessita utilizar o Page acima pois sen�o ele n�o encontra as pagina��es ao abrir o formul�rio
		view.addObject("urlPagination", "/categoria/page");
				
		return view;		
	}
}