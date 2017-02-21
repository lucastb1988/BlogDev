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
 *         Classe que representa o Controller Advice de Exception com seus respectivos m�todos.
 * 
 */

@ControllerAdvice // Esta anota��o far� com que o controller fique aguardando por uma exce��o, quando ele capturar uma exce��o ele ir� tratar essa exce��o conforme os m�todos criados nesta classe
public class ExceptionController {

	//m�todo gen�rico para capturar qualquer exce��o em todo seu aplicativo
	@ExceptionHandler(Exception.class) // Informa o Tratador de exce��o
	public ModelAndView genericException(HttpServletRequest req, Exception ex) {

		ModelAndView view = new ModelAndView("error");

		view.addObject("mensagem", "Ocorreu um erro durante a opera��o, tente novamente."); //"mensagem" � utilizada na pagina error.jsp em $ + mensagem
		view.addObject("url", req.getRequestURI()); //"url" � utilizada na pagina error.jsp em $ + identificador de url acessado pelo HttpServletRequest
		view.addObject("excecao", ex); //"excecao" � utilizada na pagina error.jsp em $ + exce��o capturada

		return view;
	}

	//m�todo especifico para capturar qualquer exce��o referente a exce��o MaxUploadSizeExceededException em todo seu aplicativo
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ModelAndView maxUploadSizeExceededException(HttpServletRequest req, MaxUploadSizeExceededException ex) {

		ModelAndView view = new ModelAndView("error");

		view.addObject("mensagem", "O arquivo selecionado n�o pode ser maior que 100kb."); //"mensagem" � utilizada na pagina error.jsp em $ + mensagem
		view.addObject("url", req.getRequestURI()); //"url" � utilizada na pagina error.jsp em $ + identificador de url acessado pelo HttpServletRequest
		view.addObject("excecao", ex); //"excecao" � utilizada na pagina error.jsp em $ + exce��o capturada

		return view;
	}

	//m�todo especifico para capturar qualquer exce��o referente a exce��o NoHandlerFoundException em todo seu aplicativo
	//exce��o especifica quando pega um URL 404 (Page not found)
	@ExceptionHandler(NoHandlerFoundException.class) // Informa o Tratador de exce��o
	@ResponseStatus(value = HttpStatus.NOT_FOUND, code = HttpStatus.NOT_FOUND) //informa o status (NOT FOUND) ap�s pegar a exce��o e tamb�m informa o c�digo deste http(404)
	public ModelAndView noHandlerFoundException(HttpServletRequest req, NoHandlerFoundException ex) {

		ModelAndView view = new ModelAndView("error");

		view.addObject("mensagem", "Ops :( <br> Esta p�gina n�o existe atrav�s desta url."); //"mensagem" � utilizada na pagina error.jsp em $ + mensagem
		view.addObject("url", req.getRequestURI()); //"url" � utilizada na pagina error.jsp em $ + identificador da url acessado pelo HttpServletRequest
		view.addObject("excecao", ex); //"excecao" � utilizada na pagina error.jsp em $ + exce��o capturada

		return view;
	}
}