package br.com.lucas.blog.web.validator;

import org.springframework.validation.BindingResult;

import br.com.lucas.blog.entity.Postagem;

/**
 * @author Lucas.
 * 
 *         Classe que representa o Validador de Postagem com seus respectivos métodos.
 * 
 */

public class PostagemAjaxValidator {

	private Postagem postagem;

	private String status;

	private String tituloError;

	private String textoError;

	public void validar(BindingResult result) {

		if(result.hasFieldErrors("titulo")) {

			this.tituloError = result.getFieldError("titulo").getDefaultMessage();
		}

		if(result.hasFieldErrors("texto")) {

			this.textoError = result.getFieldError("texto").getDefaultMessage();
		}
	}

	/**
	 * @return the postagem
	 */
	public Postagem getPostagem() {
		return postagem;
	}

	/**
	 * @param postagem the postagem to set
	 */
	public void setPostagem(Postagem postagem) {
		this.postagem = postagem;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the tituloError
	 */
	public String getTituloError() {
		return tituloError;
	}

	/**
	 * @param tituloError the tituloError to set
	 */
	public void setTituloError(String tituloError) {
		this.tituloError = tituloError;
	}

	/**
	 * @return the textoError
	 */
	public String getTextoError() {
		return textoError;
	}

	/**
	 * @param textoError the textoError to set
	 */
	public void setTextoError(String textoError) {
		this.textoError = textoError;
	}


}
