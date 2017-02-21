package br.com.lucas.blog.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.lucas.blog.entity.Comentario;
import br.com.lucas.blog.entity.Postagem;
import br.com.lucas.blog.entity.UsuarioLogado;
import br.com.lucas.blog.service.ComentarioService;
import br.com.lucas.blog.service.PostagemService;
import br.com.lucas.blog.service.UsuarioService;

/**
 * @author Lucas.
 * 
 *         Classe que representa o controller de Comentario com seus respectivos m�todos.
 * 
 */

@Controller
@RequestMapping("comentario")
public class ComentarioController {

	@Autowired
	private ComentarioService comentarioService;

	@Autowired
	private PostagemService postagemService;

	@Autowired
	private UsuarioService usuarioService;

	/**
	 * M�todo para salvar um objeto Coment�rio.
	 * 
	 * * @param comentario o comentario a ser salvo o referenciando na p�gina jsp atrav�s de seu modelAttribute.
	 * * @param result o resultado da valida��o do coment�rio.
	 * * @param permalink o @RequestParam permalink para buscar um parametro no redirect.
	 * 
	 * @return view(redirecionamento para p�gina do HomeController / + seu permalink devido).
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("comentario") @Validated Comentario comentario, 
			BindingResult result,
			@RequestParam("permalink") String permalink, @AuthenticationPrincipal UsuarioLogado logado) {
		//ser� utilizado RequestParam para buscar um parametro no redirect

		Postagem postagem = postagemService.findByPermalink(permalink); //encontra a postagem por permalink

		if(result.hasErrors()) {
			return new ModelAndView("post", "postagem", postagem);
		}	

		comentario.setUsuario(usuarioService.findById(logado.getId()));

		comentario.setPostagem(postagem); //vincula postagem dentro do comentario no bd(relacionamento)

		comentarioService.save(comentario); //salva o comentario vinculado a postagem

		return new ModelAndView("redirect:/" + permalink); //ser� redirecionado para o link do HomeController
	}
}