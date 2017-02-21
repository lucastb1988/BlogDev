package br.com.lucas.blog.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author Lucas.
 * 
 *         Classe que representa o Controller Advice de Exception com seus respectivos métodos.
 * 
 */

@ControllerAdvice // Esta anotação fará com que o controller fique aguardando por uma exceção, quando ele capturar uma exceção ele irá tratar essa exceção conforme os métodos criados nesta classe
public class ExceptionController {

	//método genérico para capturar qualquer exceção em todo seu aplicativo
	@ExceptionHandler(Exception.class) // Informa o Tratador de exceção
	public ModelAndView genericException(HttpServletRequest req, Exception ex) {

		ModelAndView view = new ModelAndView("error");

		view.addObject("mensagem", "Ocorreu um erro durante a operação, tente novamente."); //"mensagem" é utilizada na pagina error.jsp em $ + mensagem
		view.addObject("url", req.getRequestURI()); //"url" é utilizada na pagina error.jsp em $ + identificador de url acessado pelo HttpServletRequest
		view.addObject("excecao", ex); //"excecao" é utilizada na pagina error.jsp em $ + exceção capturada

		return view;
	}

	//método especifico para capturar qualquer exceção referente a exceção MaxUploadSizeExceededException em todo seu aplicativo
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ModelAndView maxUploadSizeExceededException(HttpServletRequest req, MaxUploadSizeExceededException ex) {

		ModelAndView view = new ModelAndView("error");

		view.addObject("mensagem", "O arquivo selecionado não pode ser maior que 100kb."); //"mensagem" é utilizada na pagina error.jsp em $ + mensagem
		view.addObject("url", req.getRequestURI()); //"url" é utilizada na pagina error.jsp em $ + identificador de url acessado pelo HttpServletRequest
		view.addObject("excecao", ex); //"excecao" é utilizada na pagina error.jsp em $ + exceção capturada

		return view;
	}

	//método especifico para capturar qualquer exceção referente a exceção NoHandlerFoundException em todo seu aplicativo
	//exceção especifica quando pega um URL 404 (Page not found)
	@ExceptionHandler(NoHandlerFoundException.class) // Informa o Tratador de exceção
	@ResponseStatus(value = HttpStatus.NOT_FOUND, code = HttpStatus.NOT_FOUND) //informa o status (NOT FOUND) após pegar a exceção e também informa o código deste http(404)
	public ModelAndView noHandlerFoundException(HttpServletRequest req, NoHandlerFoundException ex) {

		ModelAndView view = new ModelAndView("error");

		view.addObject("mensagem", "Ops :( <br> Esta página não existe através desta url."); //"mensagem" é utilizada na pagina error.jsp em $ + mensagem
		view.addObject("url", req.getRequestURI()); //"url" é utilizada na pagina error.jsp em $ + identificador da url acessado pelo HttpServletRequest
		view.addObject("excecao", ex); //"excecao" é utilizada na pagina error.jsp em $ + exceção capturada

		return view;
	}
}