package br.com.lucas.blog.util;

import java.text.Normalizer;

/**
 * @author Lucas.
 * 
 *         Classe utilit�ria para auxiliar na montagem do atributo permalink da Entidade Categoria.
 * 
 */

public class MyReplaceString {

	/**
	 * M�todo est�tico para formatar o atributo permalink da Entidade Categoria.
	 * 
	 * * @param value o valor a ser formatado.
	 * 
	 * @return um link formatado.
	 */
	public static String formatarPermalink(String value) {
		
		//Titulo da Postagem = "Persist�ncia com JPA!" ser� transformado utilizando permalink em -> ex."persistencia_com_jpa"
		
		String link = value.trim(); //m�todo trim elimina espa�os em branco do titulo
		
		link = link.toLowerCase(); //m�todo lowerCase transforma todas as letras do titulo em minuscula
		
		link = Normalizer.normalize(link, Normalizer.Form.NFD); // m�todo normalize remove os acentos da letra do titulo sem remover a letra
		 
		link = link.replaceAll("\\s", "_"); //substitui todos os espa�os por underline
		
		link = link.replaceAll("\\W", ""); //substitui caracteres especiais(ex. !) por vazio
		
		link = link.replaceAll("\\_+", "_"); //se caso tiver 2 ou + underlines no titulo ser� substituido por apenas 1 underline
			
		return link;
	}
}