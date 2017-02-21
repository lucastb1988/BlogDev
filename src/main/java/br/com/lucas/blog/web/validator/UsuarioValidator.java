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
 *         Classe que representa o Validador de Avatar implementando Validator com seus respectivos métodos.
 * 
 */

public class UsuarioValidator implements Validator {

	//Validação dos campos da entidade através do Spring Validator
	
	public boolean supports(Class<?> clazz) { //testa se realmente o objeto que deseja validar é o mesmo
		
		return Usuario.class.equals(clazz); //testa se o objeto que está recebendo é de uma classe do mesmo tipo que a informada
	}

	public void validate(Object target, Errors errors) { //método que irá criar as regras de validação / target é seu objeto (ex. classe Usuario) / errors é o objeto onde acessamos os campos que queremos testar para checar se estão com erros ou não

		Usuario usuario = (Usuario) target;
		
		if(usuario.getNome() != null) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "nome", "Este campo é obrigatório."); //rejeita o campo nome da classe Usuario se estiver em branco ou se contiver somente espaços em branco
			// parametros = 1º "nome" é o path do form:input na pagina cadastro.jsp, 2º "nome" é o path do form:errors, + mensagem 
		}
		
		if(usuario.getEmail() != null) {
			
			Pattern pattern = Pattern.compile(".+@.+\\..+"); //informa o padrão da expressoa regular que o campo e-mail necessita conter senão não será validado
			Matcher matcher = pattern.matcher(usuario.getEmail()); //se dar match com o e-mail capturado...
			
			if( !matcher.matches() ) { //se o match for diferente da expressão informada...
				errors.rejectValue("email", "email", "Insira um e-mail válido."); //informa os campos no path da pagina e informa a mensagem de erro
			}
		}
		
		if(usuario.getSenha() != null) {
			
			if(usuario.getSenha().length() > 8 || usuario.getSenha().length() < 3) { //se a senha capturada for entre 3 e 8 caracteres ok
				errors.rejectValue("senha", "senha", "A senha deve conter entre 3 e 8 caracteres."); //se não for será rejeitado, necessita informar os paths da pagina e a mensagem de erro
			}
		}
		
		if(usuario.getFile() != null) {
			
			if(usuario.getFile().getSize() == 0) { //se o tamanho do file for igual quer dizer que nenhum arquivo foi selecionado
				errors.rejectValue("file", "file", "Selecione uma imagem de até 100kb.");
			}
		}
	}
}