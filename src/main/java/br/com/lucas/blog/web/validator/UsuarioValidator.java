package br.com.lucas.blog.web.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import br.com.lucas.blog.entity.Usuario;

/**
 * @author Lucas.
 * 
 *         Classe que representa o Validador de Avatar implementando Validator com seus respectivos m�todos.
 * 
 */

public class UsuarioValidator implements Validator {

	//Valida��o dos campos da entidade atrav�s do Spring Validator
	
	public boolean supports(Class<?> clazz) { //testa se realmente o objeto que deseja validar � o mesmo
		
		return Usuario.class.equals(clazz); //testa se o objeto que est� recebendo � de uma classe do mesmo tipo que a informada
	}

	public void validate(Object target, Errors errors) { //m�todo que ir� criar as regras de valida��o / target � seu objeto (ex. classe Usuario) / errors � o objeto onde acessamos os campos que queremos testar para checar se est�o com erros ou n�o

		Usuario usuario = (Usuario) target;
		
		if(usuario.getNome() != null) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "nome", "Este campo � obrigat�rio."); //rejeita o campo nome da classe Usuario se estiver em branco ou se contiver somente espa�os em branco
			// parametros = 1� "nome" � o path do form:input na pagina cadastro.jsp, 2� "nome" � o path do form:errors, + mensagem 
		}
		
		if(usuario.getEmail() != null) {
			
			Pattern pattern = Pattern.compile(".+@.+\\..+"); //informa o padr�o da expressoa regular que o campo e-mail necessita conter sen�o n�o ser� validado
			Matcher matcher = pattern.matcher(usuario.getEmail()); //se dar match com o e-mail capturado...
			
			if( !matcher.matches() ) { //se o match for diferente da express�o informada...
				errors.rejectValue("email", "email", "Insira um e-mail v�lido."); //informa os campos no path da pagina e informa a mensagem de erro
			}
		}
		
		if(usuario.getSenha() != null) {
			
			if(usuario.getSenha().length() > 8 || usuario.getSenha().length() < 3) { //se a senha capturada for entre 3 e 8 caracteres ok
				errors.rejectValue("senha", "senha", "A senha deve conter entre 3 e 8 caracteres."); //se n�o for ser� rejeitado, necessita informar os paths da pagina e a mensagem de erro
			}
		}
		
		if(usuario.getFile() != null) {
			
			if(usuario.getFile().getSize() == 0) { //se o tamanho do file for igual quer dizer que nenhum arquivo foi selecionado
				errors.rejectValue("file", "file", "Selecione uma imagem de at� 100kb.");
			}
		}
	}
}