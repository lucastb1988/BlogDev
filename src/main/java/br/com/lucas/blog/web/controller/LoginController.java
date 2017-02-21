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
 *         Classe que representa o controller de Login com seus respectivos métodos.
 * 
 */

@Controller
@RequestMapping("auth")
public class LoginController {

	/**
	 * Método para acessar a página login.jsp.
	 * 
	 * @return view(acesso a página login.jsp do diretório views).
	 */
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String loginPage() {
		
		return "login";
	}
	
	/**
	 * Método para validação e acesso ao aplicativo caso seja validado o login.
	 * 
	 * * @param error o @RequestParam error caso a validação não passe se houver algum erro de login.
	 * * @param logout o @RequestParam logout caso seja realizado logout.
	 * * @param model o modelmap a add atributos e serem referenciados na página jsp.
	 * 
	 * @return view(redirecionamento a página principal utilizando simbolo "/" como url caso passe na validação do login).
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) boolean error,
							  @RequestParam(value = "logout", required = false) boolean logout, ModelMap model) { //RequestParam utilizado na pagina login.jsp
		
		if(error) { //error como true referente ao error=true no SpringSecurityConfig
			model.addAttribute("error", "Login inválido, senha ou nome de usuário não confere.");
			return new ModelAndView("login", model); //retorna na página login.jsp
		}
		
		if(logout) { //logout como true referente ao logout=true no SpringSecurityConfig
			model.addAttribute("logout", "Usuário deslogado com sucesso.");
			return new ModelAndView("login", model); //retorna na página login.jsp
		}
		
		//caso não contenha erros de acesso e o usuário não deslogue do sistema, irá redirecionar para a página home.jsp
		return new ModelAndView("redirect:/");
	}
	
	/**
	 * Método que resulta em acesso negado caso logar no sistema e não contiver perfil de autorização competente para acessar tal página.
	 * 	 
	 * @return view(acesso a página error.jsp informando mensagem de acesso negado).
	 */
	//acessoNegado ocorre quando vc está logado no sistema porém não tem perfil de autorização competente para acessar tal página
	@RequestMapping(value = "/denied", method = RequestMethod.GET) // /denied foi configurado no SpringSecurityConfig
	public ModelAndView acessoNegado() {
		
		return new ModelAndView("error", "mensagem", "Acesso negado, área restrita."); //caso denied cai na página error.jsp + mensagem
	}
}