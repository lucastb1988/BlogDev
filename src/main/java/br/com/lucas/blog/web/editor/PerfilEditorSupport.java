package br.com.lucas.blog.web.editor;

import java.beans.PropertyEditorSupport;

import br.com.lucas.blog.enumeradores.Perfil;

/**
 * @author Lucas.
 * 
 *         Classe que representa o editor do enum Perfil extendendo de PropertyEditorSupport no qual irá converter uma String em objeto/classe Perfil.
 * 
 */

public class PerfilEditorSupport extends PropertyEditorSupport{

	@Override
	public void setAsText(String text) throws IllegalArgumentException {

		// Utilizando Conversão - Recebendo String ADMIN/AUTOR/LEITOR do ENUM e necessita transformar esta String em objeto/classe Perfil
		
		if(text.equals("ADMIN")) {
			super.setValue(Perfil.ADMIN);
			
		} else if(text.equals("AUTOR")) {
			super.setValue(Perfil.AUTOR);
			
		} else {
			super.setValue(Perfil.LEITOR);
		}
	}	
}