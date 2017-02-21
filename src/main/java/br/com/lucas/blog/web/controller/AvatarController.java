package br.com.lucas.blog.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.lucas.blog.entity.Avatar;
import br.com.lucas.blog.entity.Usuario;
import br.com.lucas.blog.service.AvatarService;
import br.com.lucas.blog.service.UsuarioService;
import br.com.lucas.blog.web.validator.AvatarValidator;

/**
 * @author Lucas.
 * 
 *         Classe que representa o controller de Avatar com seus respectivos m�todos.
 * 
 */

@Controller
@RequestMapping("avatar")
public class AvatarController {	
	
	private static final Logger LOG = Logger.getLogger(AvatarController.class);

	@Autowired
	private AvatarService avatarService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	/**
	 * M�todo para editar um objeto Avatar.
	 * 
	 * * @param avatar o avatar a ser editado.
	 * * @param result o resultado da valida��o do avatar.
	 * 
	 * @return redirecionamento para p�gina jsp usuario/perfil + id capturado do usu�rio informado.
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@ModelAttribute("avatar") @Validated Avatar avatar, BindingResult result) {
		
		AvatarValidator validator = new AvatarValidator();
		validator.validate(avatar, result);	//este c�digo substitui a necessidade de inserir o ValidadorSpring no initBinder (como foi feito em UsuarioController para validar o usuario)
		
		if(result.hasErrors()) {
			return "avatar/atualizar";
		}
		
		Long id = avatar.getId();
		
		avatar = avatarService.getAvatarByUpload(avatar.getFile());
		avatar.setId(id);
		
		avatarService.saveOrUpdate(avatar);
		
		Usuario usuario = usuarioService.findByAvatar(avatar);
		
		return "redirect:/usuario/perfil/" + usuario.getId();
	}
	
	/**
	 * M�todo para pr�-editar um objeto Avatar.
	 * 
	 * * @param id a id a ser buscado para realizar update.
	 * * @param avatar a avatar a ser referenciado na p�gina jsp atrav�s de seu modelAttribute.
	 * 
	 * @return view(acesso a p�gina atualizar.jsp do diret�rio avatar).
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public ModelAndView preUpdate(@PathVariable("id") Long id, @ModelAttribute("avatar") Avatar avatar) {
		
		ModelAndView view = new ModelAndView("avatar/atualizar"); //define a p�gina jsp que constar� este m�todo
		
		view.addObject("id", id); //ir� necessitar deste id para depois consultar um avatar especifico / ir� retornar este id para p�gina atualizar.jsp
		
		return view;
	}
	
	/**
	 * M�todo para carregar o avatar do respectivo usu�rio selecionado.
	 * 
	 * * @param id a id a ser buscado para carregamento do avatar.
	 * 
	 * @return null.
	 */
	@RequestMapping(value = "/load/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> loadAvatar(@PathVariable("id") Long id) { //Setar o id utilizando o @PathVariable 
		
		Avatar avatar = avatarService.findById(id); //procura pelo id e o atribui a variavel
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf(avatar.getTipo())); //captura o tipo do avatar
		
		InputStream is = new ByteArrayInputStream(avatar.getAvatar()); //captura o avatar utilizando inputstream de bytes		
		try {
			return new ResponseEntity<byte[]>(IOUtils.toByteArray(is), headers, HttpStatus.OK);
			
		} catch (IOException e) {
			LOG.error("Ocorreu um erro ao recuperar o Avatar", e.getCause());
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				LOG.error("Ocorreu um erro ao fechar o stream do arquivo", e.getCause());
			}
		}
		
		return null;
	}	
}