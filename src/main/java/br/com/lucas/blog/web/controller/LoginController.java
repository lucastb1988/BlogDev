package br.com.lucas.blog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Lucas.
 * 
 *         Classe que representa o controller de Login com seus respectivos m�todos.
 * 
 */

@Controller
@RequestMapping("auth")
public class LoginController {

	/**
	 * M�todo para acessar a p�gina login.jsp.
	 * 
	 * @return view(acesso a p�gina login.jsp do diret�rio views).
	 */
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String loginPage() {
		
		return "login";
	}
	
	/**
	 * M�todo para valida��o e acesso ao aplicativo caso seja validado o login.
	 * 
	 * * @param error o @RequestParam error caso a valida��o n�o passe se houver algum erro de login.
	 * * @param logout o @RequestParam logout caso seja realizado logout.
	 * * @param model o modelmap a add atributos e serem referenciados na p�gina jsp.
	 * 
	 * @return view(redirecionamento a p�gina principal utilizando simbolo "/" como url caso passe na valida��o do login).
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) boolean error,
							  @RequestParam(value = "logout", required = false) boolean logout, ModelMap model) { //RequestParam utilizado na pagina login.jsp
		
		if(error) { //error como true referente ao error=true no SpringSecurityConfig
			model.addAttribute("error", "Login inv�lido, senha ou nome de usu�rio n�o confere.");
			return new ModelAndView("login", model); //retorna na p�gina login.jsp
		}
		
		if(logout) { //logout como true referente ao logout=true no SpringSecurityConfig
			model.addAttribute("logout", "Usu�rio deslogado com sucesso.");
			return new ModelAndView("login", model); //retorna na p�gina login.jsp
		}
		
		//caso n�o contenha erros de acesso e o usu�rio n�o deslogue do sistema, ir� redirecionar para a p�gina home.jsp
		return new ModelAndView("redirect:/");
	}
	
	/**
	 * M�todo que resulta em acesso negado caso logar no sistema e n�o contiver perfil de autoriza��o competente para acessar tal p�gina.
	 * 	 
	 * @return view(acesso a p�gina error.jsp informando mensagem de acesso negado).
	 */
	//acessoNegado ocorre quando vc est� logado no sistema por�m n�o tem perfil de autoriza��o competente para acessar tal p�gina
	@RequestMapping(value = "/denied", method = RequestMethod.GET) // /denied foi configurado no SpringSecurityConfig
	public ModelAndView acessoNegado() {
		
		return new ModelAndView("error", "mensagem", "Acesso negado, �rea restrita."); //caso denied cai na p�gina error.jsp + mensagem
	}
}